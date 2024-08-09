package me.lofro.handcuffs.items;

import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.items.client.HandcuffItemRenderer;
import me.lofro.handcuffs.link.LinkManager;
import me.lofro.handcuffs.utils.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class HandcuffsItem extends Item implements IAnimatable {

    public final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    @Unique public Pair<UUID, UUID> interacted;

    public HandcuffsItem() {
        super(new Properties().setISTER(() -> new Callable() {
            private final HandcuffItemRenderer renderer = new HandcuffItemRenderer();

            @Override
            public HandcuffItemRenderer call() {
                return this.renderer;
            }
        }));

        this.interacted = new Pair<>();
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack itemStack, PlayerEntity player, LivingEntity clickedEntity, Hand p_111207_4_) {
        if (!(clickedEntity instanceof ServerPlayerEntity)) {
            return ActionResultType.PASS;
        }

        ServerPlayerEntity clickedPlayer = (ServerPlayerEntity) clickedEntity;

        if (clickedPlayer.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem().equals(ModItems.HANDCUFFS.get())) {
            if (player.isSneaking()) {

                Map<UUID, UUID> linkedList = LinkManager.linkedPlayers;

                if (interacted.contains(clickedPlayer.getUniqueID())) {
                    removeInteracted(clickedPlayer.getUniqueID());
                    return ActionResultType.PASS;
                }

                if (linkedList.containsKey(clickedPlayer.getUniqueID()) || linkedList.containsValue(clickedPlayer.getUniqueID())) {
                    LinkManager.unLink(clickedPlayer, clickedPlayer.getEntityWorld());

                    return ActionResultType.PASS;
                }

                addInteracted(clickedPlayer.getUniqueID(), player.getEntityWorld());
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("close"));

        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public void addInteracted(UUID uuid, World world) {
        if (interacted.getFirst() == null && !(interacted.getSecond() == uuid)) {
            interacted.setFirst(uuid);
        } else if (interacted.getSecond() == null && !(interacted.getFirst() == uuid)) {
            interacted.setSecond(uuid);
        }

        if (interacted.getFirst() != null && interacted.getSecond() != null) {
            Map<UUID, UUID> linkedList = LinkManager.linkedPlayers;

            if (!(linkedList.containsKey(interacted.getFirst()) || linkedList.containsValue(interacted.getFirst()) || linkedList.containsKey(interacted.getSecond()) || linkedList.containsValue(interacted.getSecond()))) {

                PlayerEntity firstPlayer = world.getPlayerByUuid(interacted.getFirst());
                PlayerEntity secondPlayer = world.getPlayerByUuid(interacted.getSecond());

                if (firstPlayer != null && secondPlayer != null) {
                    LinkManager.link(firstPlayer, secondPlayer);
                }
            }
            interacted.setFirst(null);
            interacted.setSecond(null);
        }
    }

    public void removeInteracted(UUID uuid) {
        if (interacted.getFirst() == uuid) interacted.setFirst(null);
        if (interacted.getSecond() == uuid) interacted.setSecond(null);
    }

    public Pair<UUID, UUID> getInteracted() {
        return interacted;
    }

    @Override
    public ITextComponent getName() {
        return new StringTextComponent("Handcuffs");
    }

    @Override
    public ITextComponent getDisplayName(ItemStack p_200295_1_) {
        return new StringTextComponent("Handcuffs");
    }

}

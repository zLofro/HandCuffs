package me.lofro.handcuffs.listeners;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.items.ModItems;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HandcuffAnimHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.isServerWorld()) return;

        ModifierLayer<IAnimation> handcuffAnimation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayerEntity) player).get(new ResourceLocation(Main.MOD_ID, "player_animation"));

        if (ModItems.HANDCUFFS.get().equals(player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {
            if (handcuffAnimation != null && !handcuffAnimation.isActive()) {

                KeyframeAnimation animationPlayer = PlayerAnimationRegistry.getAnimation(new ResourceLocation(Main.MOD_ID, "handcuffs"));

                if (animationPlayer == null) return;

                handcuffAnimation.setAnimation(new KeyframeAnimationPlayer(animationPlayer));
            }
        } else if (handcuffAnimation != null && handcuffAnimation.isActive()) {
            handcuffAnimation.setAnimation(null);
        }
    }

    @SubscribeEvent
    public static void onEntityRender(EntityViewRenderEvent event) {
        if (event.getInfo() == null) return;
        Entity renderedEntity = event.getInfo().getRenderViewEntity();

        if (renderedEntity == null) return;

        World world = renderedEntity.world;

        if (world == null) return;

        PlayerEntity player = world.getPlayerByUuid(renderedEntity.getUniqueID());

        if (player != null) {
            ModifierLayer<IAnimation> handcuffAnimation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayerEntity) player).get(new ResourceLocation(Main.MOD_ID, "player_animation"));

            if (ModItems.HANDCUFFS.get().equals(player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {
                if (handcuffAnimation != null && !handcuffAnimation.isActive()) {

                    KeyframeAnimation animationPlayer = PlayerAnimationRegistry.getAnimation(new ResourceLocation(Main.MOD_ID, "handcuffs"));

                    if (animationPlayer == null) return;

                    handcuffAnimation.setAnimation(new KeyframeAnimationPlayer(animationPlayer));
                }
            } else if (handcuffAnimation != null && handcuffAnimation.isActive()) {
                handcuffAnimation.setAnimation(null);
            }
        }
    }

}

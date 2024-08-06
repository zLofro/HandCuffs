package me.lofro.handcuffs.listeners;

import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.items.HandcuffsItem;
import me.lofro.handcuffs.items.ModItems;
import me.lofro.handcuffs.link.LinkManager;
import me.lofro.handcuffs.utils.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusEvents {

    @SubscribeEvent
    public static void onPlayerInteractAtEntity(PlayerInteractEvent.EntityInteract event) {
        PlayerEntity player = event.getPlayer();

        if (ModItems.HANDCUFFS.get().equals(player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {
            event.setCanceled(true);
            return;
        }

        Item item = event.getItemStack().getItem();
        Entity clickedEntity = event.getTarget();

        if (item instanceof HandcuffsItem) {
            if (clickedEntity instanceof PlayerEntity) {
                PlayerEntity clickedPlayer = (PlayerEntity) clickedEntity;

                if (ModItems.HANDCUFFS.get().equals(clickedPlayer.getHeldItemOffhand().getItem())) {
                    if (player.isSneaking()) {
                        HandcuffsItem handcuffsItem = (HandcuffsItem) clickedPlayer.getHeldItemOffhand().getItem();

                        Pair<UUID, UUID> interacted = handcuffsItem.getInteracted();
                        Map<UUID, UUID> linkedList = LinkManager.linkedPlayers;

                        if (interacted.contains(clickedPlayer.getUniqueID())) {
                            handcuffsItem.removeInteracted(clickedPlayer.getUniqueID());
                            return;
                        }

                        if (linkedList.containsKey(clickedPlayer.getUniqueID()) || linkedList.containsValue(clickedPlayer.getUniqueID())) {
                            LinkManager.unLink(clickedPlayer, clickedPlayer.getEntityWorld());

                            return;
                        }

                        handcuffsItem.addInteracted(clickedPlayer.getUniqueID(), player.getEntityWorld());
                    }
                    return;
                }

                ItemStack offhandHandCuffs = new ItemStack(ModItems.HANDCUFFS.get());

                clickedPlayer.setItemStackToSlot(EquipmentSlotType.OFFHAND, offhandHandCuffs);

                if (player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() instanceof HandcuffsItem) {
                    ItemStack mainHandItemstack = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
                    mainHandItemstack.setCount(mainHandItemstack.getCount() - 1);
                } else if (player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem() instanceof HandcuffsItem) {
                    ItemStack offHandItemstack = player.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
                    offHandItemstack.setCount(offHandItemstack.getCount() - 1);
                }
            }
        }

        if (clickedEntity instanceof PlayerEntity) {
            PlayerEntity clickedPlayer = (PlayerEntity) clickedEntity;

            if (ModItems.KEY.get().equals(item)) {
                if (ModItems.HANDCUFFS.get().equals(clickedPlayer.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {

                    clickedPlayer.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.AIR));

                    player.addItemStackToInventory(new ItemStack(ModItems.HANDCUFFS.get()));
                }
            }
        }
    }

}

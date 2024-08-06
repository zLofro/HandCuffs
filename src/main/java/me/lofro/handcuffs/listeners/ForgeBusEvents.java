package me.lofro.handcuffs.listeners;

import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.items.HandcuffsItem;
import me.lofro.handcuffs.items.KeyItem;
import me.lofro.handcuffs.items.ModItems;
import me.lofro.handcuffs.link.LinkManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
                if (player.isSneaking()) return;
                PlayerEntity clickedPlayer = (PlayerEntity) clickedEntity;

                if (!clickedPlayer.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem().equals(ModItems.HANDCUFFS.get())) {
                    ItemStack offhandHandCuffs = new ItemStack(ModItems.HANDCUFFS.get());

                    clickedPlayer.setItemStackToSlot(EquipmentSlotType.OFFHAND, offhandHandCuffs);

                    event.getItemStack().setCount(event.getItemStack().getCount() - 1);
                }
            }
        }

        if (clickedEntity instanceof PlayerEntity) {
            PlayerEntity clickedPlayer = (PlayerEntity) clickedEntity;

            if (item instanceof KeyItem) {
                if (clickedPlayer.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem().equals(ModItems.HANDCUFFS.get())) {
                    player.addItemStackToInventory(clickedPlayer.getItemStackFromSlot(EquipmentSlotType.OFFHAND).copy());

                    clickedPlayer.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.AIR));

                    if (LinkManager.linkedPlayers.containsKey(clickedPlayer.getUniqueID()) || LinkManager.linkedPlayers.containsValue(clickedPlayer.getUniqueID())) {
                        LinkManager.unLink(clickedPlayer, clickedPlayer.getEntityWorld());
                    }
                }
            }
        }
    }

}

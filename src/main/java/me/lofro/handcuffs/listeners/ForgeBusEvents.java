package me.lofro.handcuffs.listeners;

import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.items.HandcuffItem;
import me.lofro.handcuffs.items.ModItems;
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

        if (item instanceof HandcuffItem) {
            if (clickedEntity instanceof ServerPlayerEntity) {
                ServerPlayerEntity clickedPlayer = (ServerPlayerEntity) clickedEntity;

                if (ModItems.HANDCUFFS.get().equals(clickedPlayer.getHeldItemOffhand().getItem())) {
                    return;
                }

                clickedPlayer.setItemStackToSlot(EquipmentSlotType.OFFHAND, event.getItemStack().copy());

                if (player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() instanceof HandcuffItem) {
                    player.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.AIR));
                } else if (player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem() instanceof HandcuffItem) {
                    player.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.AIR));
                }
            }
        }

        if (clickedEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity clickedPlayer = (ServerPlayerEntity) clickedEntity;

            if (ModItems.KEY.get().equals(item)) {
                if (ModItems.HANDCUFFS.get().equals(clickedPlayer.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {

                    HandcuffItem handcuffItem = (HandcuffItem) clickedPlayer.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem();
                    handcuffItem.setOpen(true);

                    clickedPlayer.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.AIR));

                    player.addItemStackToInventory(new ItemStack(ModItems.HANDCUFFS.get()));
                }
            }
        }
    }

}

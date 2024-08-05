package me.lofro.handcuffs.listeners;

import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.accessors.item.LeadItemAccessor;
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
            if (clickedEntity instanceof ServerPlayerEntity) {
                ServerPlayerEntity clickedPlayer = (ServerPlayerEntity) clickedEntity;

                if (ModItems.HANDCUFFS.get().equals(clickedPlayer.getHeldItemOffhand().getItem())) {
                    return;
                }

                clickedPlayer.setItemStackToSlot(EquipmentSlotType.OFFHAND, event.getItemStack().copy());

                if (player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() instanceof HandcuffsItem) {
                    player.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.AIR));
                } else if (player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem() instanceof HandcuffsItem) {
                    player.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.AIR));
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
            } else if (Items.LEAD.equals(item)) {
                if (ModItems.HANDCUFFS.get().equals(clickedPlayer.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {
                    LeadItemAccessor leadItemAccessor = (LeadItemAccessor) item;

                    Pair<UUID, UUID> interacted = leadItemAccessor.getInteracted$0();
                    Map<UUID, UUID> linkedList = LinkManager.linkedPlayers;

                    if (interacted.contains(clickedPlayer.getUniqueID())) {
                        leadItemAccessor.removeInteracted$0(clickedPlayer.getUniqueID());
                        return;
                    }

                    if (linkedList.containsKey(clickedPlayer.getUniqueID()) || linkedList.containsValue(clickedPlayer.getUniqueID())) {
                        LinkManager.unLink(clickedPlayer, clickedPlayer.getEntityWorld());

                        return;
                    }

                    leadItemAccessor.addInteracted$0(clickedPlayer.getUniqueID(), player.getEntityWorld());
                }
            }
        }
    }

}

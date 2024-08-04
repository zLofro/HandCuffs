package me.lofro.handcuffs.client;

import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventForgeRegistry {

    @SubscribeEvent
    public static void renderHandModify(RenderHandEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;

        if (player == null) return;

        if (ModItems.HANDCUFFS.get().equals(player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onClick(InputEvent.ClickInputEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;

        if (player == null) return;

        if (ModItems.HANDCUFFS.get().equals(player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onClickGui(GuiScreenEvent.MouseClickedEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;

        if (player == null) return;

        if (ModItems.HANDCUFFS.get().equals(player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRawMouse(InputEvent.RawMouseEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;

        if (player == null) return;

        if (ModItems.HANDCUFFS.get().equals(player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {
            event.setCanceled(true);
        }
    }

}

package me.lofro.handcuffs.networking;

import me.lofro.handcuffs.Main;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PacketRegistry {

    @SubscribeEvent
    public void commonSetup(final FMLCommonSetupEvent event) {
        //ModPacketHandler.register();
    }

}

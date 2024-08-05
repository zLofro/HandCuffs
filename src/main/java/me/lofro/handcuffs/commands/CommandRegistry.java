package me.lofro.handcuffs.commands;

import me.lofro.handcuffs.Main;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class CommandRegistry {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        new LinkCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

}

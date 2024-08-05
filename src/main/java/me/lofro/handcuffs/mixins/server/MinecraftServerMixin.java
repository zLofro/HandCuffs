package me.lofro.handcuffs.mixins.server;

import me.lofro.handcuffs.link.LinkManager;
import me.lofro.handcuffs.networking.ModPacketHandler;
import me.lofro.handcuffs.networking.packets.LinkPlayersS2C;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void modifyStop(CallbackInfo ci) {
        LinkManager.linkedPlayers.forEach((key, value) -> ModPacketHandler.sendToAllClients(new LinkPlayersS2C(false, key, value)));
    }

}

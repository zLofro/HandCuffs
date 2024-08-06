package me.lofro.handcuffs.mixins.server;

import me.lofro.handcuffs.link.LinkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow public abstract PlayerList getPlayerList();

    @Shadow public abstract String getServerOwner();

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void modifyStop(CallbackInfo ci) {
        LinkManager.linkedPlayers.clear();
    }

}

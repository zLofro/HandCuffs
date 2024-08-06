package me.lofro.handcuffs.mixins.server.networking;

import me.lofro.handcuffs.link.LinkManager;
import me.lofro.handcuffs.networking.ModPacketHandler;
import me.lofro.handcuffs.networking.packets.LinkPlayersS2C;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public class ServerPlayNetHandlerMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void modifyInit(MinecraftServer p_i1530_1_, NetworkManager p_i1530_2_, ServerPlayerEntity p_i1530_3_, CallbackInfo ci) {
        if (LinkManager.linkedPlayers.containsValue(p_i1530_3_.getUniqueID()) || LinkManager.linkedPlayers.containsKey(p_i1530_3_.getUniqueID())) {
            ModPacketHandler.sendToAllClients(new LinkPlayersS2C(true, p_i1530_3_.getUniqueID(), LinkManager.linkedPlayers.get(p_i1530_3_.getUniqueID())));
        }
        LinkManager.linkedPlayers.forEach((key, value) -> ModPacketHandler.sendToPlayer(p_i1530_3_, new LinkPlayersS2C(true, value, key)));
    }

}

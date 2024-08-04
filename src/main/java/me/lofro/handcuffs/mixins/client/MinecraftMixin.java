package me.lofro.handcuffs.mixins.client;

import me.lofro.handcuffs.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.IPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @Redirect(method = "processKeyBinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/play/ClientPlayNetHandler;sendPacket(Lnet/minecraft/network/IPacket;)V"))
    private void cancelSwapPacket(ClientPlayNetHandler instance, IPacket<?> p_147297_1_) {
        if (player == null) return;

        if (ModItems.HANDCUFFS.get().equals(player.getHeldItemOffhand().getItem())) {
            return;
        }

        instance.sendPacket(p_147297_1_);
    }

}

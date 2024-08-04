package me.lofro.handcuffs.mixins.client.entity;

import com.mojang.authlib.GameProfile;
import me.lofro.handcuffs.items.ModItems;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld p_i50991_1_, GameProfile p_i50991_2_) {
        super(p_i50991_1_, p_i50991_2_);
    }

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    private void cancelDrop(boolean p_225609_1_, CallbackInfoReturnable<Boolean> cir) {
        if (ModItems.HANDCUFFS.get().equals(getHeldItemOffhand().getItem()) && !p_225609_1_) {
            cir.setReturnValue(false);
        }
    }

}

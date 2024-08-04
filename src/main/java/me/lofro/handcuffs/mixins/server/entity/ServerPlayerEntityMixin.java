package me.lofro.handcuffs.mixins.server.entity;

import com.mojang.authlib.GameProfile;
import me.lofro.handcuffs.items.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow public abstract boolean isSpectator();
    @Shadow public abstract boolean isCreative();

    public ServerPlayerEntityMixin(World p_i241920_1_, BlockPos p_i241920_2_, float p_i241920_3_, GameProfile p_i241920_4_) {
        super(p_i241920_1_, p_i241920_2_, p_i241920_3_, p_i241920_4_);
    }

    @Inject(method = "onItemPickup", at = @At("HEAD"), cancellable = true)
    private void onItemPickUpCancel(Entity p_71001_1_, int p_71001_2_, CallbackInfo ci) {
        if (ModItems.HANDCUFFS.get().equals(getHeldItemOffhand().getItem())) {
            ci.cancel();
        }
    }

}

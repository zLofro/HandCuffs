package me.lofro.handcuffs.mixins.server.entity;

import com.mojang.authlib.GameProfile;
import me.lofro.handcuffs.items.ModItems;
import me.lofro.handcuffs.link.LinkManager;
import me.lofro.handcuffs.networking.ModPacketHandler;
import me.lofro.handcuffs.networking.packets.MovePlayerS2C;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTickModify(CallbackInfo ci) {
        if (!this.isServerWorld()) return;

        Map<UUID, UUID> linkedMap = LinkManager.linkedPlayers;

        if (linkedMap.containsKey(this.getUniqueID())) {
            UUID linkedPlayerUUID = linkedMap.get(getUniqueID());

            PlayerEntity linkedPlayer = getEntityWorld().getPlayerByUuid(linkedPlayerUUID);

            if (linkedPlayer != null) {
                if (getDistance(linkedPlayer) > 5) {

                    double x = linkedPlayer.getPosX() - this.getPosX();
                    double z = linkedPlayer.getPosZ() - this.getPosZ();
                    double distance = Math.sqrt(x * x + z * z);
                    double speed = 0.1;
                    double xSpeed = x / distance * speed;
                    double zSpeed = z / distance * speed;

                    Vector3d movement = new Vector3d(xSpeed, 0, zSpeed);

                    ModPacketHandler.sendToPlayer(((ServerPlayerEntity)(Object)this), new MovePlayerS2C(movement, getUniqueID()));
                }
            }
        } else if (linkedMap.containsValue(getUniqueID())) {
            AtomicReference<UUID> keyValue = new AtomicReference<>();

            linkedMap.forEach((key, value) -> {
                if (value.equals(getUniqueID())) {
                    keyValue.set(key);
                }
            });

            PlayerEntity linkedPlayer = getEntityWorld().getPlayerByUuid(keyValue.get());

            if (linkedPlayer != null) {
                if (getDistance(linkedPlayer) > 5) {
                    double x = linkedPlayer.getPosX() - this.getPosX();
                    double z = linkedPlayer.getPosZ() - this.getPosZ();
                    double distance = Math.sqrt((x * x) + (z * z));
                    double speed = 0.1;
                    double xSpeed = x / (distance * speed);
                    double zSpeed = z / (distance * speed);

                    Vector3d movement = new Vector3d(xSpeed, 0, zSpeed);

                    ModPacketHandler.sendToPlayer(((ServerPlayerEntity)(Object)this), new MovePlayerS2C(movement, getUniqueID()));
                }
            }
        }
    }

}

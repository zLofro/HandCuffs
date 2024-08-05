package me.lofro.handcuffs.mixins.server.entity;

import com.mojang.authlib.GameProfile;
import me.lofro.handcuffs.items.ModItems;
import me.lofro.handcuffs.link.LinkManager;
import me.lofro.handcuffs.networking.ModPacketHandler;
import me.lofro.handcuffs.networking.packets.LinkPlayersS2C;
import me.lofro.handcuffs.networking.packets.MovePlayerS2C;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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

    @Inject(method = "<init>", at = @At("TAIL"))
    private void modifyConstructor(MinecraftServer p_i45285_1_, ServerWorld p_i45285_2_, GameProfile p_i45285_3_, PlayerInteractionManager p_i45285_4_, CallbackInfo ci) {
        LinkManager.linkedPlayers.forEach((key, value) -> ModPacketHandler.sendToAllClients(new LinkPlayersS2C(true, key, value)));
    }

    @Inject(method = "disconnect", at = @At("TAIL"))
    private void modifyDisconnect(CallbackInfo ci) {
        LinkManager.linkedPlayers.forEach((key, value) -> ModPacketHandler.sendToAllClients(new LinkPlayersS2C(false, key, value)));
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
                    double y = linkedPlayer.getPosY() - this.getPosY();
                    double z = linkedPlayer.getPosZ() - this.getPosZ();
                    double distance = Math.sqrt((x * x) + (y * y) + (z * z));
                    double speed = 2.2;
                    double xSpeed = x / (distance * speed);
                    double ySpeed = y / (distance * speed);
                    double zSpeed = z / (distance * speed);

                    Vector3d movement = new Vector3d(xSpeed, ySpeed, zSpeed);

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
                    double y = linkedPlayer.getPosY() - this.getPosY();
                    double z = linkedPlayer.getPosZ() - this.getPosZ();
                    double distance = Math.sqrt((x * x) + (y * y) + (z * z));
                    double speed = 2.2;
                    double xSpeed = x / (distance * speed);
                    double ySpeed = y / (distance * speed);
                    double zSpeed = z / (distance * speed);

                    Vector3d movement = new Vector3d(xSpeed, ySpeed, zSpeed);

                    ModPacketHandler.sendToPlayer(((ServerPlayerEntity)(Object)this), new MovePlayerS2C(movement, getUniqueID()));
                }
            }
        }
    }

}

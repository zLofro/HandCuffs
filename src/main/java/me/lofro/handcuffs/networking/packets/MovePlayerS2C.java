package me.lofro.handcuffs.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MovePlayerS2C {

    public final Vector3d vector3d;
    public final UUID uuid;

    public MovePlayerS2C(PacketBuffer buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();

        this.vector3d = new Vector3d(x, y, z);
        this.uuid = buffer.readUniqueId();
    }

    public MovePlayerS2C(Vector3d vector3d, UUID uuid) {
        this.vector3d = vector3d;
        this.uuid = uuid;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeDouble(vector3d.x);
        buf.writeDouble(vector3d.y);
        buf.writeDouble(vector3d.z);
        buf.writeUniqueId(uuid);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {

            ClientWorld world = Minecraft.getInstance().world;

            if (world == null) return;

            PlayerEntity playerEntity = world.getPlayerByUuid(uuid);

            if (playerEntity == null) return;

            playerEntity.setMotion(vector3d.getX(), vector3d.getY(), vector3d.getZ());
        });

        return true;
    }

}

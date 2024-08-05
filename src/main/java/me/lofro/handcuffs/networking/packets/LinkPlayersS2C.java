package me.lofro.handcuffs.networking.packets;

import me.lofro.handcuffs.link.ClientLinkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Supplier;

public class LinkPlayersS2C {

    public final UUID firstPlayer;
    public final UUID secondPlayer;
    public final boolean createsLink;

    public LinkPlayersS2C(boolean createsLink ,UUID firstPlayer, @Nullable UUID secondPlayer) {
        this.createsLink = createsLink;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public LinkPlayersS2C(PacketBuffer buffer) {
        this.createsLink = buffer.readBoolean();
        this.firstPlayer = buffer.readUniqueId();
        this.secondPlayer = buffer.readUniqueId();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeBoolean(createsLink);
        buffer.writeUniqueId(firstPlayer);
        buffer.writeUniqueId(secondPlayer);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            if (createsLink) {
                ClientLinkManager.link(firstPlayer, secondPlayer);
            } else {
                ClientLinkManager.unLink(firstPlayer, secondPlayer);
            }
        });

        return true;
    }

}

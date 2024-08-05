package me.lofro.handcuffs.networking;

import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.networking.packets.LinkPlayersS2C;
import me.lofro.handcuffs.networking.packets.MovePlayerS2C;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModPacketHandler {

    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return ModPacketHandler.packetId++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Main.MOD_ID, "network"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(MovePlayerS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MovePlayerS2C::new)
                .encoder(MovePlayerS2C::toBytes)
                .consumer(MovePlayerS2C::handle)
                .add();

        INSTANCE.messageBuilder(LinkPlayersS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LinkPlayersS2C::new)
                .encoder(LinkPlayersS2C::toBytes)
                .consumer(LinkPlayersS2C::handle)
                .add();
    }

    public static <TPacket> void sendToPlayer(ServerPlayerEntity serverPlayer, TPacket message) {
        PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> serverPlayer);

        if (target == null) return;

        INSTANCE.send(target, message);
    }

    public static <TPacket> void sendToAllClients(TPacket message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

}

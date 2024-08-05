package me.lofro.handcuffs.link;

import me.lofro.handcuffs.accessors.entity.PlayerEntityAccessor;
import me.lofro.handcuffs.networking.ModPacketHandler;
import me.lofro.handcuffs.networking.packets.LinkPlayersS2C;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class LinkManager {

    public static final Map<UUID, UUID> linkedPlayers = new HashMap<>();

    public static void link(PlayerEntity firstPlayer, PlayerEntity secondPlayer) {
        linkedPlayers.put(firstPlayer.getUniqueID(), secondPlayer.getUniqueID());
        linkedPlayers.put(secondPlayer.getUniqueID(), firstPlayer.getUniqueID());

        ((PlayerEntityAccessor) secondPlayer).setShouldRenderRope$0(true);

        ModPacketHandler.sendToAllClients(new LinkPlayersS2C(true, firstPlayer.getUniqueID(), secondPlayer.getUniqueID()));
    }

    public static void unLink(PlayerEntity player, World world) {
        UUID uuid = player.getUniqueID();

        linkedPlayers.remove(uuid);

        if (((PlayerEntityAccessor) player).shouldRenderRope$0()) ((PlayerEntityAccessor) player).setShouldRenderRope$0(false);

        AtomicReference<UUID> atomicValueUUID = new AtomicReference<>();

        linkedPlayers.forEach((key, value) -> {
            if (value == uuid) atomicValueUUID.set(key);
        });

        UUID valueUUID = atomicValueUUID.get();

        if (valueUUID != null) {
            linkedPlayers.remove(valueUUID);

            PlayerEntity secondPlayer = world.getPlayerByUuid(valueUUID);

            if (secondPlayer != null) {
                if (((PlayerEntityAccessor) secondPlayer).shouldRenderRope$0()) ((PlayerEntityAccessor) secondPlayer).setShouldRenderRope$0(false);
            }

            ModPacketHandler.sendToAllClients(new LinkPlayersS2C(false, uuid, valueUUID));
        }
    }

}

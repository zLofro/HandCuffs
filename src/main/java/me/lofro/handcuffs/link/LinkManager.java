package me.lofro.handcuffs.link;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class LinkManager {

    public static final Map<UUID, UUID> linkedPlayers = new HashMap<>();

    public static void link(UUID firstPlayer, UUID secondPlayer) {
        linkedPlayers.put(firstPlayer, secondPlayer);
        linkedPlayers.put(secondPlayer, firstPlayer);
    }

    public static void unLink(UUID player) {
        linkedPlayers.remove(player);

        AtomicReference<UUID> atomicValueUUID = new AtomicReference<>();

        linkedPlayers.forEach((key, value) -> {
            if (value == player) atomicValueUUID.set(key);
        });

        UUID valueUUID = atomicValueUUID.get();

        if (valueUUID != null) {
            linkedPlayers.remove(valueUUID);
        }
    }

}

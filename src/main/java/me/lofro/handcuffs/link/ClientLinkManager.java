package me.lofro.handcuffs.link;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientLinkManager {

    public static final Map<UUID, UUID> linkedPlayers = new HashMap<>();

    public static void link(UUID firstPlayer, UUID secondPlayer) {
        linkedPlayers.put(firstPlayer, secondPlayer);
        linkedPlayers.put(secondPlayer, firstPlayer);
    }

    public static void unLink(UUID firstPlayer, UUID secondPlayer) {
        linkedPlayers.remove(firstPlayer);
        linkedPlayers.remove(secondPlayer);
    }

}

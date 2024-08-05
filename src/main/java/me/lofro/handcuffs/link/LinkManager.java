package me.lofro.handcuffs.link;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LinkManager {

    public static final Map<UUID, UUID> linkedPlayers = new HashMap<>();

    public static void link(PlayerEntity firstPlayer, PlayerEntity secondPlayer) {
        linkedPlayers.put(firstPlayer.getUniqueID(), secondPlayer.getUniqueID());
        linkedPlayers.put(secondPlayer.getUniqueID(), firstPlayer.getUniqueID());
    }

    public static void unLink(PlayerEntity player) {
        UUID object = linkedPlayers.remove(player.getUniqueID());
        linkedPlayers.remove(object);
    }

}

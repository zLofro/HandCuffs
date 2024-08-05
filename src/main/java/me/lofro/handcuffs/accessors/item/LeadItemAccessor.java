package me.lofro.handcuffs.accessors.item;

import me.lofro.handcuffs.utils.Pair;

import java.util.UUID;

public interface LeadItemAccessor {

    void addInteracted$0(UUID uuid);

    void removeInteracted$0(UUID uuid);

    Pair<UUID, UUID> getInteracted$0();

}

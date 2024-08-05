package me.lofro.handcuffs.accessors.item;

import me.lofro.handcuffs.utils.Pair;
import net.minecraft.world.World;

import java.util.UUID;

public interface LeadItemAccessor {

    void addInteracted$0(UUID uuid, World world);

    void removeInteracted$0(UUID uuid);

    Pair<UUID, UUID> getInteracted$0();

}

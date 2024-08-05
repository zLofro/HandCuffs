package me.lofro.handcuffs.mixins.server.item;

import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.accessors.item.LeadItemAccessor;
import me.lofro.handcuffs.link.LinkManager;
import me.lofro.handcuffs.utils.Pair;
import net.minecraft.item.LeadItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.UUID;

@Mixin(LeadItem.class)
public class LeadItemMixin implements LeadItemAccessor {

    @Unique public Pair<UUID, UUID> interacted$0 = new Pair<>();

    @Override
    public void addInteracted$0(UUID uuid) {
        if (interacted$0.getFirst() == null && !(interacted$0.getSecond() == uuid)) {
            interacted$0.setFirst(uuid);
        } else if (interacted$0.getSecond() == null && !(interacted$0.getFirst() == uuid)) {
            interacted$0.setSecond(uuid);
        }

        if (interacted$0.getFirst() != null && interacted$0.getSecond() != null) {
            Map<UUID, UUID> linkedList = LinkManager.linkedPlayers;

            if (!(linkedList.containsKey(interacted$0.getFirst()) || linkedList.containsValue(interacted$0.getFirst()) || linkedList.containsKey(interacted$0.getSecond()) || linkedList.containsValue(interacted$0.getSecond()))) {
                LinkManager.link(interacted$0.getFirst(), interacted$0.getSecond());
            }
            interacted$0.setFirst(null);
            interacted$0.setSecond(null);
        }
    }

    @Override
    public void removeInteracted$0(UUID uuid) {
        if (interacted$0.getFirst() == uuid) interacted$0.setFirst(null);
        if (interacted$0.getSecond() == uuid) interacted$0.setSecond(null);
    }

    @Override
    public Pair<UUID, UUID> getInteracted$0() {
        return interacted$0;
    }

}

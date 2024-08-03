package me.lofro.handcuffs.mixins.entity;

import me.lofro.handcuffs.accessors.ServerPlayerEntityAccessor;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityAccessor {

    @Unique public boolean handCuffs$handCuffed;

    @Override
    public void handcuff$0(boolean handcuffed) {
        //TODO MAKE THE ANIMATION.

        handCuffs$handCuffed = handcuffed;
    }

    @Override
    public boolean isHandcuffed$0() {
        return handCuffs$handCuffed;
    }

}

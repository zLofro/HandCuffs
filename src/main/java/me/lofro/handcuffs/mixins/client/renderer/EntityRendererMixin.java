package me.lofro.handcuffs.mixins.client.renderer;

import me.lofro.handcuffs.accessors.entity.EntityRendererAccessor;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> implements EntityRendererAccessor<T> {

    @Shadow protected abstract int getBlockLight(T p_225624_1_, BlockPos p_225624_2_);

    @Override
    public int getBlockLight$0(T p_225624_1_, BlockPos p_225624_2_) {
        return getBlockLight(p_225624_1_, p_225624_2_);
    }

}

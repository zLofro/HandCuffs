package me.lofro.handcuffs.accessors.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public interface EntityRendererAccessor<T extends Entity> {

    int getBlockLight$0(T p_225624_1_, BlockPos p_225624_2_);

}

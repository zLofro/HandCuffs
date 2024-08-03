package me.lofro.handcuffs.items.client;

import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.items.HandcuffItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HandcuffItemModel extends AnimatedGeoModel<HandcuffItem> {

    @Override
    public ResourceLocation getModelLocation(HandcuffItem handcuffItem) {
        return new ResourceLocation(Main.MOD_ID, "geo/handcuffs.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HandcuffItem handcuffItem) {
        return new ResourceLocation(Main.MOD_ID, "textures/item/handcuffs.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HandcuffItem handcuffItem) {
        return new ResourceLocation(Main.MOD_ID, "animations/handcuffs.animation.json");
    }

}

package me.lofro.handcuffs.items.client;

import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.items.HandcuffsItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HandcuffItemModel extends AnimatedGeoModel<HandcuffsItem> {

    @Override
    public ResourceLocation getModelLocation(HandcuffsItem handcuffsItem) {
        return new ResourceLocation(Main.MOD_ID, "geo/handcuffs.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HandcuffsItem handcuffsItem) {
        return new ResourceLocation(Main.MOD_ID, "textures/item/handcuffs.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HandcuffsItem handcuffsItem) {
        return new ResourceLocation(Main.MOD_ID, "animations/handcuffs.animation.json");
    }

}

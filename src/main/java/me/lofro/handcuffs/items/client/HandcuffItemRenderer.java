package me.lofro.handcuffs.items.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.lofro.handcuffs.items.HandcuffsItem;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class HandcuffItemRenderer extends GeoItemRenderer<HandcuffsItem> {

    public HandcuffItemRenderer() {
        super(new HandcuffItemModel());
    }

    @Override
    public void preparePositionRotationScale(GeoBone bone, MatrixStack stack) {
        super.preparePositionRotationScale(bone, stack);
    }
}

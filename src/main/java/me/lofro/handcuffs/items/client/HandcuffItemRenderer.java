package me.lofro.handcuffs.items.client;

import me.lofro.handcuffs.items.HandcuffItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class HandcuffItemRenderer extends GeoItemRenderer<HandcuffItem> {

    public HandcuffItemRenderer() {
        super(new HandcuffItemModel());
    }

}

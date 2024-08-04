package me.lofro.handcuffs.items;

import me.lofro.handcuffs.items.client.HandcuffItemRenderer;
import net.minecraft.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.concurrent.Callable;

public class HandcuffItem extends Item implements IAnimatable {

    public final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private boolean open;

    public HandcuffItem() {
        super(new Properties().setISTER(() -> new Callable() {
            private final HandcuffItemRenderer renderer = new HandcuffItemRenderer();

            @Override
            public HandcuffItemRenderer call() {
                return this.renderer;
            }
        }));
        open = false;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (open) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("open"));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("close"));
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}

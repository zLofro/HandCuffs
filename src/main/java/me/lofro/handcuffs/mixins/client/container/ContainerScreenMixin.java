package me.lofro.handcuffs.mixins.client.container;

import me.lofro.handcuffs.items.ModItems;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerScreen.class)
public class ContainerScreenMixin {

    @Shadow @Final protected PlayerInventory playerInventory;

    @Inject(method = "itemStackMoved", at = @At("HEAD"), cancellable = true)
    private void cancelMove(int p_195363_1_, int p_195363_2_, CallbackInfoReturnable<Boolean> cir) {
        if (ModItems.HANDCUFFS.get().equals(playerInventory.player.getHeldItemOffhand().getItem())) {
            cir.setReturnValue(false);
        }
    }

    @Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isActiveAndMatches(Lnet/minecraft/client/util/InputMappings$Input;)Z", ordinal = 2))
    private boolean modifyDropItem1(KeyBinding instance, InputMappings.Input input) {
        if (ModItems.HANDCUFFS.get().equals(playerInventory.player.getHeldItemOffhand().getItem())) {
            return false;
        }
        return instance.isActiveAndMatches(input);
    }

    @Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isActiveAndMatches(Lnet/minecraft/client/util/InputMappings$Input;)Z", ordinal = 3))
    private boolean modifyDropItem2(KeyBinding instance, InputMappings.Input input) {
        if (ModItems.HANDCUFFS.get().equals(playerInventory.player.getHeldItemOffhand().getItem())) {
            return false;
        }
        return instance.isActiveAndMatches(input);
    }

}

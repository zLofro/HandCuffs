package me.lofro.handcuffs.mixins.server.inventory;


import me.lofro.handcuffs.items.ModItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow public abstract ItemStack getStackInSlot(int p_70301_1_);

    @Inject(method = "removeStackFromSlot", at = @At("HEAD"), cancellable = true)
    private void cancelRemoveStackFromSlot(int p_70304_1_, CallbackInfoReturnable<ItemStack> cir) {
        if (ModItems.HANDCUFFS.get().equals(getStackInSlot(EquipmentSlotType.OFFHAND.getSlotIndex()).getItem())) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }

}

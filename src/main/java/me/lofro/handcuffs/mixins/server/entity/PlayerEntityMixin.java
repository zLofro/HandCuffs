package me.lofro.handcuffs.mixins.server.entity;

import me.lofro.handcuffs.accessors.entity.PlayerEntityAccessor;
import me.lofro.handcuffs.items.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {

    @Unique private static final DataParameter<Boolean> shouldRenderRope$0 = EntityDataManager.createKey(PlayerEntityMixin.class, DataSerializers.BOOLEAN);

    @Unique public PlayerEntity linkedPlayer$0 = null;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> p_i48577_1_, World p_i48577_2_) {
        super(p_i48577_1_, p_i48577_2_);
    }

    @Shadow public abstract ItemStack getItemStackFromSlot(EquipmentSlotType p_184582_1_);

    @Inject(method = "canPickUpItem", at = @At("HEAD"), cancellable = true)
    private void modifyCanPickUp(ItemStack p_213365_1_, CallbackInfoReturnable<Boolean> cir) {
        if (ModItems.HANDCUFFS.get().equals(getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "registerData", at = @At("HEAD"))
    private void registerData(CallbackInfo ci) {
        this.dataManager.register(shouldRenderRope$0, false);
    }

    @Override
    public void setShouldRenderRope$0(boolean shouldRenderRope) {
        this.dataManager.set(shouldRenderRope$0, shouldRenderRope);
    }

    @Override
    public boolean shouldRenderRope$0() {
        return this.dataManager.get(shouldRenderRope$0);
    }

}

package me.lofro.handcuffs.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class KeyItem extends Item {

    public KeyItem() {
        super(new Item.Properties());
    }

    @Override
    public ITextComponent getName() {
        return new StringTextComponent("Key");
    }

    @Override
    public ITextComponent getDisplayName(ItemStack p_200295_1_) {
        return new StringTextComponent("Key");
    }

}

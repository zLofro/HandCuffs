package me.lofro.handcuffs.items;

import me.lofro.handcuffs.Main;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);

    public static final RegistryObject<Item> HANDCUFFS = ITEMS.register("handcuffs", HandcuffsItem::new);
    public static final RegistryObject<Item> KEY = ITEMS.register("key", KeyItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}

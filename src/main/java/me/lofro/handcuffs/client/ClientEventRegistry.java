package me.lofro.handcuffs.client;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import me.lofro.handcuffs.Main;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventRegistry {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        //Set the player construct callback. It can be a lambda function.
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                new ResourceLocation(Main.MOD_ID, "player_animation"),
                42,
                ClientEventRegistry::registerPlayerAnimation);
    }

    //This method will set your mods animation into the library.
    private static IAnimation registerPlayerAnimation(AbstractClientPlayerEntity player) {
        //This will be invoked for every new player
        return new ModifierLayer<>();
    }

}

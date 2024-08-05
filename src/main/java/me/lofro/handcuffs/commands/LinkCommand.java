package me.lofro.handcuffs.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lofro.handcuffs.items.ModItems;
import me.lofro.handcuffs.link.LinkManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.text.StringTextComponent;

import java.util.Map;
import java.util.UUID;

public class LinkCommand {

    public LinkCommand(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> literalArgumentBuilder = Commands.literal("linkManager").requires((source) -> source.hasPermissionLevel(4));

        literalArgumentBuilder.then(Commands.literal("link")
                .then(Commands.argument("firstPlayer", EntityArgument.player())
                        .then(Commands.argument("secondPlayer", EntityArgument.player())
                                .executes(source -> executeLink(source.getSource(), EntityArgument.getPlayer(source, "firstPlayer"), EntityArgument.getPlayer(source, "secondPlayer")))
                                )))
                .then(Commands.literal("unlink").then(Commands.argument("firstPlayer", EntityArgument.player()).executes(source -> executeUnLink(source.getSource(), EntityArgument.getPlayer(source, "firstPlayer")))));

        dispatcher.register(literalArgumentBuilder);
    }

    private int executeLink(CommandSource source, ServerPlayerEntity firstPlayer, ServerPlayerEntity secondPlayer) {
        if (!ModItems.HANDCUFFS.get().equals(firstPlayer.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem()) || !ModItems.HANDCUFFS.get().equals(secondPlayer.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {
            source.sendFeedback(new StringTextComponent("Al menos uno de los jugadores no esta esposado."), false);
            return 0;
        }


        Map<UUID, UUID> linkedList = LinkManager.linkedPlayers;

        if (linkedList.containsKey(firstPlayer.getUniqueID()) || linkedList.containsValue(firstPlayer.getUniqueID()) || linkedList.containsKey(secondPlayer.getUniqueID()) || linkedList.containsValue(secondPlayer.getUniqueID())) {
            source.sendFeedback(new StringTextComponent("Uno de los jugadores ya esta linkeado."), false);
            return 0;
        }

        source.sendFeedback(new StringTextComponent("Has linkeado a los jugadores."), false);

        LinkManager.link(firstPlayer, secondPlayer);

        return Command.SINGLE_SUCCESS;
    }

    private int executeUnLink(CommandSource source, ServerPlayerEntity playerEntity) {
        if (!ModItems.HANDCUFFS.get().equals(playerEntity.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem())) {
            source.sendFeedback(new StringTextComponent("El jugador no esta esposado."), false);
            return 0;
        }

        Map<UUID, UUID> linkedList = LinkManager.linkedPlayers;

        if (!(linkedList.containsKey(playerEntity.getUniqueID()) || linkedList.containsKey(playerEntity.getUniqueID()))) {
            source.sendFeedback(new StringTextComponent("El jugador no está linkeado."), false);

            return 0;
        }

        LinkManager.unLink(playerEntity);

        source.sendFeedback(new StringTextComponent("Has deslinkeado al jugador."), false);

        return Command.SINGLE_SUCCESS;
    }

}

package org.chubby.github.cultcraft.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.chubby.github.cultcraft.util.Capabilities;
import org.chubby.github.cultcraft.content.capability.BloodCap;

public class DebugCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("blood")
                        .requires(source -> source.hasPermission(2)) // Requires OP level 2

                        // /blood set <amount> - Set your own blood
                        .then(Commands.literal("set")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0, BloodCap.MAX_BLOOD_AMOUNT))
                                        .executes(DebugCommands::setBloodSelf)
                                )
                        )

                        // /blood set <player> <amount> - Set another player's blood
                        .then(Commands.literal("set")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0, BloodCap.MAX_BLOOD_AMOUNT))
                                                .executes(DebugCommands::setBloodOther)
                                        )
                                )
                        )

                        // /blood add <amount> - Add blood to yourself
                        .then(Commands.literal("add")
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(DebugCommands::addBloodSelf)
                                )
                        )

                        // /blood add <player> <amount> - Add blood to another player
                        .then(Commands.literal("add")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                .executes(DebugCommands::addBloodOther)
                                        )
                                )
                        )

                        // /blood get - Check your blood
                        .then(Commands.literal("get")
                                .executes(DebugCommands::getBloodSelf)
                        )

                        // /blood get <player> - Check another player's blood
                        .then(Commands.literal("get")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(DebugCommands::getBloodOther)
                                )
                        )

                        // /blood fill - Fill your blood to max
                        .then(Commands.literal("fill")
                                .executes(DebugCommands::fillBloodSelf)
                        )

                        // /blood fill <player> - Fill another player's blood to max
                        .then(Commands.literal("fill")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(DebugCommands::fillBloodOther)
                                )
                        )

                        // /blood empty - Empty your blood
                        .then(Commands.literal("empty")
                                .executes(DebugCommands::emptyBloodSelf)
                        )

                        // /blood empty <player> - Empty another player's blood
                        .then(Commands.literal("empty")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(DebugCommands::emptyBloodOther)
                                )
                        )
        );
    }

    // Set blood for yourself
    private static int setBloodSelf(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            int amount = IntegerArgumentType.getInteger(context, "amount");

            if (Capabilities.setBlood(player, amount)) {
                context.getSource().sendSuccess(
                        () -> Component.literal("Set blood to " + amount + "/" + BloodCap.MAX_BLOOD_AMOUNT),
                        false
                );
                return Command.SINGLE_SUCCESS;
            } else {
                context.getSource().sendFailure(Component.literal("Failed to set blood (capability not found)"));
                return 0;
            }
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }

    // Set blood for another player
    private static int setBloodOther(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer target = EntityArgument.getPlayer(context, "player");
            int amount = IntegerArgumentType.getInteger(context, "amount");

            if (Capabilities.setBlood(target, amount)) {
                context.getSource().sendSuccess(
                        () -> Component.literal("Set " + target.getName().getString() + "'s blood to " + amount + "/" + BloodCap.MAX_BLOOD_AMOUNT),
                        true
                );

                // Also notify the target player
                target.sendSystemMessage(Component.literal("Your blood was set to " + amount + "/" + BloodCap.MAX_BLOOD_AMOUNT));

                return Command.SINGLE_SUCCESS;
            } else {
                context.getSource().sendFailure(Component.literal("Failed to set blood (capability not found)"));
                return 0;
            }
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }

    // Add blood to yourself
    private static int addBloodSelf(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            int amount = IntegerArgumentType.getInteger(context, "amount");

            BloodCap blood = Capabilities.getBlood(player);
            if (blood != null) {
                int oldAmount = blood.getBloodAmt();
                blood.addBlood(amount);
                int newAmount = blood.getBloodAmt();

                context.getSource().sendSuccess(
                        () -> Component.literal("Blood: " + oldAmount + " → " + newAmount + " (" + (amount > 0 ? "+" : "") + amount + ")"),
                        false
                );
                return Command.SINGLE_SUCCESS;
            } else {
                context.getSource().sendFailure(Component.literal("Failed to add blood (capability not found)"));
                return 0;
            }
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }

    // Add blood to another player
    private static int addBloodOther(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer target = EntityArgument.getPlayer(context, "player");
            int amount = IntegerArgumentType.getInteger(context, "amount");

            BloodCap blood = Capabilities.getBlood(target);
            if (blood != null) {
                int oldAmount = blood.getBloodAmt();
                blood.addBlood(amount);
                int newAmount = blood.getBloodAmt();

                context.getSource().sendSuccess(
                        () -> Component.literal(target.getName().getString() + "'s blood: " + oldAmount + " → " + newAmount + " (" + (amount > 0 ? "+" : "") + amount + ")"),
                        true
                );

                target.sendSystemMessage(Component.literal("Your blood: " + oldAmount + " → " + newAmount + " (" + (amount > 0 ? "+" : "") + amount + ")"));

                return Command.SINGLE_SUCCESS;
            } else {
                context.getSource().sendFailure(Component.literal("Failed to add blood (capability not found)"));
                return 0;
            }
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }

    // Get your own blood
    private static int getBloodSelf(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            BloodCap blood = Capabilities.getBlood(player);

            if (blood != null) {
                int amount = blood.getBloodAmt();
                float percentage = blood.getBloodPercentage() * 100;

                context.getSource().sendSuccess(
                        () -> Component.literal("Blood: " + amount + "/" + BloodCap.MAX_BLOOD_AMOUNT + " (" + String.format("%.1f", percentage) + "%)"),
                        false
                );
                return amount;
            } else {
                context.getSource().sendFailure(Component.literal("Blood capability not found"));
                return 0;
            }
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }

    // Get another player's blood
    private static int getBloodOther(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer target = EntityArgument.getPlayer(context, "player");
            BloodCap blood = Capabilities.getBlood(target);

            if (blood != null) {
                int amount = blood.getBloodAmt();
                float percentage = blood.getBloodPercentage() * 100;

                context.getSource().sendSuccess(
                        () -> Component.literal(target.getName().getString() + "'s blood: " + amount + "/" + BloodCap.MAX_BLOOD_AMOUNT + " (" + String.format("%.1f", percentage) + "%)"),
                        false
                );
                return amount;
            } else {
                context.getSource().sendFailure(Component.literal("Blood capability not found for " + target.getName().getString()));
                return 0;
            }
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }

    // Fill your blood to max
    private static int fillBloodSelf(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();

            if (Capabilities.setBlood(player, BloodCap.MAX_BLOOD_AMOUNT)) {
                context.getSource().sendSuccess(
                        () -> Component.literal("Blood filled to maximum (" + BloodCap.MAX_BLOOD_AMOUNT + ")"),
                        false
                );
                return Command.SINGLE_SUCCESS;
            } else {
                context.getSource().sendFailure(Component.literal("Failed to fill blood"));
                return 0;
            }
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }

    // Fill another player's blood to max
    private static int fillBloodOther(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer target = EntityArgument.getPlayer(context, "player");

            if (Capabilities.setBlood(target, BloodCap.MAX_BLOOD_AMOUNT)) {
                context.getSource().sendSuccess(
                        () -> Component.literal("Filled " + target.getName().getString() + "'s blood to maximum"),
                        true
                );

                target.sendSystemMessage(Component.literal("Your blood was filled to maximum"));

                return Command.SINGLE_SUCCESS;
            } else {
                context.getSource().sendFailure(Component.literal("Failed to fill blood"));
                return 0;
            }
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }

    // Empty your blood
    private static int emptyBloodSelf(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();

            if (Capabilities.setBlood(player, 0)) {
                context.getSource().sendSuccess(
                        () -> Component.literal("Blood emptied to 0"),
                        false
                );
                return Command.SINGLE_SUCCESS;
            } else {
                context.getSource().sendFailure(Component.literal("Failed to empty blood"));
                return 0;
            }
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }

    // Empty another player's blood
    private static int emptyBloodOther(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer target = EntityArgument.getPlayer(context, "player");

            if (Capabilities.setBlood(target, 0)) {
                context.getSource().sendSuccess(
                        () -> Component.literal("Emptied " + target.getName().getString() + "'s blood to 0"),
                        true
                );

                target.sendSystemMessage(Component.literal("Your blood was emptied"));

                return Command.SINGLE_SUCCESS;
            } else {
                context.getSource().sendFailure(Component.literal("Failed to empty blood"));
                return 0;
            }
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }
}
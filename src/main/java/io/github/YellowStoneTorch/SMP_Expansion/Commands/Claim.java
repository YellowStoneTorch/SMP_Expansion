package io.github.YellowStoneTorch.SMP_Expansion.Commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.YellowStoneTorch.SMP_Expansion.Claims.ClaimManager;
import io.github.YellowStoneTorch.SMP_Expansion.Config;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.text;

//@formatter:off
/**
 * Command for managing player claims
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Claim {
	/**
	 * Get command logic to register command
	 * @return command logic
	 */
	public static LiteralCommandNode<CommandSourceStack> createCommand() {
		return Commands.literal("claim")
				.then(add())
				.then(list())
				.then(remove())
				.build();
	}

	/**
	 * Get command logic to claim a new chunk
	 * @return command logic to claim a new chunk
	 */
	private static LiteralCommandNode<CommandSourceStack> add() {
		return Commands.literal("add")
				.then(Commands.literal("outpost")
						.then(Commands.argument("outpost name", StringArgumentType.string())
								.then(Commands.argument("outpost color", ArgumentTypes.namedColor())
										.executes(context -> {
											if (context.getSource().getSender() instanceof Player player) {
												if (Config.getAllowClaims()) {
													String name = context.getArgument("outpost name", String.class);
													NamedTextColor color = context.getArgument("outpost color", NamedTextColor.class);
													ClaimManager.claimOutpost(name, color, player, player.getChunk());
												}
												else
													player.sendMessage(text("Claiming new chunks has been disabled by the server.", NamedTextColor.RED));
											}
											return 1;
										}))
								.executes(context -> {
									if (context.getSource().getSender() instanceof Player player) {
										if (Config.getAllowClaims()) {
											String name = context.getArgument("outpost name", String.class);
											ClaimManager.claimOutpost(name, NamedTextColor.WHITE, player, player.getChunk());
										}
										else
											player.sendMessage(text("Claiming new chunks has been disabled by the server.", NamedTextColor.RED));
									}
									return 1;
								})))
				.then(Commands.literal("chunk")
						.then(Commands.argument("outpost name", StringArgumentType.string())
								.executes(context -> {
									if (context.getSource().getSender() instanceof Player player) {
										if (Config.getAllowClaims()) {
											String name = context.getArgument("outpost name", String.class);
											ClaimManager.claimChunk(name, player, player.getChunk());
										}
										else
											player.sendMessage(text("Claiming new chunks has been disabled by the server.", NamedTextColor.RED));
									}
									return 1;
								})))
				.build();
	}

	/**
	 * Get command logic to list all outposts
	 * @return command logic to list all outposts
	 */
	private static LiteralCommandNode<CommandSourceStack> list() {
		return Commands.literal("list")
				.executes(context -> {
					if (context.getSource().getSender() instanceof Player player)
						ClaimManager.listOutposts(player);
					return 1;
				})
				.build();
	}

	/**
	 * Get command logic to remove a claim or outpost
	 * @return command logic to remove claims
	 */
	private static LiteralCommandNode<CommandSourceStack> remove() {
		return Commands.literal("remove")
				.then(Commands.argument("confirmation", StringArgumentType.string())
						.executes(context -> {
							if (context.getSource().getSender() instanceof Player player) {
								String confirmation = context.getArgument("confirmation", String.class);
								ClaimManager.removeClaim(player, player.getChunk(), confirmation);
							}
							return 1;
						}))
				.build();
	}
}

package io.github.YellowStoneTorch.SMP_Expansion.Commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.YellowStoneTorch.SMP_Expansion.Config;
import io.github.YellowStoneTorch.SMP_Expansion.Teams.PlayerTeam;
import io.github.YellowStoneTorch.SMP_Expansion.Teams.TeamManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.text;

//@formatter:off
/**
 * Command for managing teams
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Team {
	/**
	 * Get command logic to register command
	 * @return command logic
	 */
	public static LiteralCommandNode<CommandSourceStack> createCommand() {
		return Commands.literal("team")
				.then(create())
				.then(list())
				.then(pvp())
				.then(invite())
				.then(accept())
				.then(decline())
				.then(kick())
				.then(leave())
				.then(delete())
				.build();
	}

	/**
	 * Get command logic for creating teams
	 * @return command logic for creating teams
	 */
	private static LiteralCommandNode<CommandSourceStack> create() {
		return Commands.literal("create")
				.requires(source -> (source.getSender() instanceof Player player && TeamManager.getPlayerTeam(player) == null))
				.then(Commands.argument("team name", StringArgumentType.string())
						.then(Commands.argument("team color", ArgumentTypes.namedColor())
								.executes(context -> {
									if (context.getSource().getSender() instanceof Player player) {
										if (Config.getAllowTeams()) {
											String name = context.getArgument("team name", String.class);
											NamedTextColor color = context.getArgument("team color", NamedTextColor.class);
											TeamManager.createTeam(name, color, player);
										}
										else
											player.sendMessage(text("Creating teams has been disabled by the server.", NamedTextColor.RED));
									}
									return 1;
								}))
						.executes(context -> {
							if (context.getSource().getSender() instanceof Player player) {
								if (Config.getAllowTeams()) {
									String name = context.getArgument("team name", String.class);
									TeamManager.createTeam(name, NamedTextColor.WHITE, player);
								}
								else
									player.sendMessage(text("Creating teams has been disabled by the server.", NamedTextColor.RED));
							}
							return 1;
						}))
				.build();
	}

	/**
	 * Get command logic for listing players on the team
	 * @return command logic for listing players on the team
	 */
	private static LiteralCommandNode<CommandSourceStack> list() {
		return Commands.literal("list")
				.requires(source -> (source.getSender() instanceof Player player && TeamManager.getPlayerTeam(player) != null))
				.executes(context -> {
					if (context.getSource().getSender() instanceof Player player)
						TeamManager.listPlayers(player);
					return 1;
				})
				.build();
	}

	/**
	 * Get command logic for viewing and toggling teammate pvp
	 * @return command logic for viewing and toggling teammate pvp
	 */
	private static LiteralCommandNode<CommandSourceStack> pvp() {
		return Commands.literal("pvp")
				.requires(source -> (source.getSender() instanceof Player player && TeamManager.getPlayerTeam(player) != null))
				.then(Commands.literal("check")
						.executes(context -> {
							if (context.getSource().getSender() instanceof Player player)
								TeamManager.checkPvP(player);
							return 1;
						}))
				.then(Commands.literal("set")
						.requires(source -> {
							if (source.getSender() instanceof Player player && TeamManager.getPlayerTeam(player) instanceof PlayerTeam team)
								return team.getLeader().equals(player);
							return false;
						})
						.then(Commands.argument("canPvP", BoolArgumentType.bool())
								.executes(context -> {
									if (context.getSource().getSender() instanceof Player player) {
										boolean canPvP = context.getArgument("canPvP", boolean.class);
										TeamManager.setPvP(player, canPvP);
									}
									return 1;
								})))
				.build();
	}

	/**
	 * Get command logic for inviting players on the team
	 * @return command logic for inviting players on the team
	 */
	private static LiteralCommandNode<CommandSourceStack> invite() {
		return Commands.literal("invite")
				.requires(source -> (source.getSender() instanceof Player player && TeamManager.getPlayerTeam(player) instanceof PlayerTeam team && team.getLeader().equals(player)))
				.then(Commands.argument("invitee", ArgumentTypes.player())
						.executes(context -> {
							if (context.getSource().getSender() instanceof Player inviter) {
								PlayerSelectorArgumentResolver selector = context.getArgument("invitee", PlayerSelectorArgumentResolver.class);
								Player invitee = selector.resolve(context.getSource()).getFirst();
								TeamManager.invitePlayer(invitee, inviter);
							}
							return 1;
						}))
				.build();
	}

	/**
	 * Get command logic for accepting invites
	 * @return command logic for accepting invites
	 */
	private static LiteralCommandNode<CommandSourceStack> accept() {
		return Commands.literal("accept")
				.requires(source -> (source.getSender() instanceof Player player && TeamManager.getPlayerTeam(player) == null))
				.executes(context -> {
					if (context.getSource().getSender() instanceof Player invitee)
						TeamManager.acceptInvite(invitee);
					return 1;
				})
				.build();
	}

	/**
	 * Get command logic for declining invites
	 * @return command logic for declining invites
	 */
	private static LiteralCommandNode<CommandSourceStack> decline() {
		return Commands.literal("decline")
				.requires(source -> (source.getSender() instanceof Player player && TeamManager.getPlayerTeam(player) == null))
				.executes(context -> {
					if (context.getSource().getSender() instanceof Player invitee)
						TeamManager.declineInvite(invitee);
					return 1;
				})
				.build();
	}

	/**
	 * Get command logic for kicking players from the team
	 * @return command logic for kicking players from the team
	 */
	private static LiteralCommandNode<CommandSourceStack> kick() {
		return Commands.literal("kick")
				.requires(source -> (source.getSender() instanceof Player player && TeamManager.getPlayerTeam(player) instanceof PlayerTeam team && team.getLeader().equals(player)))
				.then(Commands.argument("member", StringArgumentType.string())
						.executes(context -> {
							if (context.getSource().getSender() instanceof Player kicker) {
								String name = context.getArgument("member", String.class);
								OfflinePlayer kicked = Bukkit.getOfflinePlayer(name);
								TeamManager.kickPlayer(kicked, kicker);
							}
							return 1;
						}))
				.build();
	}

	/**
	 * Get command logic for leaving teams
	 * @return command logic for leaving teams
	 */
	private static LiteralCommandNode<CommandSourceStack> leave() {
		return Commands.literal("leave")
				.requires(source -> (source.getSender() instanceof Player player && TeamManager.getPlayerTeam(player) instanceof PlayerTeam team && !team.getLeader().equals(player)))
				.executes(context -> {
					if (context.getSource().getSender() instanceof Player player)
						TeamManager.leaveTeam(player);
					return 1;
				})
				.build();
	}

	/**
	 * Get command logic for deleting teams
	 * @return command logic for deleting teams
	 */
	private static LiteralCommandNode<CommandSourceStack> delete() {
		return Commands.literal("delete")
				.requires(source -> (source.getSender() instanceof Player player && TeamManager.getPlayerTeam(player) instanceof PlayerTeam team && team.getLeader().equals(player)))
				.executes(context -> {
					if (context.getSource().getSender() instanceof Player player)
						TeamManager.deleteTeam(player);
					return 1;
				})
				.build();
	}
}

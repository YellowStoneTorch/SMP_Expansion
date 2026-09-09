package io.github.YellowStoneTorch.SMP_Expansion.Teams;

import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.YellowStoneTorch.SMP_Expansion.Teams.PlayerTeam.scoreboard;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.*;

/**
 * Manages player teams
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class TeamManager {
	static final File teamFiles;
	private static final HashMap<OfflinePlayer, PlayerTeam> mapOfLeaders;
	private static final HashMap<OfflinePlayer, PlayerTeam> mapOfPlayerTeams;
	private static final ArrayList<TeamInvite> invites;
	private static final TextComponent.Builder prefix;

	static {
		mapOfLeaders = new HashMap<>();
		mapOfPlayerTeams = new HashMap<>();
		invites = new ArrayList<>();
		prefix = unite(comp("["), comp("Teams", NamedTextColor.AQUA, TextDecoration.BOLD), comp("] "));
		teamFiles = new File(Bukkit.getWorldContainer(), "plugins/SMP_Expansion/player_teams");
		if (!teamFiles.isDirectory())
			//noinspection ResultOfMethodCallIgnored
			teamFiles.mkdirs();
	}

	/**
	 * Gets the team this player is on
	 * @param player player to get team of
	 * @return player's team, or null if not on a team
	 */
	@Nullable
	public static PlayerTeam getPlayerTeam(@NotNull OfflinePlayer player) {
		return mapOfPlayerTeams.get(player);
	}

	/**
	 * Reads the json files and loads teams
	 */
	public static void loadTeams() {
		try {
			File[] teams = teamFiles.listFiles();
			if (teams == null)
				throw new IOException("Directory plugins/SMP_Expansion/player_teams not found");
			ArrayList<Team> minecraftTeams = new ArrayList<>();
			for (File file: teams) {
				PlayerTeam team = PlayerTeam.fromFile(file);
				mapOfLeaders.put(team.getLeader(), team);
				for (OfflinePlayer member: team.getMembers())
					mapOfPlayerTeams.put(member, team);
				minecraftTeams.add(team.getMinecraftTeam());
			}
			for (Team team: scoreboard.getTeams()) {
				if (!minecraftTeams.contains(team))
					team.unregister();
			}
		}
		catch (IOException e) {
			SMP_Expansion.loadIOException(e);
		}
	}

	/**
	 * Attempts to create a new team and save it to its file<br>
	 * Player must not currently be in a team and the team name must be unique
	 * @param name name of the new team
	 * @param color color of the new team
	 * @param player player that created the team
	 */
	public static void createTeam(@NotNull String name, @NotNull NamedTextColor color, @NotNull Player player) {
		if (getPlayerTeam(player) != null) {
			player.sendMessage(build(prefix, comp("Leave your team first before creating a new one.", NamedTextColor.RED)));
			return;
		}
		for (PlayerTeam team: mapOfLeaders.values()) {
			if (team.getTeamName().content().equals(name)) {
				player.sendMessage(build(prefix, comp("A team with that name already exists.", NamedTextColor.RED)));
				return;
			}
		}
		try {
			TextComponent.Builder teamName = comp(name, color, TextDecoration.BOLD);
			PlayerTeam newTeam = new PlayerTeam(teamName, player);
			player.sendMessage(build(prefix, comp("Created new team: ", NamedTextColor.YELLOW), newTeam.getTeamNameBuilder()));
			player.updateCommands();
			mapOfLeaders.put(player, newTeam);
			mapOfPlayerTeams.put(player, newTeam);
		}
		catch (IOException e) {
			SMP_Expansion.saveIOException(e);
		}
	}

	/**
	 * Attempts to list all players in this player's team
	 * @param player player that wants a list of their teammates
	 */
	public static void listPlayers(@NotNull Player player) {
		PlayerTeam team = TeamManager.getPlayerTeam(player);
		if (team == null) {
			player.sendMessage(build(prefix, comp("You are not currently in a team.", NamedTextColor.RED)));
			return;
		}
		player.sendMessage(build(prefix, comp("Players on team ", NamedTextColor.YELLOW), team.getTeamNameBuilder(), comp(":", NamedTextColor.YELLOW)));
		for (OfflinePlayer member: team.getMembers()) {
			if (member.equals(team.getLeader()))
				player.sendMessage(build(comp(member.getName()), comp(" (leader)", NamedTextColor.YELLOW)));
			else
				player.sendMessage(build(comp(member.getName())));
		}

	}

	/**
	 * Attempts to display this team's pvp status
	 * @param player player that wants to view pvp status
	 */
	public static void checkPvP(@NotNull Player player) {
		PlayerTeam team = getPlayerTeam(player);
		if (team == null) {
			player.sendMessage(build(prefix, comp("You are not in a team.", NamedTextColor.RED)));
			return;
		}
		if (team.canPvP())
			player.sendMessage(build(prefix, comp("Teammates on this team can PvP.", NamedTextColor.YELLOW)));
		else
			player.sendMessage(build(prefix, comp("Teammates on this team cannot PvP.", NamedTextColor.YELLOW)));
	}

	/**
	 * Attempts to set this team's pvp status<br>
	 * Only the leader can set a team's pvp status
	 * @param player player that wants to set pvp status
	 * @param canPvP whether pvp is allowed between teammates
	 */
	public static void setPvP(@NotNull Player player, boolean canPvP) {
		PlayerTeam team = getPlayerTeam(player);
		if (team == null) {
			player.sendMessage(build(prefix, comp("You are not in a team.", NamedTextColor.RED)));
			return;
		}
		if (!team.getLeader().equals(player)) {
			player.sendMessage(build(prefix, comp("Only the leader can set this team's PvP status.", NamedTextColor.RED)));
			return;
		}
		boolean currentPvP = team.canPvP();
		if (canPvP && currentPvP) {
			player.sendMessage(build(prefix, comp("Players on this team already can PvP.", NamedTextColor.RED)));
			return;
		}
		if (!canPvP && !currentPvP) {
			player.sendMessage(build(prefix, comp("Players on this team already cannot PvP.", NamedTextColor.RED)));
			return;
		}
		team.setPvP(canPvP);
		if (canPvP)
			player.sendMessage(build(prefix, comp("Teammates on this team can now PvP.", NamedTextColor.YELLOW)));
		else
			player.sendMessage(build(prefix, comp("Teammates on this team can no longer PvP.", NamedTextColor.YELLOW)));
	}

	/**
	 * Creates an invite a player to the inviter's team if they don't already have one sent<br>
	 * Only the leader can invite players<br>
	 * Cannot send an invite if invitee already has an invite<br>
	 * or inviter's last invite hasn't been resolved<br>
	 * Invites expire after 60 seconds
	 * @param invitee player to be invited
	 * @param inviter player that invited
	 */
	public static void invitePlayer(@NotNull Player invitee, @NotNull Player inviter) {
		PlayerTeam teamToJoin = getPlayerTeam(inviter);
		if (invitee.equals(inviter)) {
			inviter.sendMessage(build(prefix, comp("You cannot invite yourself.", NamedTextColor.RED)));
			return;
		}
		if (teamToJoin == null) {
			inviter.sendMessage(build(prefix, comp("You need to be in a team to invite players.", NamedTextColor.RED)));
			return;
		}
		if (!teamToJoin.getLeader().equals(inviter)) {
			inviter.sendMessage(build(prefix, comp("Only the leader may invite new players.", NamedTextColor.RED)));
			return;
		}
		if (getPlayerTeam(invitee) != null) {
			inviter.sendMessage(build(prefix, comp("This player is already in a team.", NamedTextColor.RED)));
			return;
		}
		for (TeamInvite invite: invites) {
			if (invitee.equals(invite.invitee)) {
				inviter.sendMessage(build(prefix, comp("This player already has a pending invite.", NamedTextColor.RED)));
				return;
			}
			else if (inviter.equals(invite.inviter)) {
				inviter.sendMessage(build(prefix, comp("You have already sent an invite. Please wait before sending another.", NamedTextColor.RED)));
				return;
			}
		}
		TeamInvite invite = new TeamInvite(invitee, inviter);
		invites.add(invite);
		invitee.sendMessage(build(prefix, comp("You have been invited to join ", NamedTextColor.YELLOW), teamToJoin.getTeamNameBuilder(), comp(". You have 60 seconds to accept using /team accept.", NamedTextColor.YELLOW)));
		inviter.sendMessage(build(prefix, comp("You have invited ", NamedTextColor.YELLOW), comp(invitee.getName()), comp(" to join your team. They have 60 seconds to accept.", NamedTextColor.YELLOW)));
		AtomicInteger duration = new AtomicInteger(0);
		new BukkitRunnable() {
			@Override
			public void run() {
				int time = duration.intValue();
				if (!invites.contains(invite))
					cancel();
				else if (time >= 1200) {
					cancel();
					invites.remove(invite);
					if (inviter.getPlayer() instanceof Player player)
						player.sendMessage(build(prefix, comp("Your invite has expired.", NamedTextColor.RED)));
				}
				duration.set(time + 1);
			}
		}.runTaskTimer(SMP_Expansion.getPlugin(), 1L, 1L);
	}

	/**
	 * Attempts to accept an invite
	 * @param invitee player to be invited
	 */
	public static void acceptInvite(@NotNull Player invitee) {
		for (TeamInvite invite: invites)
			if (invitee.equals(invite.invitee)) {
				invites.remove(invite);
				invite.inviteTeam.addMember(invitee);
				mapOfPlayerTeams.put(invitee, invite.inviteTeam);
				invitee.sendMessage(build(prefix, comp("You have successfully joined ", NamedTextColor.YELLOW), invite.inviteTeam.getTeamNameBuilder()));
				invitee.updateCommands();
				return;
			}
		invitee.sendMessage(build(prefix, comp("You don't have a pending invite to accept.", NamedTextColor.RED)));
	}

	/**
	 * Attempts to decline an invite
	 * @param invitee player to be invited
	 */
	public static void declineInvite(@NotNull Player invitee) {
		for (TeamInvite invite: invites)
			if (invitee.equals(invite.invitee)) {
				invites.remove(invite);
				invitee.sendMessage(build(prefix, comp("You have declined the invitation to join ", NamedTextColor.YELLOW), invite.inviteTeam.getTeamNameBuilder()));
				return;
			}
		invitee.sendMessage(build(prefix, comp("You don't have a pending invite to decline.", NamedTextColor.RED)));
	}

	/**
	 * Attempts to kick a player from the team<br>
	 * Only the leader can kick players<br>
	 * @param kicked player to be kicked
	 * @param kicker player that kicked
	 */
	public static void kickPlayer(@NotNull OfflinePlayer kicked, @NotNull Player kicker) {
		PlayerTeam teamToLeave = getPlayerTeam(kicker);
		PlayerTeam teamOfKicked = getPlayerTeam(kicked);
		if (kicked.equals(kicker)) {
			kicker.sendMessage(build(prefix, comp("You cannot kick yourself.", NamedTextColor.RED)));
			return;
		}
		if (teamToLeave == null) {
			kicker.sendMessage(build(prefix, comp("You need to be in a team to kick players.", NamedTextColor.RED)));
			return;
		}
		if (!teamToLeave.getLeader().equals(kicker)) {
			kicker.sendMessage(build(prefix, comp("Only the leader may kick players.", NamedTextColor.RED)));
			return;
		}
		if (teamOfKicked == null || !teamOfKicked.equals(teamToLeave)) {
			kicker.sendMessage(build(prefix, comp("This player is not on your team.", NamedTextColor.RED)));
			return;
		}
		teamToLeave.removeMember(kicked);
		mapOfPlayerTeams.remove(kicked);
		kicker.sendMessage(build(prefix, comp("You have kicked ", NamedTextColor.YELLOW), comp(kicked.getName()), comp(" from your team.", NamedTextColor.YELLOW)));
		if (kicked instanceof Player player) {
			player.sendMessage(build(prefix, comp("You have been kicked from ", NamedTextColor.YELLOW), teamToLeave.getTeamNameBuilder(), comp(" by ", NamedTextColor.YELLOW), comp(kicker.getName() + ".")));
			player.updateCommands();
		}
	}

	/**
	 * Attempts to removes a player from their team<br>
	 * The player cannot be the leader of their team
	 * @param player player to remove from their team
	 */
	public static void leaveTeam(@NotNull Player player) {
		PlayerTeam team = TeamManager.getPlayerTeam(player);
		if (team == null) {
			player.sendMessage(build(prefix, comp("You are not currently in a team.", NamedTextColor.RED)));
			return;
		}
		if (player.equals(team.getLeader())) {
			player.sendMessage(build(prefix, comp("You cannot leave this team. Use /team delete instead to delete this team.", NamedTextColor.RED)));
			return;
		}
		team.removeMember(player);
		mapOfPlayerTeams.remove(player);
		player.sendMessage(build(prefix, comp("You have left ", NamedTextColor.YELLOW), team.getTeamNameBuilder()));
		player.updateCommands();

	}

	/**
	 * Attempts to delete a player's team and its file<br>
	 * The player must be the leader of their team and no other members must be in the team
	 * @param player player who wants to delete their team
	 */
	public static void deleteTeam(@NotNull Player player) {
		PlayerTeam team = TeamManager.getPlayerTeam(player);
		if (team == null) {
			player.sendMessage(build(prefix, comp("You are not currently in a team.", NamedTextColor.RED)));
			return;
		}
		if (!player.equals(team.getLeader())) {
			player.sendMessage(build(prefix, comp("You cannot delete this team. Use /team leave instead to leave this team.", NamedTextColor.RED)));
			return;
		}
		else if (team.getMembers().size() > 1) {
			player.sendMessage(build(prefix, comp("Remove all members before deleting this team.", NamedTextColor.RED)));
			return;
		}
		player.sendMessage(build(prefix, comp("Deleted team ", NamedTextColor.YELLOW), team.getTeamNameBuilder(), comp(". You are no longer in a team.", NamedTextColor.YELLOW)));
		mapOfLeaders.remove(team.getLeader());
		for (OfflinePlayer member: team.getMembers()) {
			mapOfPlayerTeams.remove(member);
			if (member.getPlayer() instanceof Player online)
				online.updateCommands();
		}
		team.deleteTeam();
	}

	/**
	 * Gets the player's name with team as a text component builder
	 * @param player player to get name of
	 * @return text component builder
	 */
	public static TextComponent.Builder getPlayerName(@NotNull OfflinePlayer player) {
		PlayerTeam team = getPlayerTeam(player);
		if (team != null)
			return unite(comp("[", NamedTextColor.WHITE), team.getTeamNameBuilder(), comp("] ", NamedTextColor.WHITE), comp(player.getName(), NamedTextColor.WHITE));
		else
			return comp(player.getName());
	}

	/**
	 * Represents team invites sent to players
	 * @author YellowStoneTorch
	 * @version 0.1.0-ALPHA
	 */
	private static class TeamInvite {
		private final OfflinePlayer invitee;
		private final OfflinePlayer inviter;
		private final PlayerTeam inviteTeam;

		/**
		 * Creates a team invite
		 * @param invitee player to be invited
		 * @param inviter player that invited
		 */
		TeamInvite(OfflinePlayer invitee, OfflinePlayer inviter) {
			this.invitee = invitee;
			this.inviter = inviter;
			inviteTeam = TeamManager.getPlayerTeam(inviter);
		}
	}
}

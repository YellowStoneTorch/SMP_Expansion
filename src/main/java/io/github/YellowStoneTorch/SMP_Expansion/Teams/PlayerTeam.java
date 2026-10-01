package io.github.YellowStoneTorch.SMP_Expansion.Teams;

import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion.GSON;
import static io.github.YellowStoneTorch.SMP_Expansion.Teams.TeamManager.teamFiles;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.build;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.comp;

/**
 * Represents a player team and its options
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class PlayerTeam {
	static final Scoreboard scoreboard;

	static {
		scoreboard = Bukkit.getScoreboardManager()
				.getMainScoreboard();
	}

	private final TextComponent.Builder teamName;
	private final OfflinePlayer leader;
	private final ArrayList<OfflinePlayer> members;
	private final Team minecraftTeam;
	private final File teamFile;
	private boolean canPvp;

	/**
	 * Creates a PlayerTeam according to the specified and default options, creating and saving a file
	 * @param teamName team name
	 * @param leader player that created the team
	 * @throws IOException if an I/O exception occurs
	 */
	PlayerTeam(@NotNull TextComponent.Builder teamName, @NotNull OfflinePlayer leader) throws IOException {
		this.teamName = teamName;
		this.leader = leader;
		members = new ArrayList<>(Collections.singletonList(leader));
		if (scoreboard.getTeam(teamName.content()) instanceof Team team)
			minecraftTeam = team;
		else {
			minecraftTeam = scoreboard.registerNewTeam(teamName.content());
			minecraftTeam.setAllowFriendlyFire(canPvp);
			minecraftTeam.prefix(build(comp("["), teamName, comp("] ")));
		}
		minecraftTeam.addPlayer(leader);
		teamFile = new File(teamFiles, teamName.content() + ".json");
		if (!teamFile.isFile())
			//noinspection ResultOfMethodCallIgnored
			teamFile.createNewFile();
		canPvp = false;
		saveToFile();
	}

	/**
	 * Creates a PlayerTeam from raw form
	 * @param rawTeam raw player team
	 */
	private PlayerTeam(@NotNull RawPlayerTeam rawTeam) {
		teamName = comp(rawTeam.name, TextColor.color(rawTeam.color), TextDecoration.BOLD);
		leader = Bukkit.getOfflinePlayer(UUID.fromString(rawTeam.leader));
		members = new ArrayList<>();
		for (String playerId: rawTeam.members)
			members.add(Bukkit.getOfflinePlayer(UUID.fromString(playerId)));
		if (scoreboard.getTeam(teamName.content()) instanceof Team team)
			minecraftTeam = team;
		else {
			minecraftTeam = scoreboard.registerNewTeam(teamName.content());
			minecraftTeam.setAllowFriendlyFire(canPvp);
			minecraftTeam.prefix(build(comp("["), teamName, comp("] ")));
		}
		minecraftTeam.addPlayer(leader);
		teamFile = new File(teamFiles, teamName.content() + ".json");
		canPvp = rawTeam.canPvP;
	}

	/**
	 * Gets a player team from its file
	 * @param file file to get player team from
	 * @return player team from file
	 * @throws IOException if an I/O exception occurs
	 */
	@NotNull
	static PlayerTeam fromFile(@NotNull File file) throws IOException {
		try (FileReader reader = new FileReader(file)) {
			return new PlayerTeam(GSON.fromJson(reader, RawPlayerTeam.class));
		}
	}

	/**
	 * Gets the team name as a text component
	 * @return team name
	 */
	public TextComponent getTeamName() {
		return teamName.build();
	}

	/**
	 * Gets the team name as a text component builder
	 * @return team name
	 */
	public TextComponent.Builder getTeamNameBuilder() {
		return teamName;
	}

	/**
	 * Gets the player leading this team
	 * @return team leader
	 */
	public OfflinePlayer getLeader() {
		return leader;
	}

	/**
	 * Gets a list of the players on this team
	 * @return team members
	 */
	public ArrayList<OfflinePlayer> getMembers() {
		return new ArrayList<>(members);
	}

	/**
	 * Gets the minecraft team associated with this team
	 * @return minecraft team
	 */
	public Team getMinecraftTeam() {
		return minecraftTeam;
	}

	/**
	 * Gets whether teammates in this team can PvP
	 * @return whether pvp is allowed between teammates
	 */
	public boolean canPvP() {
		return canPvp;
	}

	/**
	 * Saves this player team to a file
	 * @throws IOException if an I/O exception occurs
	 */
	void saveToFile() throws IOException {
		try (FileWriter writer = new FileWriter(teamFile)) {
			GSON.toJson(new RawPlayerTeam(this), writer);
		}
	}

	/**
	 * Adds a new player to the team
	 * @param player player to add
	 */
	void addMember(@NotNull OfflinePlayer player) {
		members.add(player);
		minecraftTeam.addPlayer(player);
		try {
			saveToFile();
		}
		catch (IOException e) {
			SMP_Expansion.saveIOException(e);
		}
	}

	/**
	 * Removes a player from the team if they're not the leader
	 * @param player player to remove
	 */
	void removeMember(@NotNull OfflinePlayer player) {
		if (!player.equals(leader)) {
			members.remove(player);
			minecraftTeam.removePlayer(player);
			try {
				saveToFile();
			}
			catch (IOException e) {
				SMP_Expansion.saveIOException(e);
			}
		}
	}

	/**
	 * Sets whether teammates in this team can PvP
	 * @param canPvP whether pvp is allowed between teammates
	 */
	void setPvP(boolean canPvP) {
		this.canPvp = canPvP;
		minecraftTeam.setAllowFriendlyFire(canPvP);
		try {
			saveToFile();
		}
		catch (IOException e) {
			SMP_Expansion.saveIOException(e);
		}
	}

	/**
	 * Deletes the team file and associated Minecraft team
	 */
	void deleteTeam() {
		//noinspection ResultOfMethodCallIgnored
		teamFile.delete();
		minecraftTeam.unregister();
	}

	/**
	 * Converts player team into raw data for storage
	 * @author YellowStoneTorch
	 * @version 0.1.0-ALPHA
	 */
	private static class RawPlayerTeam {
		private final String name;
		private final int color;
		private final String leader;
		private final String[] members;
		private final boolean canPvP;

		/**
		 * Converts a normal player team's information into a raw form for storage
		 * @param team team to convert
		 */
		RawPlayerTeam(@NotNull PlayerTeam team) {
			TextComponent teamName = team.getTeamName();
			name = teamName.content();
			TextColor teamColor = teamName.color();
			color = teamColor == null ? NamedTextColor.WHITE.value() : teamColor.value();
			leader = team.getLeader()
					.getUniqueId()
					.toString();
			ArrayList<String> membersList = new ArrayList<>();
			for (OfflinePlayer player: team.getMembers())
				membersList.add(player.getUniqueId()
						.toString());
			members = membersList.toArray(String[]::new);
			canPvP = team.canPvp;
		}
	}
}

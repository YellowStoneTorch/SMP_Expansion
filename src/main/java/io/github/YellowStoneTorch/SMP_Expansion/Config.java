package io.github.YellowStoneTorch.SMP_Expansion;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Loads the plugin config and holds config settings
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Config {
	private static String forcedDifficulty = "none";
	private static int forcedPvP = 0;
	private static boolean allowCurrency = true;
	private static boolean allowTeams = true;
	private static boolean allowClaims = true;
	private static boolean allowKeepInventory = true;
	private static boolean enableHomeCommand = true;
	private static boolean enablePvPCooldown = false;

	/**
	 * Loads the plugin config from its file, creating a new config with default settings if it's not found
	 */
	static void loadConfig() {
		Yaml yaml = new Yaml();
		File configFile = new File(Bukkit.getWorldContainer(), "plugins/SMP_Expansion/config.yml");
		if (configFile.isFile()) {
			try (FileReader reader = new FileReader(configFile)) {
				Map<String, Object> config = yaml.load(reader);
				if (config != null) {
					if (config.get("Forced Difficulty") instanceof String difficulty)
						forcedDifficulty = switch (difficulty) {
							case "Easy" ->
									"easy";
							case "Hard" ->
									"hard";
							case "Extra Hard" ->
									"extraHard";
							default ->
									"normal";
						};
					else {
						SMP_Expansion.getPlugin().getLogger().warning("Config is missing setting for: Forced Difficulty, using default");
						forcedDifficulty = "normal";
					}
					if (config.get("Forced PvP") instanceof String pvp)
						forcedPvP = switch (pvp) {
							case "On" ->
									1;
							case "Off" ->
									-1;
							default ->
									0;
						};
					else {
						SMP_Expansion.getPlugin().getLogger().warning("Config is missing setting for: Forced PvP, using default");
						forcedPvP = 0;
					}
					if (config.get("Allow Currency") instanceof Boolean currency)
						allowCurrency = currency;
					else {
						SMP_Expansion.getPlugin().getLogger().warning("Config is missing setting for: Allow Currency, using default");
						allowCurrency = true;
					}
					if (config.get("Allow Teams") instanceof Boolean teams)
						allowTeams = teams;
					else {
						SMP_Expansion.getPlugin().getLogger().warning("Config is missing setting for: Allow Teams, using default");
						allowTeams = true;
					}
					if (config.get("Allow Claims") instanceof Boolean claims)
						allowClaims = claims;
					else {
						SMP_Expansion.getPlugin().getLogger().warning("Config is missing setting for: Allow Claims, using default");
						allowClaims = true;
					}
					if (config.get("Allow Keep Inventory") instanceof Boolean keepInventory)
						allowKeepInventory = keepInventory;
					else {
						SMP_Expansion.getPlugin().getLogger().warning("Config is missing setting for: Allow Keep Inventory, using default");
						allowKeepInventory = true;
					}
					if (config.get("Enable /home Command") instanceof Boolean home)
						enableHomeCommand = home;
					else {
						SMP_Expansion.getPlugin().getLogger().warning("Config is missing setting for: Enable /home Command, using default");
						enableHomeCommand = true;
					}
					if (config.get("Enable Combat Tagging") instanceof Boolean tagging)
						enablePvPCooldown = tagging;
					else {
						SMP_Expansion.getPlugin().getLogger().warning("Config is missing setting for: Enable Combat Tagging, using default");
						enablePvPCooldown = true;
					}
				}
				else {
					reader.close();
					//noinspection ResultOfMethodCallIgnored
					configFile.delete();
					SMP_Expansion.getPlugin().getLogger().warning("Config is empty or malformed, rebuilding");
					createConfig(configFile);
				}
			}
			catch (Exception e) {
				SMP_Expansion.getPlugin().getLogger().warning("Unable to load config.yml, using defaults");
				setDefaults();
			}
		}
		else {
			createConfig(configFile);
		}
	}

	/**
	 * Sets the default config settings if the config file is not available
	 */
	private static void setDefaults() {
		forcedDifficulty = "none";
		forcedPvP = 0;
		allowCurrency = true;
		allowTeams = true;
		allowClaims = true;
		allowKeepInventory = true;
		enableHomeCommand = true;
		enablePvPCooldown = false;
	}

	/**
	 * Creates the config file if it is not present
	 * @param configFile config file
	 */
	private static void createConfig(@NotNull File configFile) {
		try {
			//noinspection ResultOfMethodCallIgnored
			configFile.createNewFile();
		}
		catch (IOException e) {
			SMP_Expansion.getPlugin().getLogger().warning("Unable to create config.yml, using defaults");
			setDefaults();
			return;
		}
		try (FileWriter writer = new FileWriter(configFile)) {
			writer.write("""
					# SMP Expansion Plugin Config
					# These settings give you more control over multiplayer features to customize your server experience.
					# To read more about these settings: https://github.com/YellowStoneTorch/SMP_Expansion/wiki/Multiplayer-Features
					
					# Force all players to use a specific personal difficulty
					# Options: "None", "Easy", "Normal", "Hard", "Extra Hard"
					Forced Difficulty: "None"
					
					# Force all players to either opt-in or opt-out of PvP
					# Options: "None", "On", "Off"
					Forced PvP: "None"
					
					# Allow players to deposit their diamonds for currency
					# This does not prevent players from using banknotes or withdrawing their diamonds
					# Options: True, False
					Allow Currency: True
					
					# Allow players to create new teams
					# This does not affect teams that already have been created
					# Options: True, False
					Allow Teams: True
					
					# Allow players to claim new chunks
					# This does not affect chunks that already have been claimed
					# Options: True, False
					Allow Claims: True
					
					# Allow players to enable Keep Inventory for themselves
					# Options: True, False
					Allow Keep Inventory: True
					
					# Allow players to use the /home command, teleporting players to their spawn point
					# This is recommended if claims are enabled to prevent players from getting stuck in no-build zones
					# Options: True, False
					Enable /home Command: True
					
					# Enables combat tagging, which prevents players who engage in combat from disabling PvP or teleporting using commands
					# Options: True, False
					Enable Combat Tagging: False
					""");
			writer.close();
			SMP_Expansion.getPlugin().getLogger().warning("Creating config file with defaults");
			setDefaults();
		}
		catch (Exception e) {
			SMP_Expansion.getPlugin().getLogger().warning("Unable to create config.yml, using defaults");
			setDefaults();
		}
	}

	/**
	 * Gets the forced personal difficulty, if it exists
	 * @return forced difficulty, or "none" if not set
	 */
	public static String getForcedDifficulty() {
		return forcedDifficulty;
	}

	/**
	 * Gets if PvP is forced on or off
	 * @return 0 if PvP is not set, 1 if PvP is forced on, -1 if PvP is forced off
	 */
	public static int getForcedPvP() {
		return forcedPvP;
	}

	/**
	 * Gets whether players are allowed to exchange diamond for currency and banknotes
	 * @return whether players can exchange diamonds for currency
	 */
	public static boolean getAllowCurrency() {
		return allowCurrency;
	}

	/**
	 * Gets whether players are allowed to create new teams
	 * @return whether players can create new teams
	 */
	public static boolean getAllowTeams() {
		return allowTeams;
	}

	/**
	 * Gets whether players are allowed to create new claims
	 * @return whether players can create new claims
	 */
	public static boolean getAllowClaims() {
		return allowClaims;
	}

	/**
	 * Gets whether players are allowed to enable keep inventory
	 * @return whether players can enable keep inventory
	 */
	public static boolean getAllowKeepInventory() {
		return allowKeepInventory;
	}

	/**
	 * Gets whether players are allowed to use the /home command
	 * @return whether /home can be used
	 */
	public static boolean getEnableHomeCommand() {
		return enableHomeCommand;
	}

	/**
	 * Gets whether combat tagging is enabled
	 * @return whether combat tagging is enabled
	 */
	public static boolean getEnablePvPCooldown() {
		return enablePvPCooldown;
	}
}


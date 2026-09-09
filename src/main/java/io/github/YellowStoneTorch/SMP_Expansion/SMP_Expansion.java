package io.github.YellowStoneTorch.SMP_Expansion;

import com.google.gson.Gson;
import io.github.YellowStoneTorch.SMP_Expansion.Claims.ClaimManager;
import io.github.YellowStoneTorch.SMP_Expansion.Commands.CommandManager;
import io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus.InventoryMenu;
import io.github.YellowStoneTorch.SMP_Expansion.Teams.TeamManager;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Level;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.pvpCooldown;
import static net.kyori.adventure.text.Component.text;

/**
 * Plugin main class
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public final class SMP_Expansion extends JavaPlugin {
	public static final Gson GSON;
	private static SMP_Expansion plugin;
	private static boolean isShuttingDown;

	static {
		GSON = new Gson();
	}

	/**
	 * Gets whether the plugin is being disabled
	 * @return whether the plugin is being disabled
	 */
	public static boolean isShuttingDown() {
		return isShuttingDown;
	}

	/**
	 * Gets this plugin instance for use
	 * @return this plugin instance
	 */
	public static SMP_Expansion getPlugin() {
		return plugin;
	}

	/**
	 * Alerts the server to an I/O Exception when saving to a file
	 * @param e I/O Exception
	 */
	public static void saveIOException(@NotNull IOException e) {
		Bukkit.getServer()
				.broadcast(text("An error occurred when saving data to a file.", NamedTextColor.RED));
		Bukkit.getServer()
				.broadcast(text("View server console for more information.", NamedTextColor.RED));
		getPlugin().getLogger()
				.log(Level.SEVERE, "An IO exception occurred", e);
	}

	/**
	 * Alerts the server to an I/O Exception when loading from a file
	 * @param e I/O Exception
	 */
	public static void loadIOException(@NotNull IOException e) {
		Bukkit.getServer()
				.broadcast(text("An error occurred when loading data from a file.", NamedTextColor.RED));
		Bukkit.getServer()
				.broadcast(text("View server console for more information.", NamedTextColor.RED));
		getPlugin().getLogger()
				.log(Level.SEVERE, "An IO exception occurred", e);
	}

	@Override
	public void onEnable() {
		plugin = this;
		isShuttingDown = false;
		getServer().getPluginManager()
				.registerEvents(new EventListener(), plugin);
		CommandManager.registerCommands(plugin);
		Config.loadConfig();
		TeamManager.loadTeams();
		ClaimManager.loadClaims();
	}

	@Override
	public void onDisable() {
		isShuttingDown = true;
		for (Player player: getServer().getOnlinePlayers()) {
			// Ensure that players receive input items back when server shuts down
			if (player.getOpenInventory()
					.getTopInventory()
					.getHolder() instanceof InventoryMenu menu)
				menu.onClose();
			player.getPersistentDataContainer()
					.remove(pvpCooldown);
		}
	}
}

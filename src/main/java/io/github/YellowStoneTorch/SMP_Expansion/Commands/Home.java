package io.github.YellowStoneTorch.SMP_Expansion.Commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.YellowStoneTorch.SMP_Expansion.Config;
import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.pvpCooldown;
import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.teleportCountdown;
import static net.kyori.adventure.text.Component.text;

/**
 * Command for returning to the player's spawn point
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Home {
	/**
	 * Get command logic to register command
	 * @return command logic
	 */
	public static LiteralCommandNode<CommandSourceStack> createCommand() {
		return Commands.literal("home")
				.executes(context -> {
					if (context.getSource().getSender() instanceof Player player) {
						if (Config.getEnableHomeCommand()) {
							PersistentDataContainer playerData = player.getPersistentDataContainer();
							if (playerData.getOrDefault(pvpCooldown, PersistentDataType.INTEGER, 0) == 0) {
								playerData.set(teleportCountdown, PersistentDataType.INTEGER, 1);
								player.sendMessage(text("You will be teleported back to your spawn point.", NamedTextColor.YELLOW));
								player.sendMessage(text("Please stand still for 5 seconds.", NamedTextColor.YELLOW));
								player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
								new BukkitRunnable() {
									@Override
									public void run() {
										if (!player.isConnected()) {
											cancel();
											return;
										}
										int cooldown = player.getPersistentDataContainer().getOrDefault(teleportCountdown, PersistentDataType.INTEGER, 0);
										if (cooldown == 0) {
											player.sendMessage(text("Your teleport was cancelled.", NamedTextColor.RED));
											cancel();
											return;
										}
										else if (cooldown == 100) {
											Location spawnPoint = player.getRespawnLocation();
											player.teleport(spawnPoint == null ? Bukkit.getServer().getRespawnWorld().getSpawnLocation() : spawnPoint);
											player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.MASTER, 1.0f, 1.0f);
											cancel();
											return;
										}
										else if (cooldown % 20 == 0) {
											player.sendMessage(text("Please stand still for " + ((100 - cooldown) / 20) + " seconds.", NamedTextColor.YELLOW));
											player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
										}
										playerData.set(teleportCountdown, PersistentDataType.INTEGER, cooldown + 1);

									}
								}.runTaskTimer(SMP_Expansion.getPlugin(), 1L, 1L);
							}
						}
						else
							player.sendMessage(text("This command has been disabled by the server.", NamedTextColor.RED));
					}
					return 1;
				})
				.build();
	}

}

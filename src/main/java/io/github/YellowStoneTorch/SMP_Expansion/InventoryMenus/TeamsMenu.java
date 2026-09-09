package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

/**
 * Menu for managing teams with other players
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class TeamsMenu extends InventoryMenu {
	/**
	 * Creates inventory menu
	 * @param player player to display menu to
	 */
	public TeamsMenu(@NotNull Player player) {
		super(36, text("Team Settings"));
		displayIcons(player);
		inventory.setItem(28, back);
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player)event.getWhoClicked();
		switch (event.getRawSlot()) {
			case 28 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new MainMenu().getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
		}
		displayIcons(player);
	}

	/**
	 * Sets the icons to display based on keep inventory settings
	 * @param player player to check pvp settings for
	 */
	private void displayIcons(@NotNull Player player) {
		PersistentDataContainer playerData = player.getPersistentDataContainer();
	}


}

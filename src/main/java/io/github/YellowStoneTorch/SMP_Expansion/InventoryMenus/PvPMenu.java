package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.Config;
import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.pvp;
import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.pvpCooldown;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;
import static net.kyori.adventure.text.Component.text;

/**
 * Menu for setting PvP settings
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class PvPMenu extends InventoryMenu {
	/**
	 * Creates inventory menu
	 * @param player player to display menu to
	 */
	public PvPMenu(@NotNull Player player) {
		super(36, text("PvP Settings"));
		displayIcons(player);
		inventory.setItem(28, back);
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player)event.getWhoClicked();
		switch (event.getRawSlot()) {
			case 13 -> {
				if (Config.getForcedPvP() == 0) {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					PersistentDataContainer playerData = player.getPersistentDataContainer();
					if (playerData.getOrDefault(pvpCooldown, PersistentDataType.INTEGER, 0) == 0)
						playerData.set(pvp, PersistentDataType.BOOLEAN, !playerData.getOrDefault(pvp, PersistentDataType.BOOLEAN, false));
				}
			}
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
		inventory.setItem(13, getPvPIcon(playerData.getOrDefault(pvp, PersistentDataType.BOOLEAN, false)));
	}

	/**
	 * Gets the icon to display for toggling PvP
	 * @param hasPvP whether pvp is currently enabled
	 * @return pvp icon
	 */
	@NotNull
	private ItemStack getPvPIcon(boolean hasPvP) {
		ItemStack icon = ItemStack.of(Material.IRON_SWORD, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("PvP", NamedTextColor.YELLOW, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Choose whether other players can attack you and vice versa.", NamedTextColor.GRAY));
			lore.add(textNI("Note: You cannot change this setting when combat tagged.", NamedTextColor.GRAY));
			if (Config.getForcedPvP() == 0)
				lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			else
				lore.add(textNI("PvP has been set by the server", NamedTextColor.GRAY));
			if (hasPvP)
				lore.add(textNI("PvP is On", NamedTextColor.GREEN));
			else
				lore.add(textNI("PvP is Off", NamedTextColor.GREEN));
			data.lore(lore);
			data.setEnchantmentGlintOverride(hasPvP);
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		return icon;
	}
}

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

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.*;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;
import static net.kyori.adventure.text.Component.text;

/**
 * Menu for setting keep inventory settings
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class KeepInventoryMenu extends InventoryMenu {
	/**
	 * Creates inventory menu
	 * @param player player to display menu to
	 */
	public KeepInventoryMenu(@NotNull Player player) {
		super(36, text("Keep Inventory Settings"));
		displayIcons(player);
		inventory.setItem(28, back);
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player)event.getWhoClicked();
		PersistentDataContainer playerData = player.getPersistentDataContainer();
		switch (event.getRawSlot()) {
			case 10 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				if (Config.getAllowKeepInventory())
					playerData.set(keepInvArmor, PersistentDataType.BOOLEAN, !playerData.getOrDefault(keepInvArmor, PersistentDataType.BOOLEAN, true));
			}
			case 12 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				if (Config.getAllowKeepInventory())
					playerData.set(keepInvHotbar, PersistentDataType.BOOLEAN, !playerData.getOrDefault(keepInvHotbar, PersistentDataType.BOOLEAN, true));
			}
			case 14 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				if (Config.getAllowKeepInventory())
					playerData.set(keepInvInventory, PersistentDataType.BOOLEAN, !playerData.getOrDefault(keepInvInventory, PersistentDataType.BOOLEAN, true));
			}
			case 16 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				if (Config.getAllowKeepInventory())
					playerData.set(keepInvXP, PersistentDataType.BOOLEAN, !playerData.getOrDefault(keepInvXP, PersistentDataType.BOOLEAN, true));
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
	 * @param player player to check keep inventory settings for
	 */
	private void displayIcons(@NotNull Player player) {
		PersistentDataContainer playerData = player.getPersistentDataContainer();
		inventory.setItem(10, getArmorIcon(playerData.getOrDefault(keepInvArmor, PersistentDataType.BOOLEAN, true)));
		inventory.setItem(12, getHotbarIcon(playerData.getOrDefault(keepInvHotbar, PersistentDataType.BOOLEAN, true)));
		inventory.setItem(14, getInventoryIcon(playerData.getOrDefault(keepInvInventory, PersistentDataType.BOOLEAN, true)));
		inventory.setItem(16, getXPIcon(playerData.getOrDefault(keepInvXP, PersistentDataType.BOOLEAN, true)));
	}

	/**
	 * Gets the icon to display for armor keep inventory
	 * @param isEnabled whether armor keep inventory is enabled
	 */
	@NotNull
	private ItemStack getArmorIcon(boolean isEnabled) {
		ItemStack icon = ItemStack.of(Material.IRON_CHESTPLATE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Armor Slots", NamedTextColor.YELLOW, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Preserves worn armor upon death.", NamedTextColor.GRAY));
			if (Config.getAllowKeepInventory())
				lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			else
				lore.add(textNI("Keep Inventory has been disabled by the server", NamedTextColor.GRAY));
			if (isEnabled)
				lore.add(textNI("Enabled!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Disabled!", NamedTextColor.RED));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isEnabled);
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for hotbar keep inventory
	 * @param isEnabled whether hotbar keep inventory is enabled
	 */
	@NotNull
	private ItemStack getHotbarIcon(boolean isEnabled) {
		ItemStack icon = ItemStack.of(Material.IRON_SWORD, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Hotbar Slots", NamedTextColor.YELLOW, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Preserves all items in the hotbar and the offhand upon death.", NamedTextColor.GRAY));
			if (Config.getAllowKeepInventory())
				lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			else
				lore.add(textNI("Keep Inventory has been disabled by the server", NamedTextColor.GRAY));
			if (isEnabled)
				lore.add(textNI("Enabled!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Disabled!", NamedTextColor.RED));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isEnabled);
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for player inventory keep inventory
	 * @param isEnabled whether player inventory keep inventory is enabled
	 */
	@NotNull
	private ItemStack getInventoryIcon(boolean isEnabled) {
		ItemStack icon = ItemStack.of(Material.BARREL, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Inventory Slots", NamedTextColor.YELLOW, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Preserves all items in the rest of the inventory slots upon death.", NamedTextColor.GRAY));
			if (Config.getAllowKeepInventory())
				lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			else
				lore.add(textNI("Keep Inventory has been disabled by the server", NamedTextColor.GRAY));
			if (isEnabled)
				lore.add(textNI("Enabled!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Disabled!", NamedTextColor.RED));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isEnabled);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for player inventory keep inventory
	 * @param isEnabled whether player inventory keep inventory is enabled
	 */
	@NotNull
	private ItemStack getXPIcon(boolean isEnabled) {
		ItemStack icon = ItemStack.of(Material.EXPERIENCE_BOTTLE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Experience", NamedTextColor.YELLOW, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Preserves XP upon death.", NamedTextColor.GRAY));
			if (Config.getAllowKeepInventory())
				lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			else
				lore.add(textNI("Keep Inventory has been disabled by the server", NamedTextColor.GRAY));
			if (isEnabled)
				lore.add(textNI("Enabled!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Disabled!", NamedTextColor.RED));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isEnabled);
		});
		return icon;
	}
}

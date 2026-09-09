package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.Config;
import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.difficulty;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;
import static net.kyori.adventure.text.Component.text;

/**
 * Menu for setting difficulty settings
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class DifficultyMenu extends InventoryMenu {
	/**
	 * Creates inventory menu
	 * @param player player to display menu to
	 */
	public DifficultyMenu(@NotNull Player player) {
		super(36, text("Difficulty"));
		displayIcons(player);
		inventory.setItem(28, back);
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player)event.getWhoClicked();
		switch (event.getRawSlot()) {
			case 10 -> {
				if (Config.getForcedDifficulty().equals("none")) {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					player.getPersistentDataContainer().set(difficulty, PersistentDataType.STRING, "easy");
				}
			}
			case 12 -> {
				if (Config.getForcedDifficulty().equals("none")) {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					player.getPersistentDataContainer().set(difficulty, PersistentDataType.STRING, "normal");
				}
			}
			case 14 -> {
				if (Config.getForcedDifficulty().equals("none")) {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					player.getPersistentDataContainer().set(difficulty, PersistentDataType.STRING, "hard");
				}
			}
			case 16 -> {
				if (Config.getForcedDifficulty().equals("none")) {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					player.getPersistentDataContainer().set(difficulty, PersistentDataType.STRING, "extraHard");
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
	 * Sets the icons to display based on current difficulty
	 * @param player player to check difficulty for
	 */
	private void displayIcons(@NotNull Player player) {
		String dif = player.getPersistentDataContainer().getOrDefault(difficulty, PersistentDataType.STRING, "normal");
		inventory.setItem(10, getEasyIcon(dif.equals("easy")));
		inventory.setItem(12, getNormalIcon(dif.equals("normal")));
		inventory.setItem(14, getHardIcon(dif.equals("hard")));
		inventory.setItem(16, getExtraHardIcon(dif.equals("extraHard")));
	}

	/**
	 * Gets the icon to display for the easy difficulty
	 * @param isSelected whether easy difficulty is selected
	 * @return easy icon
	 */
	@NotNull
	private ItemStack getEasyIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.WOODEN_SWORD, 1);
		icon.editMeta(data -> {
			data.displayName(textNI("Easy", NamedTextColor.GREEN, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Take 30% less damage from monsters", NamedTextColor.GRAY));
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else if (!Config.getForcedDifficulty().equals("none"))
				lore.add(textNI("The difficulty has been set by the server", NamedTextColor.GRAY));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for the normal difficulty
	 * @param isSelected whether normal difficulty is selected
	 * @return normal icon
	 */
	@NotNull
	private ItemStack getNormalIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.IRON_SWORD, 1);
		icon.editMeta(data -> {
			data.displayName(textNI("Normal", NamedTextColor.GOLD, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("No damage modifiers", NamedTextColor.GRAY));
			lore.add(textNI("Note: Server difficulty is " + Bukkit.getServer().getRespawnWorld().getDifficulty(), NamedTextColor.GRAY));
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else if (!Config.getForcedDifficulty().equals("none"))
				lore.add(textNI("The difficulty has been set by the server", NamedTextColor.GRAY));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for the hard difficulty
	 * @param isSelected whether hard difficulty is selected
	 * @return hard icon
	 */
	@NotNull
	private ItemStack getHardIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.DIAMOND_SWORD, 1);
		icon.editMeta(data -> {
			data.displayName(textNI("Hard", NamedTextColor.RED, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Take 30% more damage from monsters", NamedTextColor.GRAY));
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else if (!Config.getForcedDifficulty().equals("none"))
				lore.add(textNI("The difficulty has been set by the server", NamedTextColor.GRAY));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for the extra hard difficulty
	 * @param isSelected whether extra hard difficulty is selected
	 * @return extra hard icon
	 */
	@NotNull
	private ItemStack getExtraHardIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.NETHERITE_SWORD, 1);
		icon.editMeta(data -> {
			data.displayName(textNI("Extra Hard", NamedTextColor.DARK_RED, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Take 60% more damage from monsters", NamedTextColor.GRAY));
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else if (!Config.getForcedDifficulty().equals("none"))
				lore.add(textNI("The difficulty has been set by the server", NamedTextColor.GRAY));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		return icon;
	}
}

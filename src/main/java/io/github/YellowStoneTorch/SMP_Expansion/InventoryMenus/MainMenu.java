package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;
import static net.kyori.adventure.text.Component.text;

/**
 * Main menu for all options for the server
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class MainMenu extends InventoryMenu {
	private static final ItemStack difficultyIcon;
	private static final ItemStack keepInventoryIcon;
	private static final ItemStack pvpIcon;
	private static final ItemStack bankIcon;
	private static final ItemStack claimsIcon;

	static {
		difficultyIcon = ItemStack.of(Material.SKELETON_SKULL, 1);
		difficultyIcon.editMeta(data ->
				data.customName(textNI("Set Game Difficulty", NamedTextColor.YELLOW, TextDecoration.BOLD)));
		keepInventoryIcon = ItemStack.of(Material.BARREL, 1);
		keepInventoryIcon.editMeta(data ->
				data.customName(textNI("Set Keep Inventory Settings", NamedTextColor.YELLOW, TextDecoration.BOLD)));
		pvpIcon = ItemStack.of(Material.IRON_SWORD, 1);
		pvpIcon.editMeta(data ->
		{
			data.customName(textNI("Set PvP Settings", NamedTextColor.YELLOW, TextDecoration.BOLD));
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		bankIcon = ItemStack.of(Material.BOOK, 1);
		bankIcon.editMeta(data ->
				data.customName(textNI("View Balance", NamedTextColor.YELLOW, TextDecoration.BOLD)));
		claimsIcon = ItemStack.of(Material.RED_BANNER, 1);
		claimsIcon.editMeta(data ->
				data.customName(textNI("View Claims", NamedTextColor.YELLOW, TextDecoration.BOLD)));
	}

	/**
	 * Creates inventory menu
	 */
	public MainMenu() {
		super(54, text("Settings"));
		inventory.setItem(10, difficultyIcon);
		inventory.setItem(11, keepInventoryIcon);
		inventory.setItem(12, pvpIcon);
		inventory.setItem(13, bankIcon);
		inventory.setItem(14, claimsIcon);
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player)event.getWhoClicked();
		switch (event.getRawSlot()) {
			case 10 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new DifficultyMenu(player).getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
			case 11 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new KeepInventoryMenu(player).getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
			case 12 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new PvPMenu(player).getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
			case 13 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new BankMenu(player).getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
			case 14 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new ClaimsMenu(player).getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());

			}
		}
	}
}

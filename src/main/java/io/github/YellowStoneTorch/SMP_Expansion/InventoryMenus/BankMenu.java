package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.Config;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.ItemManager;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.money;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.*;
import static net.kyori.adventure.text.Component.text;

/**
 * Menu for managing bank balance
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class BankMenu extends InventoryMenu {
	private static final ItemStack depositBanknotesIcon;
	private static final ItemStack withdrawDiamondsIcon;
	private static final ItemStack withdrawBanknotesIcon;

	static {
		depositBanknotesIcon = new ItemStack(Material.BOOK, 1);
		depositBanknotesIcon.editMeta(data -> {
			data.customName(textNI("Deposit Banknotes", NamedTextColor.GREEN, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Click to deposit all banknotes", NamedTextColor.YELLOW));
			data.lore(lore);
		});
		withdrawDiamondsIcon = new ItemStack(Material.DIAMOND_BLOCK, 1);
		withdrawDiamondsIcon.editMeta(data -> {
			data.customName(textNI("Withdraw Diamonds", NamedTextColor.AQUA, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Left Click to withdraw 1 diamond", NamedTextColor.YELLOW));
			lore.add(textNI("Right Click to withdraw 64 diamonds", NamedTextColor.YELLOW));
			lore.add(textNI("Shift Left Click to withdraw 1 diamond blick", NamedTextColor.YELLOW));
			lore.add(textNI("Shift Right Click to withdraw 64 diamond blocks", NamedTextColor.YELLOW));
			data.lore(lore);
		});
		withdrawBanknotesIcon = new ItemStack(Material.WRITABLE_BOOK, 1);
		withdrawBanknotesIcon.editMeta(data -> {
			data.customName(textNI("Withdraw Banknotes", NamedTextColor.GREEN, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Left Click to withdraw $1", NamedTextColor.YELLOW));
			lore.add(textNI("Right Click to withdraw $5", NamedTextColor.YELLOW));
			lore.add(textNI("Shift Left Click to withdraw $10", NamedTextColor.YELLOW));
			lore.add(textNI("Shift Right Click to withdraw $25", NamedTextColor.YELLOW));
			lore.add(textNI("Use /withdraw [amount] to withdraw a custom amount", NamedTextColor.GRAY));
			data.lore(lore);
		});
	}

	/**
	 * Creates inventory menu
	 * @param player player to display menu to
	 */
	public BankMenu(@NotNull Player player) {
		super(54, text("Bank"));
		PersistentDataContainer playerData = player.getPersistentDataContainer();
		inventory.setItem(13, getBalanceIcon(playerData.getOrDefault(money, PersistentDataType.INTEGER, 0)));
		inventory.setItem(28, getDepositDiamondsIcon());
		inventory.setItem(30, depositBanknotesIcon);
		inventory.setItem(32, withdrawDiamondsIcon);
		inventory.setItem(34, withdrawBanknotesIcon);
		inventory.setItem(46, back);
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player)event.getWhoClicked();
		PersistentDataContainer playerData = player.getPersistentDataContainer();
		switch (event.getRawSlot()) {
			case 28 -> {
				if (Config.getAllowCurrency()) {
					PlayerInventory playerInventory = player.getInventory();
					switch (event.getClick()) {
						case LEFT -> {
							if (playerInventory.contains(Material.DIAMOND, 1)) {
								player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
								for (ItemStack item: playerInventory.getContents())
									if (item != null && item.getType() == Material.DIAMOND) {
										item.add(-1);
										break;
									}
								playerData.set(money, PersistentDataType.INTEGER, 100 + playerData.getOrDefault(money, PersistentDataType.INTEGER, 0));
							}
						}
						case RIGHT -> {
							if (playerInventory.contains(Material.DIAMOND, 64)) {
								player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
								int itemsToRemove = 64;
								for (ItemStack item: playerInventory.getContents())
									if (item != null && item.getType() == Material.DIAMOND) {
										if (item.getAmount() >= itemsToRemove) {
											item.add(-itemsToRemove);
											break;
										}
										else {
											itemsToRemove -= item.getAmount();
											item.add(-item.getAmount());
										}
									}
								playerData.set(money, PersistentDataType.INTEGER, 6400 + playerData.getOrDefault(money, PersistentDataType.INTEGER, 0));
							}
						}
						case SHIFT_LEFT -> {
							if (playerInventory.contains(Material.DIAMOND_BLOCK, 1)) {
								player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
								for (ItemStack item: playerInventory.getContents())
									if (item != null && item.getType() == Material.DIAMOND_BLOCK) {
										item.add(-1);
										break;
									}
								playerData.set(money, PersistentDataType.INTEGER, 900 + playerData.getOrDefault(money, PersistentDataType.INTEGER, 0));
							}
						}
						case SHIFT_RIGHT -> {
							int diamonds = 0;
							for (ItemStack item: playerInventory.getContents()) {
								if (item != null) {
									switch (item.getType()) {
										case DIAMOND -> {
											diamonds += item.getAmount();
											item.add(-item.getAmount());
										}
										case DIAMOND_BLOCK -> {
											diamonds += 9 * item.getAmount();
											item.add(-item.getAmount());
										}
									}
								}
							}
							if (diamonds > 0) {
								player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
								playerData.set(money, PersistentDataType.INTEGER, (100 * diamonds) + playerData.getOrDefault(money, PersistentDataType.INTEGER, 0));
							}
						}
					}
				}
			}
			case 30 -> {
				PlayerInventory playerInventory = player.getInventory();
				int totalValue = 0;
				for (ItemStack item: playerInventory.getContents())
					if (item != null) {
						int value = item.getPersistentDataContainer().getOrDefault(money, PersistentDataType.INTEGER, 0);
						if (value > 0) {
							totalValue += value * item.getAmount();
							item.add(-item.getAmount());
						}
					}
				if (totalValue > 0) {
					player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
					playerData.set(money, PersistentDataType.INTEGER, totalValue + playerData.getOrDefault(money, PersistentDataType.INTEGER, 0));
				}
			}
			case 32 -> {
				int currentBal = playerData.getOrDefault(money, PersistentDataType.INTEGER, 0);
				switch (event.getClick()) {
					case LEFT -> {
						if (currentBal >= 100) {
							player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
							playerData.set(money, PersistentDataType.INTEGER, currentBal - 100);
							player.give(ItemStack.of(Material.DIAMOND, 1));
							new BukkitRunnable() {
								@Override
								public void run() {
									player.updateInventory();
								}
							}.runTask(SMP_Expansion.getPlugin());
						}
					}
					case RIGHT -> {
						if (currentBal >= 6400) {
							player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
							playerData.set(money, PersistentDataType.INTEGER, currentBal - 6400);
							player.give(ItemStack.of(Material.DIAMOND, 64));
							new BukkitRunnable() {
								@Override
								public void run() {
									player.updateInventory();
								}
							}.runTask(SMP_Expansion.getPlugin());
						}
					}
					case SHIFT_LEFT -> {
						if (currentBal >= 900) {
							player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
							playerData.set(money, PersistentDataType.INTEGER, currentBal - 900);
							player.give(ItemStack.of(Material.DIAMOND_BLOCK, 1));
							new BukkitRunnable() {
								@Override
								public void run() {
									player.updateInventory();
								}
							}.runTask(SMP_Expansion.getPlugin());
						}
					}
					case SHIFT_RIGHT -> {
						if (currentBal >= 57600) {
							player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
							playerData.set(money, PersistentDataType.INTEGER, currentBal - 57600);
							player.give(ItemStack.of(Material.DIAMOND_BLOCK, 64));
							new BukkitRunnable() {
								@Override
								public void run() {
									player.updateInventory();
								}
							}.runTask(SMP_Expansion.getPlugin());
						}
					}
				}
			}
			case 34 -> {
				int currentBal = playerData.getOrDefault(money, PersistentDataType.INTEGER, 0);
				switch (event.getClick()) {
					case LEFT -> {
						if (currentBal >= 1) {
							player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
							playerData.set(money, PersistentDataType.INTEGER, currentBal - 1);
							player.give(ItemManager.getBanknote(1));
							new BukkitRunnable() {
								@Override
								public void run() {
									player.updateInventory();
								}
							}.runTask(SMP_Expansion.getPlugin());
						}
					}
					case RIGHT -> {
						if (currentBal >= 5) {
							player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
							playerData.set(money, PersistentDataType.INTEGER, currentBal - 5);
							player.give(ItemManager.getBanknote(5));
							new BukkitRunnable() {
								@Override
								public void run() {
									player.updateInventory();
								}
							}.runTask(SMP_Expansion.getPlugin());
						}
					}
					case SHIFT_LEFT -> {
						if (currentBal >= 10) {
							player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
							playerData.set(money, PersistentDataType.INTEGER, currentBal - 10);
							player.give(ItemManager.getBanknote(10));
							new BukkitRunnable() {
								@Override
								public void run() {
									player.updateInventory();
								}
							}.runTask(SMP_Expansion.getPlugin());
						}
					}
					case SHIFT_RIGHT -> {
						if (currentBal >= 25) {
							player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.UI, 1, 1);
							playerData.set(money, PersistentDataType.INTEGER, currentBal - 25);
							player.give(ItemManager.getBanknote(25));
							new BukkitRunnable() {
								@Override
								public void run() {
									player.updateInventory();
								}
							}.runTask(SMP_Expansion.getPlugin());
						}
					}
				}
			}
			case 46 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new MainMenu().getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
		}
		inventory.setItem(13, getBalanceIcon(playerData.getOrDefault(money, PersistentDataType.INTEGER, 0)));
	}

	/**
	 * Gets the icon to display for depositing diamonds
	 * @return deposit icon
	 */
	@NotNull
	private ItemStack getDepositDiamondsIcon() {
		ItemStack icon = new ItemStack(Material.DIAMOND, 1);
		icon.editMeta(data -> {
			data.customName(textNI("Deposit Diamonds", NamedTextColor.AQUA, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (Config.getAllowCurrency()) {
				lore.add(textNI("Left Click to deposit 1 diamond", NamedTextColor.YELLOW));
				lore.add(textNI("Right Click to deposit 64 diamonds", NamedTextColor.YELLOW));
				lore.add(textNI("Shift Left Click to deposit 1 diamond block", NamedTextColor.YELLOW));
				lore.add(textNI("Shift Right Click to deposit all diamonds", NamedTextColor.YELLOW));
			}
			else
				lore.add(textNI("Depositing has been disabled by the server", NamedTextColor.GRAY));
			data.lore(lore);
		});
		return icon;
	}

	/**
	 * Gets the icon to display the player's current balance
	 * @param balance current balance to display
	 * @return balance icon
	 */
	@NotNull
	private ItemStack getBalanceIcon(int balance) {
		ItemStack icon = ItemStack.of(Material.BOOK, 1);
		icon.editMeta(data -> {
			data.customName(build(compNI("Current Balance: ", NamedTextColor.YELLOW, TextDecoration.BOLD), compNI("$" + balance, NamedTextColor.GREEN)));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("1 diamond is exchanged for 100 dollars.", NamedTextColor.GRAY));
			lore.add(textNI("Dollars are used for player trading or in-game purchases.", NamedTextColor.GRAY));
			lore.add(textNI("You may withdraw your diamonds at any time.", NamedTextColor.GRAY));
			if (!Config.getAllowCurrency())
				lore.add(textNI("Note: Depositing diamonds for banknotes has been disabled by the server.", NamedTextColor.YELLOW));
			data.lore(lore);
		});
		return icon;
	}
}

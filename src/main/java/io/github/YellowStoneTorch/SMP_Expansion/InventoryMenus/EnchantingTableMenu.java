package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EnchantmentType;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EquipEnchantment;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.EquipmentType;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.ItemManager;
import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.*;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.*;
import static net.kyori.adventure.text.Component.text;
import static org.bukkit.Material.*;

/**
 * Enchanting table interface
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class EnchantingTableMenu extends InventoryMenu {
	private static final ItemStack infoIcon;
	private static final ItemStack equipmentIcon;
	private static final ItemStack materialsIcon;

	static {
		ArrayList<TextComponent> lore = new ArrayList<>();
		infoIcon = ItemStack.of(Material.ENCHANTING_TABLE, 1);
		infoIcon.editMeta(data -> {
			data.customName(textNI("Enchanting Table", NamedTextColor.YELLOW, TextDecoration.BOLD));
			lore.add(Component.empty());
			lore.add(textNI("Here, you can add individual enchantments to", NamedTextColor.GRAY));
			lore.add(textNI("your equipment. To unlock more, remove enchantments", NamedTextColor.GRAY));
			lore.add(textNI("on a grindstone to gain enchantment XP.", NamedTextColor.GRAY));
			lore.add(textNI("Note: Bookshelves have no effect anymore.", NamedTextColor.YELLOW));
			data.lore(lore);
		});
		equipmentIcon = ItemStack.of(DIAMOND_SWORD, 1);
		equipmentIcon.editMeta(data -> {
			data.customName(textNI("Place Equipment Here", NamedTextColor.YELLOW));
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		materialsIcon = ItemStack.of(LAPIS_LAZULI, 1);
		materialsIcon.editMeta(data -> data.customName(textNI("Place Enchanting Materials Here", NamedTextColor.YELLOW)));
	}

	/**
	 * Creates inventory menu
	 */
	public EnchantingTableMenu() {
		super(54, text("Enchanting Table"));
		inventory.setItem(13, infoIcon);
		inventory.setItem(28, null);
		inventory.setItem(29, null);
		inventory.setItem(37, equipmentIcon);
		inventory.setItem(38, materialsIcon);
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		int slot = event.getRawSlot();
		if (slot < 54 && slot != 28 && slot != 29) {
			event.setCancelled(true);
			Inventory menu = event.getClickedInventory();
			if (menu == null)
				return;
			ItemStack input = menu.getItem(28);
			ItemStack materials = menu.getItem(29);
			ItemStack enchantmentBook = menu.getItem(slot);
			if (input == null || enchantmentBook == null)
				return;
			ArrayList<EquipEnchantment> enchantments = EquipEnchantment.getEnchantments(enchantmentBook);
			EquipEnchantment enchantment;
			int level = enchantmentBook.getItemMeta().getPersistentDataContainer().getOrDefault(itemLevel, PersistentDataType.INTEGER, 0);
			if (!enchantments.isEmpty())
				enchantment = enchantments.getFirst();
			else
				return;
			switch (enchantmentBook.getType()) {
				case BOOK -> {
					if (event.getClick().isShiftClick()) {
						// Apply enchantment to item
						if (materials == null)
							return;
						if (enchantment.type.rarity == EnchantmentType.Rarity.GOLDEN) {
							if (player.getLevel() >= (8 * enchantment.level) && materials.getType() == ENCHANTED_GOLDEN_APPLE) {
								// Player has required materials and levels
								enchantment.apply(input);
								player.setLevel(player.getLevel() - enchantment.level);
								materials.add(-1);
								player.playSound(player, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1, 1);
							}
							else
								return;
						}
						else {
							if (player.getLevel() >= (8 * enchantment.level) && materials.getType() == LAPIS_LAZULI && materials.getAmount() >= enchantment.level) {
								// Player has required materials and levels
								enchantment.apply(input);
								player.setLevel(player.getLevel() - enchantment.level);
								materials.add(-enchantment.level);
								player.playSound(player, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1, 1);
							}
							else
								return;
						}
					}
					else {
						// Cycle enchantment
						player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
						int playerMaxLevel = EquipEnchantment.getLevelFromXP(enchantment.type, player.getPersistentDataContainer().getOrDefault(enchantment.type.key, PersistentDataType.INTEGER, 0));
						if (playerMaxLevel > 1) {
							if (enchantment.level == 1)
								menu.setItem(slot, getEnchantmentIcon(enchantment.type, playerMaxLevel, player));
							else
								menu.setItem(slot, getEnchantmentIcon(enchantment.type, enchantment.level - 1, player));
							return;
						}

					}
				}
				case ENCHANTED_BOOK -> {
					if (event.getClick().isShiftClick()) {
						if (level > 0) {
							// Enchanting a book
							if (materials == null)
								return;
							if (player.getLevel() >= (8 * level) && materials.getType() == LAPIS_LAZULI && materials.getAmount() >= level) {
								if (input.getAmount() > 1) {
									player.give(input.add(-1));
									new BukkitRunnable() {
										@Override
										public void run() {
											player.updateInventory();
										}
									}.runTask(SMP_Expansion.getPlugin());
								}
								ArrayList<EquipEnchantment> newEnchantments = new ArrayList<>();
								int numEnchants = getNumEnchants(level);
								for (int i = 0; i < numEnchants; i++) {
									EnchantmentType type = getEnchantType(getRarity(level));
									newEnchantments.add(EquipEnchantment.getEnchantment(type, getEnchantLevel(level, type.maxLevel)));
								}
								inventory.setItem(28, ItemManager.getEnchantedBook(newEnchantments));
								player.setLevel(player.getLevel() - level);
								inventory.setItem(29, materials.add(-level));
								player.playSound(player, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1, 1);
							}
						}
						else {
							// Removing an enchantment
							enchantment.remove(input);
							player.playSound(player, Sound.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1, 1);
						}
					}
				}
			}
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				setEnchantments(player);
			}
		}.runTask(SMP_Expansion.getPlugin());
	}

	@Override
	public void onClose() {
		List<HumanEntity> viewers = inventory.getViewers();
		if (!viewers.isEmpty() && viewers.getFirst() instanceof Player player) {
			for (int i = 28; i < 30; i++) {
				if (inventory.getItem(i) instanceof ItemStack item)
					player.give(item);
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					player.updateInventory();
				}
			}.runTask(SMP_Expansion.getPlugin());
		}
	}

	@Override
	public void onDrag(@NotNull InventoryDragEvent event) {
		Player player = (Player)event.getWhoClicked();
		new BukkitRunnable() {
			@Override
			public void run() {
				setEnchantments(player);
			}
		}.runTask(SMP_Expansion.getPlugin());
	}

	/**
	 * Displays the corresponding enchantment icons for the item placed in the input
	 * @param player player to display enchantment icons to
	 */
	private void setEnchantments(@NotNull Player player) {
		ItemStack input = inventory.getItem(28);
		if (input != null) {
			switch (EquipmentType.getType(input.getPersistentDataContainer().getOrDefault(equipmentType, PersistentDataType.STRING, ""))) {
				case SWORD, GOLDEN_SWORD -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.SHARPNESS, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.SMITE, player));
					inventory.setItem(34, getEnchantmentIcon(EnchantmentType.BANE_OF_ARTHROPODS, player));
					inventory.setItem(40, getEnchantmentIcon(EnchantmentType.FIRE_ASPECT, player));
					inventory.setItem(41, getEnchantmentIcon(EnchantmentType.KNOCKBACK, player));
					inventory.setItem(42, getEnchantmentIcon(EnchantmentType.LOOTING, player));
					inventory.setItem(43, getEnchantmentIcon(EnchantmentType.SWEEPING_EDGE, player));
				}
				case SPEAR, GOLDEN_SPEAR -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.SHARPNESS, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.SMITE, player));
					inventory.setItem(34, getEnchantmentIcon(EnchantmentType.BANE_OF_ARTHROPODS, player));
					inventory.setItem(40, getEnchantmentIcon(EnchantmentType.FIRE_ASPECT, player));
					inventory.setItem(41, getEnchantmentIcon(EnchantmentType.KNOCKBACK, player));
					inventory.setItem(42, getEnchantmentIcon(EnchantmentType.LOOTING, player));
					inventory.setItem(43, getEnchantmentIcon(EnchantmentType.LUNGE, player));
				}
				case AXE, GOLDEN_AXE -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.SHARPNESS, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.SMITE, player));
					inventory.setItem(34, getEnchantmentIcon(EnchantmentType.BANE_OF_ARTHROPODS, player));
					inventory.setItem(40, getEnchantmentIcon(EnchantmentType.EFFICIENCY, player));
					inventory.setItem(41, getEnchantmentIcon(EnchantmentType.FORTUNE, player));
					inventory.setItem(42, getEnchantmentIcon(EnchantmentType.SILK_TOUCH, player));
				}
				case PICKAXE, SHOVEL, HOE -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.EFFICIENCY, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.FORTUNE, player));
					inventory.setItem(34, getEnchantmentIcon(EnchantmentType.SILK_TOUCH, player));
				}
				case HELMET, GOLDEN_HELMET -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.REINFORCEMENT, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.PROTECTION, player));
					inventory.setItem(34, getEnchantmentIcon(EnchantmentType.BLAST_PROTECTION, player));
					inventory.setItem(40, getEnchantmentIcon(EnchantmentType.FIRE_PROTECTION, player));
					inventory.setItem(41, getEnchantmentIcon(EnchantmentType.AQUA_AFFINITY, player));
					inventory.setItem(42, getEnchantmentIcon(EnchantmentType.RESPIRATION, player));
					inventory.setItem(43, getEnchantmentIcon(EnchantmentType.THORNS, player));
				}
				case CHESTPLATE, GOLDEN_CHESTPLATE, LEGGINGS, GOLDEN_LEGGINGS -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.REINFORCEMENT, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.PROTECTION, player));
					inventory.setItem(34, getEnchantmentIcon(EnchantmentType.BLAST_PROTECTION, player));
					inventory.setItem(40, getEnchantmentIcon(EnchantmentType.FIRE_PROTECTION, player));
					inventory.setItem(41, getEnchantmentIcon(EnchantmentType.THORNS, player));
				}
				case BOOTS, GOLDEN_BOOTS -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.REINFORCEMENT, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.PROTECTION, player));
					inventory.setItem(34, getEnchantmentIcon(EnchantmentType.BLAST_PROTECTION, player));
					inventory.setItem(40, getEnchantmentIcon(EnchantmentType.FIRE_PROTECTION, player));
					inventory.setItem(41, getEnchantmentIcon(EnchantmentType.DEPTH_STRIDER, player));
					inventory.setItem(42, getEnchantmentIcon(EnchantmentType.FEATHER_FALLING, player));
					inventory.setItem(43, getEnchantmentIcon(EnchantmentType.THORNS, player));
				}
				case BOW -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.POWER, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.FLAME, player));
					inventory.setItem(34, getEnchantmentIcon(EnchantmentType.INFINITY, player));
					inventory.setItem(40, getEnchantmentIcon(EnchantmentType.PUNCH, player));
				}
				case CROSSBOW -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.POWER, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.MULTISHOT, player));
					inventory.setItem(34, getEnchantmentIcon(EnchantmentType.PIERCING, player));
					inventory.setItem(40, getEnchantmentIcon(EnchantmentType.QUICK_CHARGE, player));
				}
				case TRIDENT -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.SHARPNESS, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.SMITE, player));
					inventory.setItem(34, getEnchantmentIcon(EnchantmentType.BANE_OF_ARTHROPODS, player));
					inventory.setItem(40, getEnchantmentIcon(EnchantmentType.IMPALING, player));
					inventory.setItem(41, getEnchantmentIcon(EnchantmentType.CHANNELING, player));
					inventory.setItem(42, getEnchantmentIcon(EnchantmentType.LOYALTY, player));
					inventory.setItem(43, getEnchantmentIcon(EnchantmentType.RIPTIDE, player));
				}
				case MACE -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.SHARPNESS, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.SMITE, player));
					inventory.setItem(34, getEnchantmentIcon(EnchantmentType.BANE_OF_ARTHROPODS, player));
					inventory.setItem(40, getEnchantmentIcon(EnchantmentType.BREACH, player));
					inventory.setItem(41, getEnchantmentIcon(EnchantmentType.DENSITY, player));
				}
				case FISHING_ROD -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.LUCK_OF_THE_SEA, player));
					inventory.setItem(33, getEnchantmentIcon(EnchantmentType.LURE, player));
				}
				case SHEARS -> {
					inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
					inventory.setItem(32, getEnchantmentIcon(EnchantmentType.EFFICIENCY, player));
				}
				case TOOL ->
						inventory.setItem(31, getEnchantmentIcon(EnchantmentType.UNBREAKING, player));
				default -> {
					if (input.getType() == Material.BOOK) {
						inventory.setItem(31, getBookEnchantmentIcon(1));
						inventory.setItem(32, getBookEnchantmentIcon(2));
						inventory.setItem(33, getBookEnchantmentIcon(3));
						inventory.setItem(34, getBookEnchantmentIcon(4));
						inventory.setItem(40, getBookEnchantmentIcon(5));
					}
					else {
						for (int i = 31; i < 35; i++)
							inventory.setItem(i, empty);
						for (int i = 40; i < 44; i++)
							inventory.setItem(i, empty);
						for (int i = 49; i < 53; i++)
							inventory.setItem(i, empty);
					}
				}
			}
		}
		else {
			for (int i = 31; i < 35; i++)
				inventory.setItem(i, empty);
			for (int i = 40; i < 44; i++)
				inventory.setItem(i, empty);
			for (int i = 49; i < 53; i++)
				inventory.setItem(i, empty);
		}
	}

	/**
	 * Gets the enchantment icon to display when displaying enchantment menu
	 * @param type enchantment to display
	 * @param player player to display icon to
	 * @return enchantment icon
	 */
	@NotNull
	private ItemStack getEnchantmentIcon(@NotNull EnchantmentType type, @NotNull Player player) {
		int xp = player.getPersistentDataContainer().getOrDefault(type.key, PersistentDataType.INTEGER, 0);
		return getEnchantmentIcon(type, getEnchantmentLevel(type, EquipEnchantment.getLevelFromXP(type, xp)), player);
	}

	/**
	 * Gets the enchantment icon to display when displaying enchantment menu
	 * @param type enchantment to display
	 * @param level force the enchantment to display at this level
	 * @param player player to display icon to
	 * @return enchantment icon
	 */
	@NotNull
	private ItemStack getEnchantmentIcon(@NotNull EnchantmentType type, int level, @NotNull Player player) {
		ItemStack input = inventory.getItem(28);
		PersistentDataContainer playerData = player.getPersistentDataContainer();
		int xp = playerData.getOrDefault(type.key, PersistentDataType.INTEGER, 0);
		int playerMaxLevel = EquipEnchantment.getLevelFromXP(type, xp);
		EquipEnchantment enchantment = EquipEnchantment.getEnchantment(type, level);
		ItemStack output;
		ArrayList<EquipEnchantment> enchantments;
		if (input == null) {
			output = ItemStack.of(Material.BOOK, 1);
			enchantments = null;
		}
		else {
			enchantments = EquipEnchantment.getEnchantments(input);
			if (EquipEnchantment.getEnchantment(type, enchantments) == null)
				output = ItemStack.of(Material.BOOK, 1);
			else
				output = ItemStack.of(ENCHANTED_BOOK, 1);
		}
		output.editMeta(data -> {
			if (enchantment == null)
				data.customName(textNI("Enchantment Locked", NamedTextColor.YELLOW));
			else {
				data.customName(EnchantmentType.getTitle(type, enchantment.level));
				ArrayList<TextComponent> lore = new ArrayList<>(enchantment.getDescription());
				lore.add(Component.empty());
				if (playerMaxLevel == type.maxLevel)
					lore.add(textNI("Maximum level unlocked!", NamedTextColor.GOLD));
				else {
					int xpThreshold = 0;
					for (int i = 2; i <= 32; i *= 2) {
						if (xp < i) {
							xpThreshold = i;
							i = 33;
						}
					}
					lore.add(build(compNI(Integer.toString(xp), NamedTextColor.YELLOW), compNI("/", NamedTextColor.WHITE), compNI(Integer.toString(xpThreshold), NamedTextColor.GREEN), compNI(" XP until next level", NamedTextColor.WHITE)));
				}
				lore.add(Component.empty());
				if (output.getType() == Material.ENCHANTED_BOOK) {
					lore.add(textNI("Shift Click to Remove Enchantment from Item", NamedTextColor.YELLOW));
					lore.add(textNI("Note: No refund or enchantment XP will be awarded", NamedTextColor.GRAY));
					data.getPersistentDataContainer().set(type.key, PersistentDataType.INTEGER, enchantment.level);
				}
				else {
					if (playerMaxLevel > 1)
						lore.add(textNI("Click to Cycle Enchantment Level", NamedTextColor.YELLOW));
					if (enchantments == null || enchantment.isCompatible(enchantments)) {
						lore.add(textNI("Shift Click to Add Enchantment to Item", NamedTextColor.YELLOW));
						lore.add(textNI("Requires: " + (8 * enchantment.level) + " Levels", NamedTextColor.GRAY));
						lore.add(textNI("Costs: " + enchantment.level + " Levels and " + enchantment.level + " Lapis Lazuli", NamedTextColor.GRAY));
						data.getPersistentDataContainer().set(type.key, PersistentDataType.INTEGER, enchantment.level);
					}
					else
						lore.add(textNI("Remove Incompatible Enchantments from Item Before Applying", NamedTextColor.RED));
				}
				data.lore(lore);
			}
		});
		return output;
	}

	/**
	 * Gets the enchantment icon to display when enchanting books with random enchantments
	 * @param level level of enchantment
	 * @return enchantment icon
	 */
	@NotNull
	private ItemStack getBookEnchantmentIcon(int level) {
		ItemStack input = ItemStack.of(ENCHANTED_BOOK, 1);
		input.editMeta(data -> {
			data.getPersistentDataContainer().set(itemLevel, PersistentDataType.INTEGER, level);
			data.getPersistentDataContainer().set(protection, PersistentDataType.INTEGER, 1);
			data.customName(textNI("Level " + level + " Experiment", NamedTextColor.YELLOW));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Shift Click to Enchant This Book", NamedTextColor.YELLOW));
			lore.add(textNI("Requires: " + (8 * level) + " Levels", NamedTextColor.GRAY));
			lore.add(textNI("Costs: " + level + " Levels and " + level + " Lapis Lazuli", NamedTextColor.GRAY));
			lore.add(Component.empty());
			data.lore(lore);
		});
		return input;
	}

	/**
	 * Gets the appropriate enchantment level to display, depending on whether the enchantment is currently on the input item
	 * @param type type of enchantment
	 * @param playerMaxLevel player's maximum level for this enchantment
	 * @return enchantment level to display
	 */
	private int getEnchantmentLevel(EnchantmentType type, int playerMaxLevel) {
		ItemStack input = inventory.getItem(28);
		EquipEnchantment currentEnchantment;
		if (input == null)
			currentEnchantment = null;
		else
			currentEnchantment = EquipEnchantment.getEnchantment(type, EquipEnchantment.getEnchantments(input));
		if (currentEnchantment == null)
			return playerMaxLevel;
		return currentEnchantment.level;
	}

	/**
	 * Gets the number of enchantments to put on a book
	 * @param level level selected (1-5)
	 * @return number of enchantments
	 */
	private int getNumEnchants(int level) {
		double chance = Math.random();
		return switch (level) {
			case 1 ->
					1;
			case 2 -> {
				if (chance < 0.25)
					yield 2;
				else
					yield 1;
			}
			case 3 -> {
				if (chance < 0.05)
					yield 3;
				else if (chance < 0.4)
					yield 2;
				else
					yield 1;
			}
			case 4 -> {
				if (chance < 0.1)
					yield 3;
				else if (chance < 0.5)
					yield 2;
				else
					yield 1;
			}
			case 5 -> {
				if (chance < 0.05)
					yield 4;
				else if (chance < 0.2)
					yield 3;
				else if (chance < 0.7)
					yield 2;
				else
					yield 1;
			}
			default ->
					0;
		};
	}

	/**
	 * Gets the rarity of enchantment to put on a book
	 * @param level level selected (1-5)
	 * @return enchantment rarity
	 */
	private EnchantmentType.Rarity getRarity(int level) {
		double chance = Math.random();
		return switch (level) {
			case 1 -> {
				if (chance < 0.05)
					yield EnchantmentType.Rarity.RARE;
				else
					yield EnchantmentType.Rarity.COMMON;
			}
			case 2 -> {
				if (chance < 0.2)
					yield EnchantmentType.Rarity.RARE;
				else
					yield EnchantmentType.Rarity.COMMON;
			}
			case 3 -> {
				if (chance < 0.35)
					yield EnchantmentType.Rarity.RARE;
				else
					yield EnchantmentType.Rarity.COMMON;
			}
			case 4 -> {
				if (chance < 0.5)
					yield EnchantmentType.Rarity.RARE;
				else
					yield EnchantmentType.Rarity.COMMON;
			}
			case 5 -> {
				if (chance < 0.6)
					yield EnchantmentType.Rarity.RARE;
				else
					yield EnchantmentType.Rarity.COMMON;
			}
			default ->
					EnchantmentType.Rarity.COMMON;
		};
	}

	/**
	 * Gets a random enchantment type of the given rarity
	 * @param rarity rarity of enchantment
	 * @return random enchantment type
	 */
	@NotNull
	private EnchantmentType getEnchantType(EnchantmentType.Rarity rarity) {
		EnchantmentType[] types = EnchantmentType.values();
		while (true) {
			EnchantmentType type = types[(int)(Math.random() * types.length)];
			if (type.rarity == rarity)
				return type;
		}
	}

	/**
	 * Gets the level of enchantment to put on a book
	 * @param level level selected (1-5)
	 * @param maxLevel max level of enchantment
	 * @return enchantment level
	 */
	private int getEnchantLevel(int level, int maxLevel) {
		if (maxLevel == 1)
			return 1;
		double chance = Math.random() * 0.4;
		return switch (level) {
			case 1 ->
					(int)(chance * maxLevel) + 1;
			case 2 ->
					(int)((chance + 0.15) * maxLevel) + 1;
			case 3 ->
					(int)((chance + 0.3) * maxLevel) + 1;
			case 4 ->
					(int)((chance + 0.45) * maxLevel) + 1;
			case 5 ->
					(int)((chance + 0.6) * maxLevel) + 1;
			default ->
					1;
		};
	}
}

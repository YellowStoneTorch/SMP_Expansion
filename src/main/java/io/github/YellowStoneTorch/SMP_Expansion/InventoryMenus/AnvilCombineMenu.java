package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EquipEnchantment;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.EquipmentType;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.ItemManager;
import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.isCustom;
import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.renamed;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;
import static net.kyori.adventure.text.Component.text;

/**
 * Menu specifically for combining two items
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class AnvilCombineMenu extends InventoryMenu {
	/**
	 * Result of attempted anvil operation
	 * @author YellowStoneTorch
	 * @version 0.1.0-ALPHA
	 */
	private enum Result {
		INVALID_RECIPE,
		NOT_ENOUGH_XP,
		NOT_ENOUGH_DURABILITY,
		NO_OPERATION,
		SUCCESS
	}

	private static final ItemStack inputIcon;
	private static final ItemStack sacrificeIcon;

	static {
		inputIcon = ItemStack.of(Material.DIAMOND_SWORD, 1);
		inputIcon.editMeta(data -> {
			data.customName(textNI("Place First Item Here", NamedTextColor.YELLOW));
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		sacrificeIcon = ItemStack.of(Material.DIAMOND, 1);
		sacrificeIcon.editMeta(data -> data.customName(textNI("Place Second Item Here", NamedTextColor.YELLOW)));
	}

	private final Block block;
	private int sacrificeItemsConsumed;
	private int xpCost;
	private Result result;

	/**
	 * Creates inventory menu
	 * @param block anvil that this menu is tied to
	 */
	AnvilCombineMenu(Block block) {
		super(54, text("Combine Items"));
		this.block = block;
		sacrificeItemsConsumed = 0;
		xpCost = 0;
		result = Result.INVALID_RECIPE;
		inventory.setItem(29, null);
		inventory.setItem(31, null);
		inventory.setItem(33, outputSlot);
		inventory.setItem(38, inputIcon);
		inventory.setItem(40, sacrificeIcon);
		inventory.setItem(42, getOutputIcon());
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		int slot = event.getRawSlot();
		boolean isOutputting;
		if (slot == 33) {
			ItemStack output = inventory.getItem(33);
			if (output != null && !output.equals(outputSlot)) {
				// Check if an anvil still exists at the current location
				Block currentBlock = block.getLocation().getBlock();
				switch (currentBlock.getType()) {
					case ANVIL, CHIPPED_ANVIL, DAMAGED_ANVIL -> {
						if (xpCost > 0 && player.getLevel() >= xpCost) {
							inventory.setItem(29, null);
							if (inventory.getItem(31) instanceof ItemStack sacrifice)
								inventory.setItem(31, sacrifice.add(-sacrificeItemsConsumed));
							player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1);
							player.setLevel(player.getLevel() - xpCost);
							isOutputting = true;
							// Anvil is damaged
							if (Math.random() < 0.12) {
								switch (currentBlock.getType()) {
									case ANVIL -> {
										BlockFace direction = null;
										if (currentBlock.getBlockData() instanceof Directional directional)
											direction = directional.getFacing();
										currentBlock.setType(Material.CHIPPED_ANVIL);
										if (direction != null && currentBlock.getBlockData() instanceof Directional directional) {
											directional.setFacing(direction);
											currentBlock.setBlockData(directional);
										}
									}
									case CHIPPED_ANVIL -> {
										BlockFace direction;
										if (currentBlock.getBlockData() instanceof Directional directional)
											direction = directional.getFacing();
										else
											direction = null;
										currentBlock.setType(Material.DAMAGED_ANVIL);
										if (direction != null && currentBlock.getBlockData() instanceof Directional directional) {
											directional.setFacing(direction);
											currentBlock.setBlockData(directional);
										}
									}
									case DAMAGED_ANVIL -> {
										currentBlock.breakNaturally(ItemStack.of(Material.STICK, 1), true, false, true);
										currentBlock.getWorld().playSound(currentBlock.getLocation(), Sound.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS, 1, 1);
										new BukkitRunnable() {
											@Override
											public void run() {
												inventory.close();
											}
										}.runTask(SMP_Expansion.getPlugin());
									}
								}
							}
						}
						else {
							event.setCancelled(true);
							isOutputting = false;
						}
					}
					default -> {
						event.setCancelled(true);
						isOutputting = false;
						new BukkitRunnable() {
							@Override
							public void run() {
								inventory.close();
								player.sendMessage(text("This anvil has been moved or destroyed.", NamedTextColor.RED));
							}
						}.runTask(SMP_Expansion.getPlugin());

					}
				}
			}
			else {
				event.setCancelled(true);
				isOutputting = false;
			}
		}
		else if (slot < 54 && slot != 29 && slot != 31) {
			event.setCancelled(true);
			isOutputting = false;
		}
		else
			isOutputting = false;
		new BukkitRunnable() {
			@Override
			public void run() {
				if (isOutputting && event.getClick() == ClickType.NUMBER_KEY && inventory.getItem(33) instanceof ItemStack item)
					player.give(item);
				inventory.setItem(33, getOutput(player.getLevel()));
				inventory.setItem(42, getOutputIcon());
			}
		}.runTask(SMP_Expansion.getPlugin());
	}

	@Override
	public void onClose() {
		List<HumanEntity> viewers = inventory.getViewers();
		if (!viewers.isEmpty() && viewers.getFirst() instanceof Player player) {
			if (inventory.getItem(29) instanceof ItemStack item)
				player.give(item);
			if (inventory.getItem(31) instanceof ItemStack item)
				player.give(item);
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
				inventory.setItem(33, getOutput(player.getLevel()));
				inventory.setItem(42, getOutputIcon());
			}
		}.runTask(SMP_Expansion.getPlugin());

	}

	/**
	 * Gets the output item to display depending on inputs
	 * @param playerLevel player's current level
	 * @return output item
	 */
	private ItemStack getOutput(int playerLevel) {
		ItemStack input = inventory.getItem(29);
		ItemStack sacrifice = inventory.getItem(31);
		if (input != null && sacrifice != null) {
			if (isRepairWithMaterials(input, sacrifice))
				return repairWithMaterials(input, sacrifice.getAmount(), playerLevel);
			else if (isCombineItems(input, sacrifice))
				return combineItems(input, sacrifice, playerLevel);
			else if (isUpgradeItem(input, sacrifice))
				return upgradeItem(input, sacrifice, playerLevel);
			else {
				sacrificeItemsConsumed = 0;
				xpCost = 0;
				result = Result.INVALID_RECIPE;
				return outputSlot;
			}
		}
		else {
			sacrificeItemsConsumed = 0;
			xpCost = 0;
			result = Result.INVALID_RECIPE;
			return outputSlot;
		}
	}

	/**
	 * Checks whether the current combination type is repairing item with materials
	 * @param input input material type
	 * @param sacrifice sacrifice material type
	 * @return if the input can be repaired using sacrifice
	 */
	private boolean isRepairWithMaterials(@NotNull ItemStack input, @NotNull ItemStack sacrifice) {
		return switch (sacrifice.getType()) {
			case OAK_PLANKS, BIRCH_PLANKS, SPRUCE_PLANKS, JUNGLE_PLANKS, ACACIA_PLANKS, DARK_OAK_PLANKS, MANGROVE_PLANKS, CHERRY_PLANKS, PALE_OAK_PLANKS, BAMBOO_PLANKS, CRIMSON_PLANKS, WARPED_PLANKS ->
					switch (input.getType()) {
						case WOODEN_SWORD, WOODEN_AXE, WOODEN_PICKAXE, WOODEN_SHOVEL, WOODEN_HOE, SHIELD ->
								true;
						default ->
								false;
					};
			case COBBLESTONE, COBBLED_DEEPSLATE, BLACKSTONE ->
					switch (input.getType()) {
						case STONE_SWORD, STONE_AXE, STONE_PICKAXE, STONE_SHOVEL, STONE_HOE ->
								true;
						default ->
								false;
					};
			case LEATHER ->
					switch (input.getType()) {
						case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS ->
								true;
						default ->
								false;
					};
			case COPPER_INGOT ->
					switch (input.getType()) {
						case COPPER_SWORD, COPPER_AXE, COPPER_PICKAXE, COPPER_SHOVEL, COPPER_HOE, COPPER_HELMET, COPPER_CHESTPLATE, COPPER_LEGGINGS, COPPER_BOOTS ->
								true;
						default ->
								false;
					};
			case IRON_INGOT ->
					switch (input.getType()) {
						case IRON_SWORD, IRON_AXE, IRON_PICKAXE, IRON_SHOVEL, IRON_HOE, CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS, IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS ->
								true;
						default ->
								false;
					};
			case GOLD_INGOT ->
					switch (input.getType()) {
						case GOLDEN_SWORD, GOLDEN_AXE, GOLDEN_PICKAXE, GOLDEN_SHOVEL, GOLDEN_HOE, GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS ->
								true;
						default ->
								false;
					};
			case DIAMOND ->
					switch (input.getType()) {
						case DIAMOND_SWORD, DIAMOND_AXE, DIAMOND_PICKAXE, DIAMOND_SHOVEL, DIAMOND_HOE, DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS ->
								true;
						default ->
								false;
					};
			case NETHERITE_INGOT ->
					switch (input.getType()) {
						case NETHERITE_SWORD, NETHERITE_AXE, NETHERITE_PICKAXE, NETHERITE_SHOVEL, NETHERITE_HOE, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS ->
								true;
						default ->
								false;
					};
			case TURTLE_SCUTE ->
					input.getType() == Material.TURTLE_HELMET;
			case PHANTOM_MEMBRANE ->
					input.getType() == Material.ELYTRA;
			case BREEZE_ROD ->
					input.getType() == Material.MACE;
			case ARMADILLO_SCUTE ->
					input.getType() == Material.WOLF_ARMOR;
			default ->
					false;

		};
	}

	/**
	 * Checks whether the current combination type is combining two items
	 * @param input input item
	 * @param sacrifice sacrifice item
	 * @return if the two items can be combined
	 */
	private boolean isCombineItems(@NotNull ItemStack input, @NotNull ItemStack sacrifice) {
		return input.getItemMeta().getPersistentDataContainer().has(isCustom) && sacrifice.getItemMeta().getPersistentDataContainer().has(isCustom) && (input.getType() == sacrifice.getType() || sacrifice.getType() == Material.ENCHANTED_BOOK);
	}

	/**
	 * Checks whether the current combination type is upgrading item tier
	 * @param input input item
	 * @param sacrifice sacrifice item
	 * @return if the two items can be combined
	 */
	private boolean isUpgradeItem(@NotNull ItemStack input, @NotNull ItemStack sacrifice) {
		return switch (input.getType()) {
			case WOODEN_SWORD ->
					sacrifice.getType() == Material.STONE_SWORD;
			case WOODEN_AXE ->
					sacrifice.getType() == Material.STONE_AXE;
			case WOODEN_PICKAXE ->
					sacrifice.getType() == Material.STONE_PICKAXE;
			case WOODEN_SHOVEL ->
					sacrifice.getType() == Material.STONE_SHOVEL;
			case WOODEN_HOE ->
					sacrifice.getType() == Material.STONE_HOE;
			case STONE_SWORD ->
					sacrifice.getType() == Material.COPPER_SWORD;
			case STONE_AXE ->
					sacrifice.getType() == Material.COPPER_AXE;
			case STONE_PICKAXE ->
					sacrifice.getType() == Material.COPPER_PICKAXE;
			case STONE_SHOVEL ->
					sacrifice.getType() == Material.COPPER_SHOVEL;
			case STONE_HOE ->
					sacrifice.getType() == Material.COPPER_HOE;
			case COPPER_SWORD ->
					sacrifice.getType() == Material.IRON_SWORD;
			case COPPER_AXE ->
					sacrifice.getType() == Material.IRON_AXE;
			case COPPER_PICKAXE ->
					sacrifice.getType() == Material.IRON_PICKAXE;
			case COPPER_SHOVEL ->
					sacrifice.getType() == Material.IRON_SHOVEL;
			case COPPER_HOE ->
					sacrifice.getType() == Material.IRON_HOE;
			case COPPER_HELMET, CHAINMAIL_HELMET ->
					sacrifice.getType() == Material.IRON_HELMET;
			case COPPER_CHESTPLATE, CHAINMAIL_CHESTPLATE ->
					sacrifice.getType() == Material.IRON_CHESTPLATE;
			case COPPER_LEGGINGS, CHAINMAIL_LEGGINGS ->
					sacrifice.getType() == Material.IRON_LEGGINGS;
			case COPPER_BOOTS, CHAINMAIL_BOOTS ->
					sacrifice.getType() == Material.IRON_BOOTS;
			case IRON_SWORD ->
					switch (sacrifice.getType()) {
						case GOLDEN_SWORD, DIAMOND_SWORD ->
								true;
						default ->
								false;
					};
			case IRON_AXE ->
					switch (sacrifice.getType()) {
						case GOLDEN_AXE, DIAMOND_AXE ->
								true;
						default ->
								false;
					};
			case IRON_PICKAXE ->
					switch (sacrifice.getType()) {
						case GOLDEN_PICKAXE, DIAMOND_PICKAXE ->
								true;
						default ->
								false;
					};
			case IRON_SHOVEL ->
					switch (sacrifice.getType()) {
						case GOLDEN_SHOVEL, DIAMOND_SHOVEL ->
								true;
						default ->
								false;
					};
			case IRON_HOE ->
					switch (sacrifice.getType()) {
						case GOLDEN_HOE, DIAMOND_HOE ->
								true;
						default ->
								false;
					};
			case IRON_HELMET ->
					switch (sacrifice.getType()) {
						case GOLDEN_HELMET, DIAMOND_HELMET, TURTLE_HELMET ->
								true;
						default ->
								false;
					};
			case IRON_CHESTPLATE ->
					switch (sacrifice.getType()) {
						case GOLDEN_CHESTPLATE, DIAMOND_CHESTPLATE ->
								true;
						default ->
								false;
					};
			case IRON_LEGGINGS ->
					switch (sacrifice.getType()) {
						case GOLDEN_LEGGINGS, DIAMOND_LEGGINGS ->
								true;
						default ->
								false;
					};
			case IRON_BOOTS ->
					switch (sacrifice.getType()) {
						case GOLDEN_BOOTS, DIAMOND_BOOTS ->
								true;
						default ->
								false;
					};
			default ->
					false;
		};
	}

	/**
	 * Returns the resulting item when repairing with materials and updates xp cost
	 * @param item item to repair
	 * @param materials max number of materials availabile
	 * @param playerLevel player's current level
	 * @return repaired item
	 */
	@NotNull
	private ItemStack repairWithMaterials(@NotNull ItemStack item, int materials, int playerLevel) {
		Damageable inputData = (Damageable)item.getItemMeta();
		int damage = inputData.hasDamage() ? inputData.getDamage() : 0;
		int maxDamage;
		if (inputData.hasMaxDamage())
			maxDamage = inputData.getMaxDamage();
		else {
			ItemType type = item.getType().asItemType();
			if (type != null)
				maxDamage = type.getMaxDurability();
			else
				maxDamage = 0;
		}
		int unitsUsed = getUnitsUsed(materials, maxDamage, damage);
		int newDamage = getNewDamage(unitsUsed, maxDamage, damage);
		sacrificeItemsConsumed = unitsUsed;
		xpCost = unitsUsed;
		if (xpCost == 0) {
			result = Result.INVALID_RECIPE;
			return outputSlot;
		}
		if (playerLevel < xpCost) {
			result = Result.NOT_ENOUGH_XP;
			return outputSlot;
		}
		else {
			result = Result.SUCCESS;
			ItemStack output = item.clone();
			output.editMeta(data -> {
				if (data instanceof Damageable damageable)
					damageable.setDamage(newDamage);
			});
			return output;
		}
	}

	/**
	 * Returns the resulting item when combining two items, combining durability and enchantments
	 * @param input item to be preserved
	 * @param sacrifice item to be destroyed
	 * @param playerLevel player's current level
	 * @return combined item
	 */
	@NotNull
	private ItemStack combineItems(@NotNull ItemStack input, @NotNull ItemStack sacrifice, int playerLevel) {
		ItemStack output = input.clone();
		int xpCost = 0;
		Damageable inputData = (Damageable)input.getItemMeta();
		if (inputData.hasDamage() && inputData.getDamage() > 0 && sacrifice.getType() != Material.ENCHANTED_BOOK) {
			inputData.setDamage(getCombinedDamage(input, sacrifice));
			xpCost += 2;
		}
		output.setItemMeta(inputData);
		EquipmentType inputType = EquipmentType.getType(input);
		ArrayList<EquipEnchantment> inputEnchantments = EquipEnchantment.getEnchantments(input);
		ArrayList<EquipEnchantment> sacrificeEnchantments = EquipEnchantment.getEnchantments(sacrifice);
		for (EquipEnchantment enchantment: sacrificeEnchantments) {
			EquipEnchantment inputEnchantment = enchantment.getMatchingEnchantment(inputEnchantments);
			if (inputType.getAllowedEnchantments().contains(enchantment.type) && enchantment.isCompatible(inputEnchantments)) {
				if (inputEnchantment == null) {
					// Try to apply enchantment to output item
					inputEnchantments.add(enchantment);
					enchantment.apply(output);
					xpCost += switch (enchantment.type.rarity) {
						case COMMON ->
								enchantment.level;
						case RARE ->
								enchantment.level * 2;
						case EPIC, GOLDEN ->
								enchantment.level * 3;
						case CURSE ->
								1;
					};
				}
				else {
					// Try to upgrade enchantment level of output item
					if (enchantment.level == inputEnchantment.level && enchantment.level < enchantment.type.maxLevel) {
						EquipEnchantment upgradedEnchantment = EquipEnchantment.getEnchantment(enchantment.type, enchantment.level + 1);
						assert upgradedEnchantment != null;
						inputEnchantment.remove(output);
						upgradedEnchantment.apply(output);
						xpCost += switch (enchantment.type.rarity) {
							case COMMON, CURSE ->
									1;
							case RARE ->
									2;
							case EPIC, GOLDEN ->
									3;
						};
					}
					// Try to add higher enchantment level of output item
					else if (enchantment.level > inputEnchantment.level) {
						inputEnchantment.remove(output);
						enchantment.apply(output);
						xpCost += switch (enchantment.type.rarity) {
							case COMMON ->
									enchantment.level - inputEnchantment.level;
							case RARE ->
									2 * (enchantment.level - inputEnchantment.level);
							case EPIC, GOLDEN ->
									3 * (enchantment.level - inputEnchantment.level);
							case CURSE ->
									0;
						};
					}
				}
			}
		}
		sacrificeItemsConsumed = 1;
		this.xpCost = xpCost;
		if (xpCost == 0) {
			result = Result.INVALID_RECIPE;
			return outputSlot;
		}
		else if (playerLevel < xpCost) {
			result = Result.NOT_ENOUGH_XP;
			return outputSlot;
		}
		else {
			result = Result.SUCCESS;
			return output;
		}
	}

	/**
	 * Returns the resulting item when upgrading an item, combining damage only
	 * @param input item to be upgraded
	 * @param sacrifice item to be destroyed
	 * @param playerLevel player's current level
	 * @return upgraded item
	 */
	@NotNull
	private ItemStack upgradeItem(@NotNull ItemStack input, @NotNull ItemStack sacrifice, int playerLevel) {
		int totalDamage;
		ArmorTrim oldTrim;
		totalDamage = getDamage(input) + getDamage(sacrifice);
		if (totalDamage >= getMaxDamage(sacrifice)) {
			// Too much damage on sacrifice item
			result = Result.NOT_ENOUGH_DURABILITY;
			return outputSlot;
		}
		if (input.getItemMeta() instanceof ArmorMeta armorMeta)
			oldTrim = armorMeta.getTrim();
		else
			oldTrim = null;
		ItemStack output = ItemManager.convertItem(ItemStack.of(sacrifice.getType(), 1));
		output.editMeta(data -> {
			if (data instanceof Damageable damageable)
				damageable.setDamage(totalDamage);
			if (data instanceof ArmorMeta armorMeta && oldTrim != null)
				armorMeta.setTrim(oldTrim);
			if (input.getItemMeta().getPersistentDataContainer().has(renamed))
				data.customName(input.getItemMeta().customName());
		});
		for (EquipEnchantment enchantment: EquipEnchantment.getEnchantments(input))
			enchantment.apply(output);
		sacrificeItemsConsumed = 1;
		xpCost = 2;
		if (playerLevel < xpCost) {
			result = Result.NOT_ENOUGH_XP;
			return outputSlot;
		}
		else {
			result = Result.SUCCESS;
			return output;
		}
	}

	/**
	 * Gets a variant of the output icon displaying xp level cost information
	 * @return output icon
	 */
	@NotNull
	private ItemStack getOutputIcon() {
		ItemStack outputIcon = new ItemStack(Material.DIAMOND_SWORD, 1);
		outputIcon.editMeta(data -> {
			data.customName(textNI("Resulting Equipment", NamedTextColor.YELLOW));
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			switch (result) {
				case SUCCESS ->
						lore.add(textNI("Cost: " + xpCost + " levels", NamedTextColor.GREEN));
				case NOT_ENOUGH_XP -> {
					lore.add(textNI("Cost: " + xpCost + " levels", NamedTextColor.RED));
					lore.add(textNI("Not Enough Levels!", NamedTextColor.RED));
				}
				case NOT_ENOUGH_DURABILITY ->
						lore.add(textNI("Not Enough Durability on Sacrificed Item!", NamedTextColor.RED));
			}
			data.lore(lore);
		});
		return outputIcon;
	}

	/**
	 * Gets the total units used to repair the item
	 * @param maxUnits maximum number of units available to repair
	 * @param maxDurability maximum durability of item
	 * @param damage current damage of item
	 * @return total units used
	 */
	private int getUnitsUsed(int maxUnits, int maxDurability, int damage) {
		return Math.min(maxUnits, (int)Math.ceil(damage / (maxDurability / 4.0)));
	}

	/**
	 * Gets the new durability of the item after units are used to repair
	 * @param unitsUsed total units used to repair item
	 * @param maxDurability maximum durability of item
	 * @param damage current damage of item
	 * @return new damage of item
	 */
	private int getNewDamage(int unitsUsed, int maxDurability, int damage) {
		return Math.max(0, damage - Math.round(maxDurability / 4.0f * unitsUsed));
	}

	/**
	 * Gets the combined durability of the two items after they are combined
	 * @param input input item
	 * @param sacrifice sacrifice item
	 * @return combined durability
	 */
	private int getCombinedDamage(@NotNull ItemStack input, @NotNull ItemStack sacrifice) {
		return Math.max(0, getMaxDamage(input) - Math.round(1.12f * ((getMaxDamage(input) - getDamage(input)) + (getMaxDamage(sacrifice) - getDamage(sacrifice)))));
	}

	/**
	 * Gets the current damage of item
	 * @param item item to get damage of
	 * @return current damage
	 */
	private int getDamage(@NotNull ItemStack item) {
		Damageable itemData = (Damageable)item.getItemMeta();
		if (itemData.hasDamage())
			return itemData.getDamage();
		else
			return 0;
	}

	/**
	 * Gets the maximum damage of this item
	 * @param item item to get maximum damage of
	 * @return maximum damage of item
	 */
	private int getMaxDamage(@NotNull ItemStack item) {
		Damageable itemData = (Damageable)item.getItemMeta();
		if (itemData.hasMaxDamage())
			return itemData.getMaxDamage();
		else {
			ItemType itemType = item.getType().asItemType();
			if (itemType != null) {
				return itemType.getMaxDurability();
			}
			else
				return 0;
		}
	}
}

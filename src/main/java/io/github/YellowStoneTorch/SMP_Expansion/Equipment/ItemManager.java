package io.github.YellowStoneTorch.SMP_Expansion.Equipment;

import com.google.common.collect.ArrayListMultimap;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EnchantmentType;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EquipEnchantment;
import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.KineticWeapon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.*;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;
import static net.kyori.adventure.text.Component.text;

/**
 * Holds methods for converting vanilla items into custom items
 * @author YellowStoneTorch
 * @version 0.1.1-ALPHA
 */
@SuppressWarnings("UnstableApiUsage")
public class ItemManager {
	private static final NamespacedKey attributeDamage;
	private static final NamespacedKey attributeAttackSpeed;
	private static final NamespacedKey attributeArmorHead;
	private static final NamespacedKey attributeArmorChest;
	private static final NamespacedKey attributeArmorLegs;
	private static final NamespacedKey attributeArmorFeet;
	private static final NamespacedKey attributeToughnessHead;
	private static final NamespacedKey attributeToughnessChest;
	private static final NamespacedKey attributeToughnessLegs;
	private static final NamespacedKey attributeToughnessFeet;
	private static final NamespacedKey attributeKBResHead;
	private static final NamespacedKey attributeKBResChest;
	private static final NamespacedKey attributeKBResLegs;
	private static final NamespacedKey attributeKBResFeet;

	static {
		attributeDamage = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeDamage");
		attributeAttackSpeed = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeAttackSpeed");
		attributeArmorHead = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeArmorHead");
		attributeArmorChest = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeArmorChest");
		attributeArmorLegs = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeArmorLegs");
		attributeArmorFeet = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeArmorFeet");
		attributeToughnessHead = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeToughnessHead");
		attributeToughnessChest = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeToughnessChest");
		attributeToughnessLegs = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeToughnessLegs");
		attributeToughnessFeet = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeToughnessFeet");
		attributeKBResHead = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeKBResHead");
		attributeKBResChest = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeKBResChest");
		attributeKBResLegs = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeKBResLegs");
		attributeKBResFeet = new NamespacedKey(SMP_Expansion.getPlugin(), "attributeKBResFeet");
	}

	/**
	 * Converts any vanilla items such as weapons and armor into custom items
	 * @param item item to convert
	 * @return converted item
	 */
	@NotNull
	public static ItemStack convertItem(@Nullable ItemStack item) {
		if (item == null)
			return ItemStack.of(Material.AIR, 1);
		Damageable data = (Damageable)item.getItemMeta();
		if (!data.getPersistentDataContainer().has(isCustom)) {
			ArrayList<EquipEnchantment> enchantments = new ArrayList<>();
			Map<Enchantment, Integer> oldEnchants;
			if (data instanceof EnchantmentStorageMeta dataE)
				oldEnchants = dataE.getStoredEnchants();
			else
				oldEnchants = data.getEnchants();
			for (Enchantment enchantment: oldEnchants.keySet())
				enchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.getNewEnchantment(enchantment), oldEnchants.get(enchantment)));
			ItemStack output = switch (item.getType()) {
				case ENCHANTED_BOOK ->
						getEnchantedBook(enchantments);
				case WOODEN_SWORD ->
						getWoodenSword(data.getDamage(), enchantments);
				case STONE_SWORD ->
						getStoneSword(data.getDamage(), enchantments);
				case COPPER_SWORD ->
						getCopperSword(data.getDamage(), enchantments);
				case IRON_SWORD ->
						getIronSword(data.getDamage(), enchantments);
				case GOLDEN_SWORD ->
						getGoldenSword(data.getDamage(), enchantments);
				case DIAMOND_SWORD ->
						getDiamondSword(data.getDamage(), enchantments);
				case NETHERITE_SWORD ->
						getNetheriteSword(1, data.getDamage(), enchantments);
				case WOODEN_SPEAR ->
						getWoodenSpear(data.getDamage(), enchantments);
				case STONE_SPEAR ->
						getStoneSpear(data.getDamage(), enchantments);
				case COPPER_SPEAR ->
						getCopperSpear(data.getDamage(), enchantments);
				case IRON_SPEAR ->
						getIronSpear(data.getDamage(), enchantments);
				case GOLDEN_SPEAR ->
						getGoldenSpear(data.getDamage(), enchantments);
				case DIAMOND_SPEAR ->
						getDiamondSpear(data.getDamage(), enchantments);
				case NETHERITE_SPEAR ->
						getNetheriteSpear(1, data.getDamage(), enchantments);
				case WOODEN_AXE ->
						getWoodenAxe(data.getDamage(), enchantments);
				case STONE_AXE ->
						getStoneAxe(data.getDamage(), enchantments);
				case COPPER_AXE ->
						getCopperAxe(data.getDamage(), enchantments);
				case IRON_AXE ->
						getIronAxe(data.getDamage(), enchantments);
				case GOLDEN_AXE ->
						getGoldenAxe(data.getDamage(), enchantments);
				case DIAMOND_AXE ->
						getDiamondAxe(data.getDamage(), enchantments);
				case NETHERITE_AXE ->
						getNetheriteAxe(1, data.getDamage(), enchantments);
				case WOODEN_PICKAXE ->
						getWoodenPickaxe(data.getDamage(), enchantments);
				case STONE_PICKAXE ->
						getStonePickaxe(data.getDamage(), enchantments);
				case COPPER_PICKAXE ->
						getCopperPickaxe(data.getDamage(), enchantments);
				case IRON_PICKAXE ->
						getIronPickaxe(data.getDamage(), enchantments);
				case GOLDEN_PICKAXE ->
						getGoldenPickaxe(data.getDamage(), enchantments);
				case DIAMOND_PICKAXE ->
						getDiamondPickaxe(data.getDamage(), enchantments);
				case NETHERITE_PICKAXE ->
						getNetheritePickaxe(data.getDamage(), enchantments);
				case WOODEN_SHOVEL ->
						getWoodenShovel(data.getDamage(), enchantments);
				case STONE_SHOVEL ->
						getStoneShovel(data.getDamage(), enchantments);
				case COPPER_SHOVEL ->
						getCopperShovel(data.getDamage(), enchantments);
				case IRON_SHOVEL ->
						getIronShovel(data.getDamage(), enchantments);
				case GOLDEN_SHOVEL ->
						getGoldenShovel(data.getDamage(), enchantments);
				case DIAMOND_SHOVEL ->
						getDiamondShovel(data.getDamage(), enchantments);
				case NETHERITE_SHOVEL ->
						getNetheriteShovel(data.getDamage(), enchantments);
				case WOODEN_HOE ->
						getWoodenHoe(data.getDamage(), enchantments);
				case STONE_HOE ->
						getStoneHoe(data.getDamage(), enchantments);
				case COPPER_HOE ->
						getCopperHoe(data.getDamage(), enchantments);
				case IRON_HOE ->
						getIronHoe(data.getDamage(), enchantments);
				case GOLDEN_HOE ->
						getGoldenHoe(data.getDamage(), enchantments);
				case DIAMOND_HOE ->
						getDiamondHoe(data.getDamage(), enchantments);
				case NETHERITE_HOE ->
						getNetheriteHoe(data.getDamage(), enchantments);
				case LEATHER_HELMET ->
						getLeatherCap(data.getDamage(), enchantments);
				case COPPER_HELMET ->
						getCopperHelmet(data.getDamage(), enchantments);
				case CHAINMAIL_HELMET ->
						getChainmailHelmet(data.getDamage(), enchantments);
				case IRON_HELMET ->
						getIronHelmet(data.getDamage(), enchantments);
				case TURTLE_HELMET ->
						getTurtleHelmet(data.getDamage(), enchantments);
				case GOLDEN_HELMET ->
						getGoldenHelmet(data.getDamage(), enchantments);
				case DIAMOND_HELMET ->
						getDiamondHelmet(data.getDamage(), enchantments);
				case NETHERITE_HELMET ->
						getNetheriteHelmet(1, data.getDamage(), enchantments);
				case LEATHER_CHESTPLATE ->
						getLeatherTunic(data.getDamage(), enchantments);
				case COPPER_CHESTPLATE ->
						getCopperChestplate(data.getDamage(), enchantments);
				case CHAINMAIL_CHESTPLATE ->
						getChainmailChesplate(data.getDamage(), enchantments);
				case IRON_CHESTPLATE ->
						getIronChestplate(data.getDamage(), enchantments);
				case GOLDEN_CHESTPLATE ->
						getGoldenChestplate(data.getDamage(), enchantments);
				case DIAMOND_CHESTPLATE ->
						getDiamondChestplate(data.getDamage(), enchantments);
				case NETHERITE_CHESTPLATE ->
						getNetheriteChestplate(1, data.getDamage(), enchantments);
				case LEATHER_LEGGINGS ->
						getLeatherPants(data.getDamage(), enchantments);
				case COPPER_LEGGINGS ->
						getCopperLeggings(data.getDamage(), enchantments);
				case CHAINMAIL_LEGGINGS ->
						getChainmailLeggings(data.getDamage(), enchantments);
				case IRON_LEGGINGS ->
						getIronLeggings(data.getDamage(), enchantments);
				case GOLDEN_LEGGINGS ->
						getGoldenLeggings(data.getDamage(), enchantments);
				case DIAMOND_LEGGINGS ->
						getDiamondLeggings(data.getDamage(), enchantments);
				case NETHERITE_LEGGINGS ->
						getNetheriteLeggings(1, data.getDamage(), enchantments);
				case LEATHER_BOOTS ->
						getLeatherBoots(data.getDamage(), enchantments);
				case COPPER_BOOTS ->
						getCopperBoots(data.getDamage(), enchantments);
				case CHAINMAIL_BOOTS ->
						getChainmailBoots(data.getDamage(), enchantments);
				case IRON_BOOTS ->
						getIronBoots(data.getDamage(), enchantments);
				case GOLDEN_BOOTS ->
						getGoldenBoots(data.getDamage(), enchantments);
				case DIAMOND_BOOTS ->
						getDiamondBoots(data.getDamage(), enchantments);
				case NETHERITE_BOOTS ->
						getNetheriteBoots(1, data.getDamage(), enchantments);
				case BOW ->
						getBow(data.getDamage(), enchantments);
				case CROSSBOW ->
						getCrossbow(data.getDamage(), enchantments);
				case TRIDENT ->
						getTrident(data.getDamage(), enchantments);
				case MACE ->
						getMace(data.getDamage(), enchantments);
				case FISHING_ROD ->
						getFishingRod(data.getDamage(), enchantments);
				case SHEARS ->
						getShears(data.getDamage(), enchantments);
				case ELYTRA ->
						getElytra(data.getDamage(), enchantments);
				case WOLF_ARMOR ->
						getWolfArmor(data.getDamage(), enchantments);
				case BRUSH ->
						getBrush(data.getDamage(), enchantments);
				case CARROT_ON_A_STICK ->
						getCarrotOnAStick(data.getDamage(), enchantments);
				case FLINT_AND_STEEL ->
						getFlintAndSteel(data.getDamage(), enchantments);
				case SHIELD ->
						getShield(data.getDamage(), enchantments);
				case WARPED_FUNGUS_ON_A_STICK ->
						getWarpedFungusOnAStick(data.getDamage(), enchantments);
				case CARVED_PUMPKIN ->
						getCarvedPumpkin(enchantments, item.getAmount());
				case CREEPER_HEAD ->
						getCreeperHead(enchantments, item.getAmount());
				case DRAGON_HEAD ->
						getDragonHead(enchantments, item.getAmount());
				case PIGLIN_HEAD ->
						getPiglinHead(enchantments, item.getAmount());
				case PLAYER_HEAD ->
						getPlayerHead(enchantments, item.getAmount());
				case SKELETON_SKULL ->
						getSkeletonSkull(enchantments, item.getAmount());
				case WITHER_SKELETON_SKULL ->
						getWitherSkeletonSkull(enchantments, item.getAmount());
				case ZOMBIE_HEAD ->
						getZombieHead(enchantments, item.getAmount());
				case COMPASS ->
						getCompass(enchantments, item.getAmount(), ((CompassMeta)item.getItemMeta()).getLodestone());
				default ->
						item;
			};
			if (data.customName() instanceof TextComponent component)
				output.editMeta(outputData -> outputData.customName(component));
			if (data instanceof LeatherArmorMeta colorData) {
				LeatherArmorMeta outputData = (LeatherArmorMeta)output.getItemMeta();
				outputData.setColor(colorData.getColor());
				output.setItemMeta(outputData);
			}
			if (data instanceof ArmorMeta trimData) {
				ArmorMeta outputData = (ArmorMeta)output.getItemMeta();
				outputData.setTrim(trimData.getTrim());
				output.setItemMeta(outputData);
			}
			if (data instanceof ShieldMeta shieldData) {
				ShieldMeta outputData = (ShieldMeta)output.getItemMeta();
				outputData.setBaseColor(shieldData.getBaseColor());
				outputData.setPatterns(shieldData.getPatterns());
				output.setItemMeta(outputData);
			}
			return output;
		}
		return item;
	}

	/**
	 * Converts all the items in an inventory
	 * @param inventory inventory to convert items
	 */
	public static void convertAllItems(@NotNull Inventory inventory) {
		ItemStack[] items = inventory.getContents();
		for (int i = 0; i < items.length; i++) {
			items[i] = convertItem(items[i]);
		}
		inventory.setContents(items);
	}

	/**
	 * Regenerates item lore after changes
	 * @param item item to change lore
	 */
	public static void regenerateLore(@NotNull ItemStack item) {
		ItemMeta data = item.getItemMeta();
		switch (EquipmentType.getType(data.getPersistentDataContainer().getOrDefault(equipmentType, PersistentDataType.STRING, ""))) {
			case SWORD, GOLDEN_SWORD ->
					data.lore(getSwordLore(item));
			case SPEAR, GOLDEN_SPEAR ->
					data.lore(getSpearLore(item));
			case AXE, GOLDEN_AXE ->
					data.lore(getAxeLore(item));
			case PICKAXE ->
					data.lore(getPickaxeLore(item));
			case SHOVEL ->
					data.lore(getShovelLore(item));
			case HOE ->
					data.lore(getHoeLore(item));
			case HELMET, GOLDEN_HELMET ->
					data.lore(getHelmetLore(item));
			case CHESTPLATE, GOLDEN_CHESTPLATE ->
					data.lore(getChestplateLore(item));
			case LEGGINGS, GOLDEN_LEGGINGS ->
					data.lore(getLeggingsLore(item));
			case BOOTS, GOLDEN_BOOTS ->
					data.lore(getBootsLore(item));
			case BOW ->
					data.lore(getBowLore(item));
			case CROSSBOW ->
					data.lore(getCrossbowLore(item));
			case TRIDENT ->
					data.lore(getTridentLore(item));
			case MACE ->
					data.lore(getMaceLore(item));
			case FISHING_ROD ->
					data.lore(getFishingRodLore(item));
			case SHEARS ->
					data.lore(getShearsLore(item));
			case ELYTRA ->
					data.lore(getElytraLore(item));
			case WOLF_ARMOR ->
					data.lore(getWolfArmorLore(item));
			case TOOL ->
					data.lore(getToolLore(item));
			case WEARABLE ->
					data.lore(getWearableLore(item));
			case VANISHABLE ->
					data.lore(getVanishableLore(item));
			case ENCHANTED_BOOK ->
					data.lore(getEnchantedBookLore(item));
		}
		item.setItemMeta(data);
	}

	/**
	 * Gets the lore to display for a sword
	 * @param sword sword to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getSwordLore(@NotNull ItemStack sword) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = sword.getItemMeta().getPersistentDataContainer();
		double damage = switch (sword.getType()) {
			case WOODEN_SWORD ->
					4.0;
			case STONE_SWORD ->
					5.0;
			case COPPER_SWORD ->
					5.5;
			case IRON_SWORD, GOLDEN_SWORD ->
					6.0;
			case DIAMOND_SWORD ->
					7.0;
			case NETHERITE_SWORD ->
					7.75 + (0.25 * dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 1));
			default ->
					0.0;
		};
		int level = dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 0);
		switch (level) {
			case 1 ->
					lore.add(textNI("Level 1", NamedTextColor.GRAY, TextDecoration.BOLD));
			case 2 ->
					lore.add(textNI("Level 2", NamedTextColor.WHITE, TextDecoration.BOLD));
			case 3 ->
					lore.add(textNI("Level 3", NamedTextColor.AQUA, TextDecoration.BOLD));
			case 4 ->
					lore.add(textNI("Level 4", NamedTextColor.GOLD, TextDecoration.BOLD));
			case 5 ->
					lore.add(textNI("Level 5", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD));
		}
		lore.add(Component.empty());
		lore.add(textNI(damage + "⚔", NamedTextColor.RED));
		lore.add(textNI("0.625s\uD83D\uDDE1", NamedTextColor.AQUA));
		lore.add(Component.empty());
		if (dataP.has(baneOfArthropods))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BANE_OF_ARTHROPODS, dataP.getOrDefault(baneOfArthropods, PersistentDataType.INTEGER, 1)));
		if (dataP.has(sharpness))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SHARPNESS, dataP.getOrDefault(sharpness, PersistentDataType.INTEGER, 1)));
		if (dataP.has(smite))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SMITE, dataP.getOrDefault(smite, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fireAspect))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FIRE_ASPECT, dataP.getOrDefault(fireAspect, PersistentDataType.INTEGER, 1)));
		if (dataP.has(knockback))
			lore.add(EnchantmentType.getTitle(EnchantmentType.KNOCKBACK, dataP.getOrDefault(knockback, PersistentDataType.INTEGER, 1)));
		if (dataP.has(looting))
			lore.add(EnchantmentType.getTitle(EnchantmentType.LOOTING, dataP.getOrDefault(looting, PersistentDataType.INTEGER, 1)));
		if (dataP.has(sweepingEdge))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SWEEPING_EDGE, dataP.getOrDefault(sweepingEdge, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for a spear
	 * @param spear spear to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getSpearLore(@NotNull ItemStack spear) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = spear.getItemMeta().getPersistentDataContainer();
		double damage = switch (spear.getType()) {
			case WOODEN_SPEAR ->
					2.0;
			case STONE_SPEAR ->
					3.0;
			case COPPER_SPEAR ->
					3.5;
			case IRON_SPEAR, GOLDEN_SPEAR ->
					4.0;
			case DIAMOND_SPEAR ->
					5.0;
			case NETHERITE_SPEAR ->
					5.75 + (0.25 * dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 1));
			default ->
					0.0;
		};
		double attackSpeed = switch (spear.getType()) {
			case WOODEN_SPEAR ->
					0.65;
			case STONE_SPEAR ->
					0.75;
			case COPPER_SPEAR ->
					0.85;
			case IRON_SPEAR, GOLDEN_SPEAR ->
					0.95;
			case DIAMOND_SPEAR ->
					1.05;
			case NETHERITE_SPEAR ->
					1.15;
			default ->
					0.0;
		};
		int level = dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 0);
		switch (level) {
			case 1 ->
					lore.add(textNI("Level 1", NamedTextColor.GRAY, TextDecoration.BOLD));
			case 2 ->
					lore.add(textNI("Level 2", NamedTextColor.WHITE, TextDecoration.BOLD));
			case 3 ->
					lore.add(textNI("Level 3", NamedTextColor.AQUA, TextDecoration.BOLD));
			case 4 ->
					lore.add(textNI("Level 4", NamedTextColor.GOLD, TextDecoration.BOLD));
			case 5 ->
					lore.add(textNI("Level 5", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD));
		}
		lore.add(Component.empty());
		lore.add(textNI(damage + "⚔", NamedTextColor.RED));
		lore.add(textNI(attackSpeed + "s\uD83D\uDDE1", NamedTextColor.AQUA));
		lore.add(Component.empty());
		if (dataP.has(baneOfArthropods))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BANE_OF_ARTHROPODS, dataP.getOrDefault(baneOfArthropods, PersistentDataType.INTEGER, 1)));
		if (dataP.has(sharpness))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SHARPNESS, dataP.getOrDefault(sharpness, PersistentDataType.INTEGER, 1)));
		if (dataP.has(smite))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SMITE, dataP.getOrDefault(smite, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fireAspect))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FIRE_ASPECT, dataP.getOrDefault(fireAspect, PersistentDataType.INTEGER, 1)));
		if (dataP.has(knockback))
			lore.add(EnchantmentType.getTitle(EnchantmentType.KNOCKBACK, dataP.getOrDefault(knockback, PersistentDataType.INTEGER, 1)));
		if (dataP.has(looting))
			lore.add(EnchantmentType.getTitle(EnchantmentType.LOOTING, dataP.getOrDefault(looting, PersistentDataType.INTEGER, 1)));
		if (dataP.has(lunge))
			lore.add(EnchantmentType.getTitle(EnchantmentType.LUNGE, dataP.getOrDefault(lunge, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for an axe
	 * @param axe axe to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getAxeLore(@NotNull ItemStack axe) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = axe.getItemMeta().getPersistentDataContainer();
		double damage = switch (axe.getType()) {
			case WOODEN_AXE ->
					7.0;
			case STONE_AXE ->
					8.0;
			case COPPER_AXE ->
					8.5;
			case IRON_AXE, GOLDEN_AXE, DIAMOND_AXE ->
					9.0;
			case NETHERITE_AXE ->
					9.75 + (0.25 * dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 1));
			default ->
					0.0;
		};
		double speed = switch (axe.getType()) {
			case WOODEN_AXE ->
					2.0;
			case STONE_AXE ->
					4.0;
			case COPPER_AXE ->
					5.0;
			case IRON_AXE ->
					6.0;
			case GOLDEN_AXE ->
					12.0;
			case DIAMOND_AXE ->
					8.0;
			case NETHERITE_AXE ->
					9.0;
			default ->
					0.0;
		};
		int level = dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 0);
		switch (level) {
			case 1 ->
					lore.add(textNI("Level 1", NamedTextColor.GRAY, TextDecoration.BOLD));
			case 2 ->
					lore.add(textNI("Level 2", NamedTextColor.WHITE, TextDecoration.BOLD));
			case 3 ->
					lore.add(textNI("Level 3", NamedTextColor.AQUA, TextDecoration.BOLD));
			case 4 ->
					lore.add(textNI("Level 4", NamedTextColor.GOLD, TextDecoration.BOLD));
			case 5 ->
					lore.add(textNI("Level 5", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD));
		}
		lore.add(Component.empty());
		lore.add(textNI(damage + "⚔", NamedTextColor.RED));
		lore.add(textNI("1.0s\uD83D\uDDE1", NamedTextColor.AQUA));
		lore.add(textNI(speed + "⛏", NamedTextColor.YELLOW));
		lore.add(Component.empty());
		if (dataP.has(baneOfArthropods))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BANE_OF_ARTHROPODS, dataP.getOrDefault(baneOfArthropods, PersistentDataType.INTEGER, 1)));
		if (dataP.has(efficiency))
			lore.add(EnchantmentType.getTitle(EnchantmentType.EFFICIENCY, dataP.getOrDefault(efficiency, PersistentDataType.INTEGER, 1)));
		if (dataP.has(sharpness))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SHARPNESS, dataP.getOrDefault(sharpness, PersistentDataType.INTEGER, 1)));
		if (dataP.has(smite))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SMITE, dataP.getOrDefault(smite, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fortune))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FORTUNE, dataP.getOrDefault(fortune, PersistentDataType.INTEGER, 1)));
		if (dataP.has(silkTouch))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SILK_TOUCH, 1));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for a pickaxe
	 * @param pickaxe pickaxe to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getPickaxeLore(@NotNull ItemStack pickaxe) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = pickaxe.getItemMeta().getPersistentDataContainer();
		double damage = switch (pickaxe.getType()) {
			case WOODEN_PICKAXE ->
					2.0;
			case STONE_PICKAXE ->
					3.0;
			case COPPER_PICKAXE ->
					3.5;
			case IRON_PICKAXE, GOLDEN_PICKAXE ->
					4.0;
			case DIAMOND_PICKAXE ->
					5.0;
			case NETHERITE_PICKAXE ->
					6.0;
			default ->
					0.0;
		};
		double speed = switch (pickaxe.getType()) {
			case WOODEN_PICKAXE ->
					2.0;
			case STONE_PICKAXE ->
					4.0;
			case COPPER_PICKAXE ->
					5.0;
			case IRON_PICKAXE ->
					6.0;
			case GOLDEN_PICKAXE ->
					12.0;
			case DIAMOND_PICKAXE ->
					8.0;
			case NETHERITE_PICKAXE ->
					9.0;
			default ->
					0.0;
		};
		lore.add(Component.empty());
		lore.add(textNI(damage + "⚔", NamedTextColor.RED));
		lore.add(textNI("0.83s\uD83D\uDDE1", NamedTextColor.AQUA));
		lore.add(textNI(speed + "⛏", NamedTextColor.YELLOW));
		lore.add(Component.empty());
		if (dataP.has(efficiency))
			lore.add(EnchantmentType.getTitle(EnchantmentType.EFFICIENCY, dataP.getOrDefault(efficiency, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fortune))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FORTUNE, dataP.getOrDefault(fortune, PersistentDataType.INTEGER, 1)));
		if (dataP.has(silkTouch))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SILK_TOUCH, 1));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for a shovel
	 * @param shovel shovel to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getShovelLore(@NotNull ItemStack shovel) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = shovel.getItemMeta().getPersistentDataContainer();
		double damage = switch (shovel.getType()) {
			case WOODEN_SHOVEL ->
					2.5;
			case STONE_SHOVEL ->
					3.5;
			case COPPER_SHOVEL ->
					4.0;
			case IRON_SHOVEL, GOLDEN_SHOVEL ->
					4.5;
			case DIAMOND_SHOVEL ->
					5.5;
			case NETHERITE_SHOVEL ->
					6.5;
			default ->
					0.0;
		};
		double speed = switch (shovel.getType()) {
			case WOODEN_SHOVEL ->
					2.0;
			case STONE_SHOVEL ->
					4.0;
			case COPPER_SHOVEL ->
					5.0;
			case IRON_SHOVEL ->
					6.0;
			case GOLDEN_SHOVEL ->
					12.0;
			case DIAMOND_SHOVEL ->
					8.0;
			case NETHERITE_SHOVEL ->
					9.0;
			default ->
					0.0;
		};
		lore.add(Component.empty());
		lore.add(textNI(damage + "⚔", NamedTextColor.RED));
		lore.add(textNI("1.0s\uD83D\uDDE1", NamedTextColor.AQUA));
		lore.add(textNI(speed + "⛏", NamedTextColor.YELLOW));
		lore.add(Component.empty());
		if (dataP.has(efficiency))
			lore.add(EnchantmentType.getTitle(EnchantmentType.EFFICIENCY, dataP.getOrDefault(efficiency, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fortune))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FORTUNE, dataP.getOrDefault(fortune, PersistentDataType.INTEGER, 1)));
		if (dataP.has(silkTouch))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SILK_TOUCH, 1));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for a hoe
	 * @param hoe hoe to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getHoeLore(@NotNull ItemStack hoe) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = hoe.getItemMeta().getPersistentDataContainer();
		double damage = switch (hoe.getType()) {
			case WOODEN_HOE ->
					3.0;
			case STONE_HOE ->
					4.0;
			case COPPER_HOE ->
					4.5;
			case IRON_HOE, GOLDEN_HOE ->
					5.0;
			case DIAMOND_HOE ->
					6.0;
			case NETHERITE_HOE ->
					7.0;
			default ->
					0.0;
		};
		double speed = switch (hoe.getType()) {
			case WOODEN_HOE ->
					2.0;
			case STONE_HOE ->
					4.0;
			case COPPER_HOE ->
					5.0;
			case IRON_HOE ->
					6.0;
			case GOLDEN_HOE ->
					12.0;
			case DIAMOND_HOE ->
					8.0;
			case NETHERITE_HOE ->
					9.0;
			default ->
					0.0;
		};
		lore.add(Component.empty());
		lore.add(textNI(damage + "⚔", NamedTextColor.RED));
		lore.add(textNI("1.0s\uD83D\uDDE1", NamedTextColor.AQUA));
		lore.add(textNI(speed + "⛏", NamedTextColor.YELLOW));
		lore.add(Component.empty());
		if (dataP.has(efficiency))
			lore.add(EnchantmentType.getTitle(EnchantmentType.EFFICIENCY, dataP.getOrDefault(efficiency, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fortune))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FORTUNE, dataP.getOrDefault(fortune, PersistentDataType.INTEGER, 1)));
		if (dataP.has(silkTouch))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SILK_TOUCH, 1));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for helmet
	 * @param helmet helmet to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getHelmetLore(@NotNull ItemStack helmet) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = helmet.getItemMeta().getPersistentDataContainer();
		double armor = switch (helmet.getType()) {
			case LEATHER_HELMET ->
					1.0;
			case COPPER_HELMET, CHAINMAIL_HELMET, IRON_HELMET, GOLDEN_HELMET, TURTLE_HELMET ->
					2.0;
			case DIAMOND_HELMET, NETHERITE_HELMET ->
					3.0;
			default ->
					0.0;
		};
		double toughness = switch (helmet.getType()) {
			case COPPER_HELMET, CHAINMAIL_HELMET ->
					0.5;
			case IRON_HELMET, GOLDEN_HELMET, TURTLE_HELMET ->
					1.0;
			case DIAMOND_HELMET ->
					2.0;
			case NETHERITE_HELMET ->
					2.875 + (0.125 * dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 1));
			default ->
					0.0;
		};
		int level = dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 0);
		switch (level) {
			case 1 ->
					lore.add(textNI("Level 1", NamedTextColor.GRAY, TextDecoration.BOLD));
			case 2 ->
					lore.add(textNI("Level 2", NamedTextColor.WHITE, TextDecoration.BOLD));
			case 3 ->
					lore.add(textNI("Level 3", NamedTextColor.AQUA, TextDecoration.BOLD));
			case 4 ->
					lore.add(textNI("Level 4", NamedTextColor.GOLD, TextDecoration.BOLD));
			case 5 ->
					lore.add(textNI("Level 5", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD));
		}
		lore.add(Component.empty());
		lore.add(textNI(armor + "\uD83D\uDEE1", NamedTextColor.BLUE));
		if (toughness != 0.0)
			lore.add(textNI(toughness + "♦", NamedTextColor.GRAY));
		lore.add(Component.empty());
		if (dataP.has(blastProtection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BLAST_PROTECTION, dataP.getOrDefault(blastProtection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fireProtection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FIRE_PROTECTION, dataP.getOrDefault(fireProtection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(protection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.PROTECTION, dataP.getOrDefault(protection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(reinforcement))
			lore.add(EnchantmentType.getTitle(EnchantmentType.REINFORCEMENT, dataP.getOrDefault(reinforcement, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(aquaAffinity))
			lore.add(EnchantmentType.getTitle(EnchantmentType.AQUA_AFFINITY, 1));
		if (dataP.has(respiration))
			lore.add(EnchantmentType.getTitle(EnchantmentType.RESPIRATION, dataP.getOrDefault(respiration, PersistentDataType.INTEGER, 1)));
		if (dataP.has(thorns))
			lore.add(EnchantmentType.getTitle(EnchantmentType.THORNS, dataP.getOrDefault(thorns, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfBinding))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BINDING_CURSE, dataP.getOrDefault(curseOfBinding, PersistentDataType.INTEGER, 1)));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for chestplate
	 * @param chestplate chestplate to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getChestplateLore(@NotNull ItemStack chestplate) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = chestplate.getItemMeta().getPersistentDataContainer();
		double armor = switch (chestplate.getType()) {
			case LEATHER_CHESTPLATE ->
					3.0;
			case COPPER_CHESTPLATE, CHAINMAIL_CHESTPLATE ->
					5.0;
			case IRON_CHESTPLATE, GOLDEN_CHESTPLATE ->
					6.0;
			case DIAMOND_CHESTPLATE, NETHERITE_CHESTPLATE ->
					8.0;
			default ->
					0.0;
		};
		double toughness = switch (chestplate.getType()) {
			case COPPER_CHESTPLATE, CHAINMAIL_CHESTPLATE ->
					0.5;
			case IRON_CHESTPLATE, GOLDEN_CHESTPLATE ->
					1.0;
			case DIAMOND_CHESTPLATE ->
					2.0;
			case NETHERITE_CHESTPLATE ->
					2.875 + (0.125 * dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 1));
			default ->
					0.0;
		};
		int level = dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 0);
		switch (level) {
			case 1 ->
					lore.add(textNI("Level 1", NamedTextColor.GRAY, TextDecoration.BOLD));
			case 2 ->
					lore.add(textNI("Level 2", NamedTextColor.WHITE, TextDecoration.BOLD));
			case 3 ->
					lore.add(textNI("Level 3", NamedTextColor.AQUA, TextDecoration.BOLD));
			case 4 ->
					lore.add(textNI("Level 4", NamedTextColor.GOLD, TextDecoration.BOLD));
			case 5 ->
					lore.add(textNI("Level 5", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD));
		}
		lore.add(Component.empty());
		lore.add(textNI(armor + "\uD83D\uDEE1", NamedTextColor.BLUE));
		if (toughness != 0.0)
			lore.add(textNI(toughness + "♦", NamedTextColor.GRAY));
		lore.add(Component.empty());
		if (dataP.has(blastProtection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BLAST_PROTECTION, dataP.getOrDefault(blastProtection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fireProtection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FIRE_PROTECTION, dataP.getOrDefault(fireProtection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(protection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.PROTECTION, dataP.getOrDefault(protection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(reinforcement))
			lore.add(EnchantmentType.getTitle(EnchantmentType.REINFORCEMENT, dataP.getOrDefault(reinforcement, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(thorns))
			lore.add(EnchantmentType.getTitle(EnchantmentType.THORNS, dataP.getOrDefault(thorns, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfBinding))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BINDING_CURSE, dataP.getOrDefault(curseOfBinding, PersistentDataType.INTEGER, 1)));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for leggings
	 * @param leggings leggings to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getLeggingsLore(@NotNull ItemStack leggings) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = leggings.getItemMeta().getPersistentDataContainer();
		double armor = switch (leggings.getType()) {
			case LEATHER_LEGGINGS ->
					2.0;
			case COPPER_LEGGINGS ->
					3.0;
			case CHAINMAIL_LEGGINGS ->
					4.0;
			case IRON_LEGGINGS, GOLDEN_LEGGINGS ->
					5.0;
			case DIAMOND_LEGGINGS, NETHERITE_LEGGINGS ->
					6.0;
			default ->
					0.0;
		};
		double toughness = switch (leggings.getType()) {
			case COPPER_LEGGINGS, CHAINMAIL_LEGGINGS ->
					0.5;
			case IRON_LEGGINGS, GOLDEN_LEGGINGS ->
					1.0;
			case DIAMOND_LEGGINGS ->
					2.0;
			case NETHERITE_LEGGINGS ->
					2.875 + (0.125 * dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 1));
			default ->
					0.0;
		};
		int level = dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 0);
		switch (level) {
			case 1 ->
					lore.add(textNI("Level 1", NamedTextColor.GRAY, TextDecoration.BOLD));
			case 2 ->
					lore.add(textNI("Level 2", NamedTextColor.WHITE, TextDecoration.BOLD));
			case 3 ->
					lore.add(textNI("Level 3", NamedTextColor.AQUA, TextDecoration.BOLD));
			case 4 ->
					lore.add(textNI("Level 4", NamedTextColor.GOLD, TextDecoration.BOLD));
			case 5 ->
					lore.add(textNI("Level 5", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD));
		}
		lore.add(Component.empty());
		lore.add(textNI(armor + "\uD83D\uDEE1", NamedTextColor.BLUE));
		if (toughness != 0.0)
			lore.add(textNI(toughness + "♦", NamedTextColor.GRAY));
		lore.add(Component.empty());
		if (dataP.has(blastProtection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BLAST_PROTECTION, dataP.getOrDefault(blastProtection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fireProtection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FIRE_PROTECTION, dataP.getOrDefault(fireProtection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(protection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.PROTECTION, dataP.getOrDefault(protection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(reinforcement))
			lore.add(EnchantmentType.getTitle(EnchantmentType.REINFORCEMENT, dataP.getOrDefault(reinforcement, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(thorns))
			lore.add(EnchantmentType.getTitle(EnchantmentType.THORNS, dataP.getOrDefault(thorns, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(swiftSneak))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SWIFT_SNEAK, dataP.getOrDefault(swiftSneak, PersistentDataType.INTEGER, 1)));
		if (dataP.has(curseOfBinding))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BINDING_CURSE, dataP.getOrDefault(curseOfBinding, PersistentDataType.INTEGER, 1)));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for boots
	 * @param boots boots to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getBootsLore(@NotNull ItemStack boots) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = boots.getItemMeta().getPersistentDataContainer();
		double armor = switch (boots.getType()) {
			case LEATHER_BOOTS, COPPER_BOOTS, CHAINMAIL_BOOTS ->
					1.0;
			case IRON_BOOTS, GOLDEN_BOOTS ->
					2.0;
			case DIAMOND_BOOTS, NETHERITE_BOOTS ->
					3.0;
			default ->
					0.0;
		};
		double toughness = switch (boots.getType()) {
			case COPPER_BOOTS, CHAINMAIL_BOOTS ->
					0.5;
			case IRON_BOOTS, GOLDEN_BOOTS ->
					1.0;
			case DIAMOND_BOOTS ->
					2.0;
			case NETHERITE_BOOTS ->
					2.875 + (0.125 * dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 1));
			default ->
					0.0;
		};
		int level = dataP.getOrDefault(itemLevel, PersistentDataType.INTEGER, 0);
		switch (level) {
			case 1 ->
					lore.add(textNI("Level 1", NamedTextColor.GRAY, TextDecoration.BOLD));
			case 2 ->
					lore.add(textNI("Level 2", NamedTextColor.WHITE, TextDecoration.BOLD));
			case 3 ->
					lore.add(textNI("Level 3", NamedTextColor.AQUA, TextDecoration.BOLD));
			case 4 ->
					lore.add(textNI("Level 4", NamedTextColor.GOLD, TextDecoration.BOLD));
			case 5 ->
					lore.add(textNI("Level 5", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD));
		}
		lore.add(Component.empty());
		lore.add(textNI(armor + "\uD83D\uDEE1", NamedTextColor.BLUE));
		if (toughness != 0.0)
			lore.add(textNI(toughness + "♦", NamedTextColor.GRAY));
		lore.add(Component.empty());
		if (dataP.has(blastProtection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BLAST_PROTECTION, dataP.getOrDefault(blastProtection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fireProtection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FIRE_PROTECTION, dataP.getOrDefault(fireProtection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(protection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.PROTECTION, dataP.getOrDefault(protection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(reinforcement))
			lore.add(EnchantmentType.getTitle(EnchantmentType.REINFORCEMENT, dataP.getOrDefault(reinforcement, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(depthStrider))
			lore.add(EnchantmentType.getTitle(EnchantmentType.DEPTH_STRIDER, dataP.getOrDefault(depthStrider, PersistentDataType.INTEGER, 1)));
		if (dataP.has(featherFalling))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FEATHER_FALLING, dataP.getOrDefault(featherFalling, PersistentDataType.INTEGER, 1)));
		if (dataP.has(thorns))
			lore.add(EnchantmentType.getTitle(EnchantmentType.THORNS, dataP.getOrDefault(thorns, PersistentDataType.INTEGER, 1)));
		if (dataP.has(frostWalker))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FROST_WALKER, dataP.getOrDefault(frostWalker, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(soulSpeed))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SOUL_SPEED, dataP.getOrDefault(soulSpeed, PersistentDataType.INTEGER, 1)));
		if (dataP.has(curseOfBinding))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BINDING_CURSE, dataP.getOrDefault(curseOfBinding, PersistentDataType.INTEGER, 1)));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for a bow
	 * @param bow bow to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getBowLore(@NotNull ItemStack bow) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = bow.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		lore.add(textNI("8.0\uD83C\uDFF9", NamedTextColor.RED));
		lore.add(Component.empty());
		if (dataP.has(power))
			lore.add(EnchantmentType.getTitle(EnchantmentType.POWER, dataP.getOrDefault(power, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(flame))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FLAME, 1));
		if (dataP.has(infinity))
			lore.add(EnchantmentType.getTitle(EnchantmentType.INFINITY, 1));
		if (dataP.has(punch))
			lore.add(EnchantmentType.getTitle(EnchantmentType.PUNCH, dataP.getOrDefault(punch, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for a crossbow
	 * @param crossbow crossbow to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getCrossbowLore(@NotNull ItemStack crossbow) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = crossbow.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		lore.add(textNI("9.0\uD83C\uDFF9", NamedTextColor.RED));
		lore.add(Component.empty());
		if (dataP.has(power))
			lore.add(EnchantmentType.getTitle(EnchantmentType.POWER, dataP.getOrDefault(power, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(multishot))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MULTISHOT, 1));
		if (dataP.has(piercing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.PIERCING, dataP.getOrDefault(piercing, PersistentDataType.INTEGER, 1)));
		if (dataP.has(quickCharge))
			lore.add(EnchantmentType.getTitle(EnchantmentType.QUICK_CHARGE, dataP.getOrDefault(quickCharge, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for a trident
	 * @param trident trident to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getTridentLore(@NotNull ItemStack trident) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = trident.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		lore.add(textNI("9.0⚔", NamedTextColor.RED));
		lore.add(textNI("8.0\uD83C\uDFF9", NamedTextColor.RED));
		lore.add(textNI("0.91s\uD83D\uDDE1", NamedTextColor.AQUA));
		lore.add(Component.empty());
		if (dataP.has(baneOfArthropods))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BANE_OF_ARTHROPODS, dataP.getOrDefault(baneOfArthropods, PersistentDataType.INTEGER, 1)));
		if (dataP.has(impaling))
			lore.add(EnchantmentType.getTitle(EnchantmentType.IMPALING, dataP.getOrDefault(impaling, PersistentDataType.INTEGER, 1)));
		if (dataP.has(sharpness))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SHARPNESS, dataP.getOrDefault(sharpness, PersistentDataType.INTEGER, 1)));
		if (dataP.has(smite))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SMITE, dataP.getOrDefault(smite, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(channeling))
			lore.add(EnchantmentType.getTitle(EnchantmentType.CHANNELING, 1));
		if (dataP.has(loyalty))
			lore.add(EnchantmentType.getTitle(EnchantmentType.LOYALTY, dataP.getOrDefault(loyalty, PersistentDataType.INTEGER, 1)));
		if (dataP.has(riptide))
			lore.add(EnchantmentType.getTitle(EnchantmentType.RIPTIDE, dataP.getOrDefault(riptide, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for a mace
	 * @param mace mace to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getMaceLore(@NotNull ItemStack mace) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = mace.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		lore.add(textNI("6.0⚔", NamedTextColor.RED));
		lore.add(textNI("1.25s\uD83D\uDDE1", NamedTextColor.AQUA));
		lore.add(Component.empty());
		if (dataP.has(baneOfArthropods))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BANE_OF_ARTHROPODS, dataP.getOrDefault(baneOfArthropods, PersistentDataType.INTEGER, 1)));
		if (dataP.has(sharpness))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SHARPNESS, dataP.getOrDefault(sharpness, PersistentDataType.INTEGER, 1)));
		if (dataP.has(smite))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SMITE, dataP.getOrDefault(smite, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(breach))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BREACH, dataP.getOrDefault(breach, PersistentDataType.INTEGER, 1)));
		if (dataP.has(density))
			lore.add(EnchantmentType.getTitle(EnchantmentType.DENSITY, dataP.getOrDefault(density, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(windBurst))
			lore.add(EnchantmentType.getTitle(EnchantmentType.WIND_BURST, dataP.getOrDefault(windBurst, PersistentDataType.INTEGER, 1)));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;

	}

	/**
	 * Gets the lore to display for a fishing rod
	 * @param rod fishing rod to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getFishingRodLore(@NotNull ItemStack rod) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = rod.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(luckOfTheSea))
			lore.add(EnchantmentType.getTitle(EnchantmentType.LUCK_OF_THE_SEA, dataP.getOrDefault(luckOfTheSea, PersistentDataType.INTEGER, 1)));
		if (dataP.has(lure))
			lore.add(EnchantmentType.getTitle(EnchantmentType.LURE, dataP.getOrDefault(lure, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;

	}

	/**
	 * Gets the lore to display for a shears
	 * @param shears shears to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getShearsLore(@NotNull ItemStack shears) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = shears.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		if (dataP.has(efficiency))
			lore.add(EnchantmentType.getTitle(EnchantmentType.EFFICIENCY, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;

	}

	/**
	 * Gets the lore to display for an elytra
	 * @param elytra elytra to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getElytraLore(@NotNull ItemStack elytra) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = elytra.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfBinding))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BINDING_CURSE, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for wolf armor
	 * @param wolfArmor wolf armor to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getWolfArmorLore(@NotNull ItemStack wolfArmor) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = wolfArmor.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		if (dataP.has(protection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.PROTECTION, dataP.getOrDefault(protection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(thorns))
			lore.add(EnchantmentType.getTitle(EnchantmentType.THORNS, dataP.getOrDefault(thorns, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfBinding))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BINDING_CURSE, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for a tool with durability
	 * @param tool tool to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getToolLore(@NotNull ItemStack tool) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = tool.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;

	}

	/**
	 * Gets the lore to display for a wearable
	 * @param wearable wearable to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getWearableLore(@NotNull ItemStack wearable) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = wearable.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		if (dataP.has(curseOfBinding))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BINDING_CURSE, 1));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for a vanishable
	 * @param vanishable vanishable to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getVanishableLore(@NotNull ItemStack vanishable) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = vanishable.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;
	}

	/**
	 * Gets the lore to display for an enchanted book
	 * @param book enchanted book to get lore for
	 * @return lore
	 */
	@NotNull
	private static ArrayList<TextComponent> getEnchantedBookLore(@NotNull ItemStack book) {
		ArrayList<TextComponent> lore = new ArrayList<>();
		PersistentDataContainer dataP = book.getItemMeta().getPersistentDataContainer();
		lore.add(Component.empty());
		if (dataP.has(baneOfArthropods))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BANE_OF_ARTHROPODS, dataP.getOrDefault(baneOfArthropods, PersistentDataType.INTEGER, 1)));
		if (dataP.has(blastProtection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BLAST_PROTECTION, dataP.getOrDefault(blastProtection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(efficiency))
			lore.add(EnchantmentType.getTitle(EnchantmentType.EFFICIENCY, dataP.getOrDefault(efficiency, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fireProtection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FIRE_PROTECTION, dataP.getOrDefault(fireProtection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(impaling))
			lore.add(EnchantmentType.getTitle(EnchantmentType.IMPALING, dataP.getOrDefault(impaling, PersistentDataType.INTEGER, 1)));
		if (dataP.has(power))
			lore.add(EnchantmentType.getTitle(EnchantmentType.POWER, dataP.getOrDefault(power, PersistentDataType.INTEGER, 1)));
		if (dataP.has(protection))
			lore.add(EnchantmentType.getTitle(EnchantmentType.PROTECTION, dataP.getOrDefault(protection, PersistentDataType.INTEGER, 1)));
		if (dataP.has(reinforcement))
			lore.add(EnchantmentType.getTitle(EnchantmentType.REINFORCEMENT, dataP.getOrDefault(reinforcement, PersistentDataType.INTEGER, 1)));
		if (dataP.has(sharpness))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SHARPNESS, dataP.getOrDefault(sharpness, PersistentDataType.INTEGER, 1)));
		if (dataP.has(smite))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SMITE, dataP.getOrDefault(smite, PersistentDataType.INTEGER, 1)));
		if (dataP.has(unbreaking))
			lore.add(EnchantmentType.getTitle(EnchantmentType.UNBREAKING, dataP.getOrDefault(unbreaking, PersistentDataType.INTEGER, 1)));
		if (dataP.has(aquaAffinity))
			lore.add(EnchantmentType.getTitle(EnchantmentType.AQUA_AFFINITY, 1));
		if (dataP.has(breach))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BREACH, dataP.getOrDefault(breach, PersistentDataType.INTEGER, 1)));
		if (dataP.has(channeling))
			lore.add(EnchantmentType.getTitle(EnchantmentType.CHANNELING, 1));
		if (dataP.has(density))
			lore.add(EnchantmentType.getTitle(EnchantmentType.DENSITY, dataP.getOrDefault(density, PersistentDataType.INTEGER, 1)));
		if (dataP.has(depthStrider))
			lore.add(EnchantmentType.getTitle(EnchantmentType.DEPTH_STRIDER, dataP.getOrDefault(depthStrider, PersistentDataType.INTEGER, 1)));
		if (dataP.has(featherFalling))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FEATHER_FALLING, dataP.getOrDefault(featherFalling, PersistentDataType.INTEGER, 1)));
		if (dataP.has(fireAspect))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FIRE_ASPECT, dataP.getOrDefault(fireAspect, PersistentDataType.INTEGER, 1)));
		if (dataP.has(flame))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FLAME, 1));
		if (dataP.has(fortune))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FORTUNE, dataP.getOrDefault(fortune, PersistentDataType.INTEGER, 1)));
		if (dataP.has(infinity))
			lore.add(EnchantmentType.getTitle(EnchantmentType.INFINITY, 1));
		if (dataP.has(knockback))
			lore.add(EnchantmentType.getTitle(EnchantmentType.KNOCKBACK, dataP.getOrDefault(knockback, PersistentDataType.INTEGER, 1)));
		if (dataP.has(looting))
			lore.add(EnchantmentType.getTitle(EnchantmentType.LOOTING, dataP.getOrDefault(looting, PersistentDataType.INTEGER, 1)));
		if (dataP.has(loyalty))
			lore.add(EnchantmentType.getTitle(EnchantmentType.LOYALTY, dataP.getOrDefault(loyalty, PersistentDataType.INTEGER, 1)));
		if (dataP.has(luckOfTheSea))
			lore.add(EnchantmentType.getTitle(EnchantmentType.LUCK_OF_THE_SEA, dataP.getOrDefault(luckOfTheSea, PersistentDataType.INTEGER, 1)));
		if (dataP.has(lure))
			lore.add(EnchantmentType.getTitle(EnchantmentType.LURE, dataP.getOrDefault(lure, PersistentDataType.INTEGER, 1)));
		if (dataP.has(multishot))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MULTISHOT, 1));
		if (dataP.has(piercing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.PIERCING, dataP.getOrDefault(piercing, PersistentDataType.INTEGER, 1)));
		if (dataP.has(punch))
			lore.add(EnchantmentType.getTitle(EnchantmentType.PUNCH, dataP.getOrDefault(punch, PersistentDataType.INTEGER, 1)));
		if (dataP.has(quickCharge))
			lore.add(EnchantmentType.getTitle(EnchantmentType.QUICK_CHARGE, dataP.getOrDefault(quickCharge, PersistentDataType.INTEGER, 1)));
		if (dataP.has(respiration))
			lore.add(EnchantmentType.getTitle(EnchantmentType.RESPIRATION, dataP.getOrDefault(respiration, PersistentDataType.INTEGER, 1)));
		if (dataP.has(riptide))
			lore.add(EnchantmentType.getTitle(EnchantmentType.RIPTIDE, dataP.getOrDefault(riptide, PersistentDataType.INTEGER, 1)));
		if (dataP.has(silkTouch))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SILK_TOUCH, 1));
		if (dataP.has(sweepingEdge))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SWEEPING_EDGE, dataP.getOrDefault(sweepingEdge, PersistentDataType.INTEGER, 1)));
		if (dataP.has(thorns))
			lore.add(EnchantmentType.getTitle(EnchantmentType.THORNS, dataP.getOrDefault(thorns, PersistentDataType.INTEGER, 1)));
		if (dataP.has(frostWalker))
			lore.add(EnchantmentType.getTitle(EnchantmentType.FROST_WALKER, dataP.getOrDefault(frostWalker, PersistentDataType.INTEGER, 1)));
		if (dataP.has(mending))
			lore.add(EnchantmentType.getTitle(EnchantmentType.MENDING, 1));
		if (dataP.has(soulSpeed))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SOUL_SPEED, dataP.getOrDefault(soulSpeed, PersistentDataType.INTEGER, 1)));
		if (dataP.has(swiftSneak))
			lore.add(EnchantmentType.getTitle(EnchantmentType.SWIFT_SNEAK, dataP.getOrDefault(swiftSneak, PersistentDataType.INTEGER, 1)));
		if (dataP.has(windBurst))
			lore.add(EnchantmentType.getTitle(EnchantmentType.WIND_BURST, dataP.getOrDefault(windBurst, PersistentDataType.INTEGER, 1)));
		if (dataP.has(curseOfBinding))
			lore.add(EnchantmentType.getTitle(EnchantmentType.BINDING_CURSE, dataP.getOrDefault(curseOfBinding, PersistentDataType.INTEGER, 1)));
		if (dataP.has(curseOfVanishing))
			lore.add(EnchantmentType.getTitle(EnchantmentType.VANISHING_CURSE, 1));
		return lore;

	}

	/**
	 * Creates a custom wooden sword
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new wooden sword
	 */
	@NotNull
	public static ItemStack getWoodenSword(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack sword = ItemStack.of(Material.WOODEN_SWORD, 1);
		Damageable data = (Damageable)sword.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SWORD");
		data.setDamage(damage);
		data.lore(getSwordLore(sword));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		sword.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(sword);
		return sword;
	}

	/**
	 * Creates a custom stone sword
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new stone sword
	 */
	@NotNull
	public static ItemStack getStoneSword(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack sword = ItemStack.of(Material.STONE_SWORD, 1);
		Damageable data = (Damageable)sword.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SWORD");
		data.setDamage(damage);
		data.lore(getSwordLore(sword));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		sword.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(sword);
		return sword;
	}

	/**
	 * Creates a custom copper sword
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new copper sword
	 */
	@NotNull
	public static ItemStack getCopperSword(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack sword = ItemStack.of(Material.COPPER_SWORD, 1);
		Damageable data = (Damageable)sword.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 4.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SWORD");
		data.setDamage(damage);
		sword.setItemMeta(data);
		data.lore(getSwordLore(sword));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		sword.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(sword);
		return sword;
	}

	/**
	 * Creates a custom iron sword
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new iron sword
	 */
	@NotNull
	public static ItemStack getIronSword(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack sword = ItemStack.of(Material.IRON_SWORD, 1);
		Damageable data = (Damageable)sword.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 5.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SWORD");
		data.setDamage(damage);
		sword.setItemMeta(data);
		data.lore(getSwordLore(sword));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		sword.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(sword);
		return sword;
	}

	/**
	 * Creates a custom golden sword
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new golden sword
	 */
	@NotNull
	public static ItemStack getGoldenSword(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack sword = ItemStack.of(Material.GOLDEN_SWORD, 1);
		Damageable data = (Damageable)sword.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 5.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "GOLDEN_SWORD");
		data.setDamage(damage);
		data.setMaxDamage(120);
		sword.setItemMeta(data);
		data.lore(getSwordLore(sword));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		sword.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(sword);
		return sword;
	}

	/**
	 * Creates a custom diamond sword
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new diamond sword
	 */
	@NotNull
	public static ItemStack getDiamondSword(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack sword = ItemStack.of(Material.DIAMOND_SWORD, 1);
		Damageable data = (Damageable)sword.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SWORD");
		data.setDamage(damage);
		sword.setItemMeta(data);
		data.lore(getSwordLore(sword));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		sword.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(sword);
		return sword;
	}

	/**
	 * Creates a custom netherite sword
	 * @param level level of sword (1-5)
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new netherite sword
	 */
	@NotNull
	public static ItemStack getNetheriteSword(int level, int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack sword = ItemStack.of(Material.NETHERITE_SWORD, 1);
		Damageable data = (Damageable)sword.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 6.75 + (0.25 * level), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SWORD");
		dataP.set(itemLevel, PersistentDataType.INTEGER, level);
		data.setDamage(damage);
		data.setMaxDamage(2000 + (100 * level));
		sword.setItemMeta(data);
		data.lore(getSwordLore(sword));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		sword.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(sword);
		return sword;
	}

	/**
	 * Creates a custom wooden spear
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new wooden spear
	 */
	@NotNull
	public static ItemStack getWoodenSpear(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack spear = ItemStack.of(Material.WOODEN_SPEAR, 1);
		Damageable data = (Damageable)spear.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.46, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		data.setAttributeModifiers(attributes);
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SPEAR");
		data.setDamage(damage);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		KineticWeapon.Builder kineticWeapon = KineticWeapon.kineticWeapon();
		kineticWeapon.damageMultiplier(0.7f);
		kineticWeapon.contactCooldownTicks(10);
		kineticWeapon.delayTicks(15);
		kineticWeapon.dismountConditions(KineticWeapon.condition(100, 14.0f, 0.0f));
		kineticWeapon.knockbackConditions(KineticWeapon.condition(200, 5.1f, 0.0f));
		kineticWeapon.damageConditions(KineticWeapon.condition(300, 0.0f, 4.6f));
		kineticWeapon.sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_WOOD_USE));
		kineticWeapon.hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_WOOD_HIT));
		spear.setItemMeta(data);
		spear.setData(DataComponentTypes.KINETIC_WEAPON, kineticWeapon);
		regenerateLore(spear);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(spear);
		return spear;
	}

	/**
	 * Creates a custom stone spear
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new stone spear
	 */
	@NotNull
	public static ItemStack getStoneSpear(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack spear = ItemStack.of(Material.STONE_SPEAR, 1);
		Damageable data = (Damageable)spear.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.67, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		data.setAttributeModifiers(attributes);
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SPEAR");
		data.setDamage(damage);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		KineticWeapon.Builder kineticWeapon = KineticWeapon.kineticWeapon();
		kineticWeapon.damageMultiplier(0.8f);
		kineticWeapon.contactCooldownTicks(10);
		kineticWeapon.delayTicks(14);
		kineticWeapon.dismountConditions(KineticWeapon.condition(90, 10.0f, 0.0f));
		kineticWeapon.knockbackConditions(KineticWeapon.condition(180, 5.1f, 0.0f));
		kineticWeapon.damageConditions(KineticWeapon.condition(275, 0.0f, 4.6f));
		kineticWeapon.sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_USE));
		kineticWeapon.hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_HIT));
		spear.setItemMeta(data);
		spear.setData(DataComponentTypes.KINETIC_WEAPON, kineticWeapon);
		regenerateLore(spear);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(spear);
		return spear;
	}

	/**
	 * Creates a custom copper spear
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new copper spear
	 */
	@NotNull
	public static ItemStack getCopperSpear(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack spear = ItemStack.of(Material.COPPER_SPEAR, 1);
		Damageable data = (Damageable)spear.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 2.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.82, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		data.setAttributeModifiers(attributes);
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SPEAR");
		data.setDamage(damage);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		KineticWeapon.Builder kineticWeapon = KineticWeapon.kineticWeapon();
		kineticWeapon.damageMultiplier(0.85f);
		kineticWeapon.contactCooldownTicks(10);
		kineticWeapon.delayTicks(13);
		kineticWeapon.dismountConditions(KineticWeapon.condition(80, 9.0f, 0.0f));
		kineticWeapon.knockbackConditions(KineticWeapon.condition(165, 5.1f, 0.0f));
		kineticWeapon.damageConditions(KineticWeapon.condition(250, 0.0f, 4.6f));
		kineticWeapon.sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_USE));
		kineticWeapon.hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_HIT));
		spear.setItemMeta(data);
		spear.setData(DataComponentTypes.KINETIC_WEAPON, kineticWeapon);
		regenerateLore(spear);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(spear);
		return spear;
	}

	/**
	 * Creates a custom iron spear
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new iron spear
	 */
	@NotNull
	public static ItemStack getIronSpear(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack spear = ItemStack.of(Material.IRON_SPEAR, 1);
		Damageable data = (Damageable)spear.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.95, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		data.setAttributeModifiers(attributes);
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SPEAR");
		data.setDamage(damage);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		KineticWeapon.Builder kineticWeapon = KineticWeapon.kineticWeapon();
		kineticWeapon.damageMultiplier(0.9f);
		kineticWeapon.contactCooldownTicks(10);
		kineticWeapon.delayTicks(12);
		kineticWeapon.dismountConditions(KineticWeapon.condition(50, 8.0f, 0.0f));
		kineticWeapon.knockbackConditions(KineticWeapon.condition(135, 5.1f, 0.0f));
		kineticWeapon.damageConditions(KineticWeapon.condition(225, 0.0f, 4.6f));
		kineticWeapon.sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_USE));
		kineticWeapon.hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_HIT));
		spear.setItemMeta(data);
		spear.setData(DataComponentTypes.KINETIC_WEAPON, kineticWeapon);
		regenerateLore(spear);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(spear);
		return spear;
	}

	/**
	 * Creates a custom golden spear
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new golden spear
	 */
	@NotNull
	public static ItemStack getGoldenSpear(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack spear = ItemStack.of(Material.GOLDEN_SPEAR, 1);
		Damageable data = (Damageable)spear.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.95, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		data.setAttributeModifiers(attributes);
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "GOLDEN_SPEAR");
		data.setDamage(damage);
		data.setMaxDamage(120);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		KineticWeapon.Builder kineticWeapon = KineticWeapon.kineticWeapon();
		kineticWeapon.damageMultiplier(0.9f);
		kineticWeapon.contactCooldownTicks(10);
		kineticWeapon.delayTicks(12);
		kineticWeapon.dismountConditions(KineticWeapon.condition(50, 8.0f, 0.0f));
		kineticWeapon.knockbackConditions(KineticWeapon.condition(135, 5.1f, 0.0f));
		kineticWeapon.damageConditions(KineticWeapon.condition(225, 0.0f, 4.6f));
		kineticWeapon.sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_USE));
		kineticWeapon.hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_HIT));
		spear.setItemMeta(data);
		spear.setData(DataComponentTypes.KINETIC_WEAPON, kineticWeapon);
		regenerateLore(spear);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(spear);
		return spear;
	}

	/**
	 * Creates a custom diamond spear
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new golden spear
	 */
	@NotNull
	public static ItemStack getDiamondSpear(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack spear = ItemStack.of(Material.DIAMOND_SPEAR, 1);
		Damageable data = (Damageable)spear.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.05, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		data.setAttributeModifiers(attributes);
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SPEAR");
		data.setDamage(damage);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		KineticWeapon.Builder kineticWeapon = KineticWeapon.kineticWeapon();
		kineticWeapon.damageMultiplier(1.05f);
		kineticWeapon.contactCooldownTicks(10);
		kineticWeapon.delayTicks(10);
		kineticWeapon.dismountConditions(KineticWeapon.condition(60, 7.5f, 0.0f));
		kineticWeapon.knockbackConditions(KineticWeapon.condition(130, 5.1f, 0.0f));
		kineticWeapon.damageConditions(KineticWeapon.condition(200, 0.0f, 4.6f));
		kineticWeapon.sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_USE));
		kineticWeapon.hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_HIT));
		spear.setItemMeta(data);
		spear.setData(DataComponentTypes.KINETIC_WEAPON, kineticWeapon);
		regenerateLore(spear);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(spear);
		return spear;
	}

	/**
	 * Creates a custom netherite spear
	 * @param level level of spear (1-5)
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new netherite spear
	 */
	@NotNull
	public static ItemStack getNetheriteSpear(int level, int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack spear = ItemStack.of(Material.NETHERITE_SPEAR, 1);
		Damageable data = (Damageable)spear.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 4.75 + (0.25 * level), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.13, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		data.setAttributeModifiers(attributes);
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SPEAR");
		dataP.set(itemLevel, PersistentDataType.INTEGER, level);
		data.setDamage(damage);
		data.setMaxDamage(2000 + (100 * level));
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		KineticWeapon.Builder kineticWeapon = KineticWeapon.kineticWeapon();
		kineticWeapon.damageMultiplier(1.2f);
		kineticWeapon.contactCooldownTicks(10);
		kineticWeapon.delayTicks(10);
		kineticWeapon.dismountConditions(KineticWeapon.condition(50, 7.0f, 0.0f));
		kineticWeapon.knockbackConditions(KineticWeapon.condition(110, 5.1f, 0.0f));
		kineticWeapon.damageConditions(KineticWeapon.condition(175, 0.0f, 4.6f));
		kineticWeapon.sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_USE));
		kineticWeapon.hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_HIT));
		spear.setItemMeta(data);
		spear.setData(DataComponentTypes.KINETIC_WEAPON, kineticWeapon);
		regenerateLore(spear);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(spear);
		return spear;
	}

	/**
	 * Creates a custom wooden axe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new wooden axe
	 */
	@NotNull
	public static ItemStack getWoodenAxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack axe = ItemStack.of(Material.WOODEN_AXE, 1);
		Damageable data = (Damageable)axe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "AXE");
		data.setDamage(damage);
		data.lore(getAxeLore(axe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		axe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(axe);
		return axe;
	}

	/**
	 * Creates a custom stone axe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new stone axe
	 */
	@NotNull
	public static ItemStack getStoneAxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack axe = ItemStack.of(Material.STONE_AXE, 1);
		Damageable data = (Damageable)axe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 7, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "AXE");
		data.setDamage(damage);
		data.lore(getAxeLore(axe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		axe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(axe);
		return axe;
	}

	/**
	 * Creates a custom copper axe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new copper axe
	 */
	@NotNull
	public static ItemStack getCopperAxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack axe = ItemStack.of(Material.COPPER_AXE, 1);
		Damageable data = (Damageable)axe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 7.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "AXE");
		data.setDamage(damage);
		axe.setItemMeta(data);
		data.lore(getAxeLore(axe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		axe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(axe);
		return axe;
	}

	/**
	 * Creates a custom iron axe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new iron axe
	 */
	@NotNull
	public static ItemStack getIronAxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack axe = ItemStack.of(Material.IRON_AXE, 1);
		Damageable data = (Damageable)axe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "AXE");
		data.setDamage(damage);
		axe.setItemMeta(data);
		data.lore(getAxeLore(axe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		axe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(axe);
		return axe;
	}

	/**
	 * Creates a custom golden axe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new golden axe
	 */
	@NotNull
	public static ItemStack getGoldenAxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack axe = ItemStack.of(Material.GOLDEN_AXE, 1);
		Damageable data = (Damageable)axe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "GOLDEN_AXE");
		data.setDamage(damage);
		data.setMaxDamage(120);
		axe.setItemMeta(data);
		data.lore(getAxeLore(axe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		axe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(axe);
		return axe;
	}

	/**
	 * Creates a custom diamond axe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new diamond axe
	 */
	@NotNull
	public static ItemStack getDiamondAxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack axe = ItemStack.of(Material.DIAMOND_AXE, 1);
		Damageable data = (Damageable)axe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "AXE");
		data.setDamage(damage);
		axe.setItemMeta(data);
		data.lore(getAxeLore(axe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		axe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(axe);
		return axe;
	}

	/**
	 * Creates a custom netherite axe
	 * @param level level of axe (1-5)
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new netherite axe
	 */
	@NotNull
	public static ItemStack getNetheriteAxe(int level, int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack axe = ItemStack.of(Material.NETHERITE_AXE, 1);
		Damageable data = (Damageable)axe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 8.75 + (0.25 * level), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "AXE");
		dataP.set(itemLevel, PersistentDataType.INTEGER, level);
		data.setDamage(damage);
		data.setMaxDamage(2000 + (100 * level));
		axe.setItemMeta(data);
		data.lore(getAxeLore(axe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		axe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(axe);
		return axe;
	}

	/**
	 * Creates a custom wooden pickaxe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new wooden pickaxe
	 */
	@NotNull
	public static ItemStack getWoodenPickaxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack pickaxe = ItemStack.of(Material.WOODEN_PICKAXE, 1);
		Damageable data = (Damageable)pickaxe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "PICKAXE");
		data.setDamage(damage);
		data.lore(getPickaxeLore(pickaxe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		pickaxe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(pickaxe);
		return pickaxe;
	}

	/**
	 * Creates a custom stone pickaxe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new stone pickaxe
	 */
	@NotNull
	public static ItemStack getStonePickaxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack pickaxe = ItemStack.of(Material.STONE_PICKAXE, 1);
		Damageable data = (Damageable)pickaxe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "PICKAXE");
		data.setDamage(damage);
		data.lore(getPickaxeLore(pickaxe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		pickaxe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(pickaxe);
		return pickaxe;
	}

	/**
	 * Creates a custom copper pickaxe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new copper pickaxe
	 */
	@NotNull
	public static ItemStack getCopperPickaxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack pickaxe = ItemStack.of(Material.COPPER_PICKAXE, 1);
		Damageable data = (Damageable)pickaxe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 2.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "PICKAXE");
		data.setDamage(damage);
		ToolComponent tool = data.getTool();
		tool.setDefaultMiningSpeed(5.0f);
		tool.addRule(Tag.INCORRECT_FOR_IRON_TOOL, null, false);
		tool.addRule(Tag.MINEABLE_PICKAXE, null, true);
		tool.setDamagePerBlock(1);
		data.setTool(tool);
		pickaxe.setItemMeta(data);
		data.lore(getPickaxeLore(pickaxe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		pickaxe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(pickaxe);
		return pickaxe;
	}

	/**
	 * Creates a custom iron pickaxe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new iron pickaxe
	 */
	@NotNull
	public static ItemStack getIronPickaxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack pickaxe = ItemStack.of(Material.IRON_PICKAXE, 1);
		Damageable data = (Damageable)pickaxe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "PICKAXE");
		data.setDamage(damage);
		pickaxe.setItemMeta(data);
		data.lore(getPickaxeLore(pickaxe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		pickaxe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(pickaxe);
		return pickaxe;
	}

	/**
	 * Creates a custom golden pickaxe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new golden pickaxe
	 */
	@NotNull
	public static ItemStack getGoldenPickaxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack pickaxe = ItemStack.of(Material.GOLDEN_PICKAXE, 1);
		Damageable data = (Damageable)pickaxe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "GOLDEN_PICKAXE");
		data.setDamage(damage);
		data.setMaxDamage(120);
		ToolComponent tool = data.getTool();
		tool.setDefaultMiningSpeed(12.0f);
		tool.addRule(Tag.INCORRECT_FOR_IRON_TOOL, null, false);
		tool.addRule(Tag.MINEABLE_PICKAXE, null, true);
		tool.setDamagePerBlock(1);
		data.setTool(tool);
		pickaxe.setItemMeta(data);
		data.lore(getPickaxeLore(pickaxe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		pickaxe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(pickaxe);
		return pickaxe;
	}

	/**
	 * Creates a custom diamond pickaxe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new diamond pickaxe
	 */
	@NotNull
	public static ItemStack getDiamondPickaxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack pickaxe = ItemStack.of(Material.DIAMOND_PICKAXE, 1);
		Damageable data = (Damageable)pickaxe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "PICKAXE");
		data.setDamage(damage);
		pickaxe.setItemMeta(data);
		data.lore(getPickaxeLore(pickaxe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		pickaxe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(pickaxe);
		return pickaxe;
	}

	/**
	 * Creates a custom netherite pickaxe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new netherite pickaxe
	 */
	@NotNull
	public static ItemStack getNetheritePickaxe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack pickaxe = ItemStack.of(Material.NETHERITE_PICKAXE, 1);
		Damageable data = (Damageable)pickaxe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 5.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -2.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "PICKAXE");
		data.setDamage(damage);
		data.setMaxDamage(2100);
		pickaxe.setItemMeta(data);
		data.lore(getPickaxeLore(pickaxe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		pickaxe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(pickaxe);
		return pickaxe;
	}

	/**
	 * Creates a custom wooden shovel
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new wooden shovel
	 */
	@NotNull
	public static ItemStack getWoodenShovel(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack shovel = ItemStack.of(Material.WOODEN_SHOVEL, 1);
		Damageable data = (Damageable)shovel.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 1.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SHOVEL");
		data.setDamage(damage);
		data.lore(getShovelLore(shovel));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		shovel.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(shovel);
		return shovel;
	}

	/**
	 * Creates a custom stone shovel
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new stone shovel
	 */
	@NotNull
	public static ItemStack getStoneShovel(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack shovel = ItemStack.of(Material.STONE_SHOVEL, 1);
		Damageable data = (Damageable)shovel.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 2.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SHOVEL");
		data.setDamage(damage);
		data.lore(getShovelLore(shovel));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		shovel.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(shovel);
		return shovel;
	}

	/**
	 * Creates a custom copper shovel
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new copper shovel
	 */
	@NotNull
	public static ItemStack getCopperShovel(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack shovel = ItemStack.of(Material.COPPER_SHOVEL, 1);
		Damageable data = (Damageable)shovel.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SHOVEL");
		data.setDamage(damage);
		shovel.setItemMeta(data);
		data.lore(getShovelLore(shovel));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		shovel.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(shovel);
		return shovel;
	}

	/**
	 * Creates a custom iron shovel
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new iron shovel
	 */
	@NotNull
	public static ItemStack getIronShovel(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack shovel = ItemStack.of(Material.IRON_SHOVEL, 1);
		Damageable data = (Damageable)shovel.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 3.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SHOVEL");
		data.setDamage(damage);
		shovel.setItemMeta(data);
		data.lore(getShovelLore(shovel));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		shovel.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(shovel);
		return shovel;
	}

	/**
	 * Creates a custom golden shovel
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new golden shovel
	 */
	@NotNull
	public static ItemStack getGoldenShovel(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack shovel = ItemStack.of(Material.GOLDEN_SHOVEL, 1);
		Damageable data = (Damageable)shovel.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 3.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "GOLDEN_SHOVEL");
		data.setDamage(damage);
		data.setMaxDamage(120);
		shovel.setItemMeta(data);
		data.lore(getShovelLore(shovel));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		shovel.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(shovel);
		return shovel;
	}

	/**
	 * Creates a custom diamond shovel
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new diamond shovel
	 */
	@NotNull
	public static ItemStack getDiamondShovel(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack shovel = ItemStack.of(Material.DIAMOND_SHOVEL, 1);
		Damageable data = (Damageable)shovel.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 4.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SHOVEL");
		data.setDamage(damage);
		shovel.setItemMeta(data);
		data.lore(getShovelLore(shovel));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		shovel.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(shovel);
		return shovel;
	}

	/**
	 * Creates a custom netherite shovel
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new netherite shovel
	 */
	@NotNull
	public static ItemStack getNetheriteShovel(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack shovel = ItemStack.of(Material.NETHERITE_SHOVEL, 1);
		Damageable data = (Damageable)shovel.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 5.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SHOVEL");
		data.setDamage(damage);
		data.setMaxDamage(2100);
		shovel.setItemMeta(data);
		data.lore(getShovelLore(shovel));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		shovel.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(shovel);
		return shovel;
	}

	/**
	 * Creates a custom wooden hoe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new wooden hoe
	 */
	@NotNull
	public static ItemStack getWoodenHoe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack hoe = ItemStack.of(Material.WOODEN_HOE, 1);
		Damageable data = (Damageable)hoe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HOE");
		data.setDamage(damage);
		data.lore(getHoeLore(hoe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		hoe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(hoe);
		return hoe;
	}

	/**
	 * Creates a custom stone hoe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new stone hoe
	 */
	@NotNull
	public static ItemStack getStoneHoe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack hoe = ItemStack.of(Material.STONE_HOE, 1);
		Damageable data = (Damageable)hoe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HOE");
		data.setDamage(damage);
		data.lore(getHoeLore(hoe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		hoe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(hoe);
		return hoe;
	}

	/**
	 * Creates a custom copper hoe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new copper hoe
	 */
	@NotNull
	public static ItemStack getCopperHoe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack hoe = ItemStack.of(Material.COPPER_HOE, 1);
		Damageable data = (Damageable)hoe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 3.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HOE");
		data.setDamage(damage);
		hoe.setItemMeta(data);
		data.lore(getHoeLore(hoe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		hoe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(hoe);
		return hoe;
	}

	/**
	 * Creates a custom iron hoe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new iron hoe
	 */
	@NotNull
	public static ItemStack getIronHoe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack hoe = ItemStack.of(Material.IRON_HOE, 1);
		Damageable data = (Damageable)hoe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HOE");
		data.setDamage(damage);
		hoe.setItemMeta(data);
		data.lore(getHoeLore(hoe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		hoe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(hoe);
		return hoe;
	}

	/**
	 * Creates a custom golden hoe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new steel hoe
	 */
	@NotNull
	public static ItemStack getGoldenHoe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack hoe = ItemStack.of(Material.GOLDEN_HOE, 1);
		Damageable data = (Damageable)hoe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "GOLDEN_HOE");
		data.setDamage(damage);
		data.setMaxDamage(120);
		hoe.setItemMeta(data);
		data.lore(getHoeLore(hoe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		hoe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(hoe);
		return hoe;
	}

	/**
	 * Creates a custom diamond hoe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new diamond hoe
	 */
	@NotNull
	public static ItemStack getDiamondHoe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack hoe = ItemStack.of(Material.DIAMOND_HOE, 1);
		Damageable data = (Damageable)hoe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 5.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HOE");
		data.setDamage(damage);
		hoe.setItemMeta(data);
		data.lore(getHoeLore(hoe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		hoe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(hoe);
		return hoe;
	}

	/**
	 * Creates a custom netherite hoe
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new diamond hoe
	 */
	@NotNull
	public static ItemStack getNetheriteHoe(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack hoe = ItemStack.of(Material.NETHERITE_HOE, 1);
		Damageable data = (Damageable)hoe.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HOE");
		data.setDamage(damage);
		data.setMaxDamage(2100);
		hoe.setItemMeta(data);
		data.lore(getHoeLore(hoe));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		hoe.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(hoe);
		return hoe;
	}

	/**
	 * Creates a custom leather cap
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new leather cap
	 */
	@NotNull
	public static ItemStack getLeatherCap(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack cap = ItemStack.of(Material.LEATHER_HELMET, 1);
		Damageable data = (Damageable)cap.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HELMET");
		data.setDamage(damage);
		data.lore(getHelmetLore(cap));
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		cap.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(cap);
		return cap;
	}

	/**
	 * Creates a custom copper helmet
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new copper helmet
	 */
	@NotNull
	public static ItemStack getCopperHelmet(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack helmet = ItemStack.of(Material.COPPER_HELMET, 1);
		Damageable data = (Damageable)helmet.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorHead, 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessHead, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HELMET");
		data.setDamage(damage);
		helmet.setItemMeta(data);
		data.lore(getHelmetLore(helmet));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmet.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(helmet);
		return helmet;
	}

	/**
	 * Creates a custom chainmail helmet
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new chainmail helmet
	 */
	@NotNull
	public static ItemStack getChainmailHelmet(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack helmet = ItemStack.of(Material.CHAINMAIL_HELMET, 1);
		Damageable data = (Damageable)helmet.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorHead, 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessHead, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HELMET");
		data.setDamage(damage);
		helmet.setItemMeta(data);
		data.lore(getHelmetLore(helmet));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmet.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(helmet);
		return helmet;
	}

	/**
	 * Creates a custom iron helmet
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new iron helmet
	 */
	@NotNull
	public static ItemStack getIronHelmet(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack helmet = ItemStack.of(Material.IRON_HELMET, 1);
		Damageable data = (Damageable)helmet.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorHead, 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessHead, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HELMET");
		data.setDamage(damage);
		helmet.setItemMeta(data);
		data.lore(getHelmetLore(helmet));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmet.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(helmet);
		return helmet;
	}

	/**
	 * Creates a custom turtle helmet
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new turtle helmet
	 */
	@NotNull
	public static ItemStack getTurtleHelmet(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack helmet = ItemStack.of(Material.TURTLE_HELMET, 1);
		Damageable data = (Damageable)helmet.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorHead, 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessHead, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HELMET");
		data.setDamage(damage);
		helmet.setItemMeta(data);
		data.lore(getHelmetLore(helmet));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmet.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(helmet);
		return helmet;
	}

	/**
	 * Creates a custom golden helmet
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new golden helmet
	 */
	@NotNull
	public static ItemStack getGoldenHelmet(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack helmet = ItemStack.of(Material.GOLDEN_HELMET, 1);
		Damageable data = (Damageable)helmet.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorHead, 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessHead, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "GOLDEN_HELMET");
		data.setDamage(damage);
		data.setMaxDamage(99);
		helmet.setItemMeta(data);
		data.lore(getHelmetLore(helmet));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmet.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(helmet);
		return helmet;
	}

	/**
	 * Creates a custom diamond helmet
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new diamond helmet
	 */
	@NotNull
	public static ItemStack getDiamondHelmet(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack helmet = ItemStack.of(Material.DIAMOND_HELMET, 1);
		Damageable data = (Damageable)helmet.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HELMET");
		data.setDamage(damage);
		helmet.setItemMeta(data);
		data.lore(getHelmetLore(helmet));
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmet.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(helmet);
		return helmet;
	}

	/**
	 * Creates a custom netherite helmet
	 * @param level level of helmet (1-5)
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new netherite helmet
	 */
	@NotNull
	public static ItemStack getNetheriteHelmet(int level, int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack helmet = ItemStack.of(Material.NETHERITE_HELMET, 1);
		Damageable data = (Damageable)helmet.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorHead, 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessHead, 2.875 + (0.125 * level), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		attributes.put(Attribute.KNOCKBACK_RESISTANCE, new AttributeModifier(attributeKBResHead, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "HELMET");
		dataP.set(itemLevel, PersistentDataType.INTEGER, level);
		data.setDamage(damage);
		data.setMaxDamage(396 + (11 * level));
		helmet.setItemMeta(data);
		data.lore(getHelmetLore(helmet));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmet.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(helmet);
		return helmet;
	}

	/**
	 * Creates a custom leather tunic
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new leather tunic
	 */
	@NotNull
	public static ItemStack getLeatherTunic(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack tunic = ItemStack.of(Material.LEATHER_CHESTPLATE, 1);
		Damageable data = (Damageable)tunic.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "CHESTPLATE");
		data.setDamage(damage);
		tunic.setItemMeta(data);
		data.lore(getChestplateLore(tunic));
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		tunic.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(tunic);
		return tunic;
	}

	/**
	 * Creates a custom copper chestplate
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new copper chestplate
	 */
	@NotNull
	public static ItemStack getCopperChestplate(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack chestplate = ItemStack.of(Material.COPPER_CHESTPLATE, 1);
		Damageable data = (Damageable)chestplate.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorChest, 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessChest, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "CHESTPLATE");
		data.setDamage(damage);
		chestplate.setItemMeta(data);
		data.lore(getChestplateLore(chestplate));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestplate.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(chestplate);
		return chestplate;
	}

	/**
	 * Creates a custom chainmail chestplate
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new chainmail chestplate
	 */
	@NotNull
	public static ItemStack getChainmailChesplate(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack chestplate = ItemStack.of(Material.CHAINMAIL_CHESTPLATE, 1);
		Damageable data = (Damageable)chestplate.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorChest, 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessChest, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "CHESTPLATE");
		data.setDamage(damage);
		chestplate.setItemMeta(data);
		data.lore(getChestplateLore(chestplate));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestplate.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(chestplate);
		return chestplate;
	}

	/**
	 * Creates a custom iron chestplate
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new iron chestplate
	 */
	@NotNull
	public static ItemStack getIronChestplate(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack chestplate = ItemStack.of(Material.IRON_CHESTPLATE, 1);
		Damageable data = (Damageable)chestplate.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorChest, 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessChest, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "CHESTPLATE");
		data.setDamage(damage);
		chestplate.setItemMeta(data);
		data.lore(getChestplateLore(chestplate));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestplate.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(chestplate);
		return chestplate;
	}

	/**
	 * Creates a custom golden chestplate
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new golden chestplate
	 */
	@NotNull
	public static ItemStack getGoldenChestplate(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack chestplate = ItemStack.of(Material.GOLDEN_CHESTPLATE, 1);
		Damageable data = (Damageable)chestplate.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorChest, 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessChest, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "GOLDEN_CHESTPLATE");
		data.setDamage(damage);
		data.setMaxDamage(144);
		chestplate.setItemMeta(data);
		data.lore(getChestplateLore(chestplate));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestplate.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(chestplate);
		return chestplate;
	}

	/**
	 * Creates a custom diamond chestplate
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new diamond chestplate
	 */
	@NotNull
	public static ItemStack getDiamondChestplate(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack chestplate = ItemStack.of(Material.DIAMOND_CHESTPLATE, 1);
		Damageable data = (Damageable)chestplate.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "CHESTPLATE");
		data.setDamage(damage);
		chestplate.setItemMeta(data);
		data.lore(getChestplateLore(chestplate));
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestplate.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(chestplate);
		return chestplate;
	}

	/**
	 * Creates a custom netherite chestplate
	 * @param level level of chestplate (1-5)
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new netherite chestplate
	 */
	@NotNull
	public static ItemStack getNetheriteChestplate(int level, int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack chestplate = ItemStack.of(Material.NETHERITE_CHESTPLATE, 1);
		Damageable data = (Damageable)chestplate.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorChest, 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessChest, 2.875 + (0.125 * level), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST));
		attributes.put(Attribute.KNOCKBACK_RESISTANCE, new AttributeModifier(attributeKBResChest, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "CHESTPLATE");
		dataP.set(itemLevel, PersistentDataType.INTEGER, level);
		data.setDamage(damage);
		data.setMaxDamage(576 + (16 * level));
		chestplate.setItemMeta(data);
		data.lore(getChestplateLore(chestplate));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestplate.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(chestplate);
		return chestplate;
	}

	/**
	 * Creates a custom leather pants
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new leather pants
	 */
	@NotNull
	public static ItemStack getLeatherPants(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack pants = ItemStack.of(Material.LEATHER_LEGGINGS, 1);
		Damageable data = (Damageable)pants.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "LEGGINGS");
		data.setDamage(damage);
		pants.setItemMeta(data);
		data.lore(getLeggingsLore(pants));
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		pants.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(pants);
		return pants;
	}

	/**
	 * Creates a custom copper leggings
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new copper leggings
	 */
	@NotNull
	public static ItemStack getCopperLeggings(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack leggings = ItemStack.of(Material.COPPER_LEGGINGS, 1);
		Damageable data = (Damageable)leggings.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorLegs, 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessLegs, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "LEGGINGS");
		data.setDamage(damage);
		leggings.setItemMeta(data);
		data.lore(getLeggingsLore(leggings));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		leggings.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(leggings);
		return leggings;
	}

	/**
	 * Creates a custom chainmail leggings
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new chainmail leggings
	 */
	@NotNull
	public static ItemStack getChainmailLeggings(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack leggings = ItemStack.of(Material.CHAINMAIL_LEGGINGS, 1);
		Damageable data = (Damageable)leggings.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorLegs, 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessLegs, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "LEGGINGS");
		data.setDamage(damage);
		leggings.setItemMeta(data);
		data.lore(getLeggingsLore(leggings));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		leggings.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(leggings);
		return leggings;
	}

	/**
	 * Creates a custom iron leggings
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new iron leggings
	 */
	@NotNull
	public static ItemStack getIronLeggings(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack leggings = ItemStack.of(Material.IRON_LEGGINGS, 1);
		Damageable data = (Damageable)leggings.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorLegs, 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessLegs, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "LEGGINGS");
		data.setDamage(damage);
		leggings.setItemMeta(data);
		data.lore(getLeggingsLore(leggings));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		leggings.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(leggings);
		return leggings;
	}

	/**
	 * Creates a custom golden leggings
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new golden leggings
	 */
	@NotNull
	public static ItemStack getGoldenLeggings(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack leggings = ItemStack.of(Material.GOLDEN_LEGGINGS, 1);
		Damageable data = (Damageable)leggings.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorLegs, 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessLegs, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "GOLDEN_LEGGINGS");
		data.setDamage(damage);
		data.setMaxDamage(135);
		leggings.setItemMeta(data);
		data.lore(getLeggingsLore(leggings));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		leggings.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(leggings);
		return leggings;
	}

	/**
	 * Creates a custom diamond leggings
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new diamond leggings
	 */
	@NotNull
	public static ItemStack getDiamondLeggings(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack leggings = ItemStack.of(Material.DIAMOND_LEGGINGS, 1);
		Damageable data = (Damageable)leggings.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "LEGGINGS");
		data.setDamage(damage);
		leggings.setItemMeta(data);
		data.lore(getLeggingsLore(leggings));
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		leggings.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(leggings);
		return leggings;
	}

	/**
	 * Creates a custom netherite leggings
	 * @param level level of leggings (1-5)
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new netherite leggings
	 */
	@NotNull
	public static ItemStack getNetheriteLeggings(int level, int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack leggings = ItemStack.of(Material.NETHERITE_LEGGINGS, 1);
		Damageable data = (Damageable)leggings.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorLegs, 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessLegs, 2.875 + (0.125 * level), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
		attributes.put(Attribute.KNOCKBACK_RESISTANCE, new AttributeModifier(attributeKBResLegs, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "LEGGINGS");
		dataP.set(itemLevel, PersistentDataType.INTEGER, level);
		data.setDamage(damage);
		data.setMaxDamage(540 + (15 * level));
		leggings.setItemMeta(data);
		data.lore(getLeggingsLore(leggings));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		leggings.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(leggings);
		return leggings;
	}

	/**
	 * Creates a custom leather boots
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new leather boots
	 */
	@NotNull
	public static ItemStack getLeatherBoots(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack boots = ItemStack.of(Material.LEATHER_BOOTS, 1);
		Damageable data = (Damageable)boots.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "BOOTS");
		data.setDamage(damage);
		boots.setItemMeta(data);
		data.lore(getBootsLore(boots));
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		boots.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(boots);
		return boots;
	}

	/**
	 * Creates a custom copper boots
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new copper boots
	 */
	@NotNull
	public static ItemStack getCopperBoots(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack boots = ItemStack.of(Material.COPPER_BOOTS, 1);
		Damageable data = (Damageable)boots.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorFeet, 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessFeet, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "BOOTS");
		data.setDamage(damage);
		boots.setItemMeta(data);
		data.lore(getBootsLore(boots));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		boots.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(boots);
		return boots;
	}

	/**
	 * Creates a custom chainmail boots
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new chainmail boots
	 */
	@NotNull
	public static ItemStack getChainmailBoots(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack boots = ItemStack.of(Material.CHAINMAIL_BOOTS, 1);
		Damageable data = (Damageable)boots.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorFeet, 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessFeet, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "BOOTS");
		data.setDamage(damage);
		boots.setItemMeta(data);
		data.lore(getBootsLore(boots));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		boots.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(boots);
		return boots;
	}

	/**
	 * Creates a custom iron boots
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new iron boots
	 */
	@NotNull
	public static ItemStack getIronBoots(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack boots = ItemStack.of(Material.IRON_BOOTS, 1);
		Damageable data = (Damageable)boots.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorFeet, 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessFeet, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "BOOTS");
		data.setDamage(damage);
		boots.setItemMeta(data);
		data.lore(getBootsLore(boots));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		boots.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(boots);
		return boots;
	}

	/**
	 * Creates a custom golden boots
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new golden boots
	 */
	@NotNull
	public static ItemStack getGoldenBoots(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack boots = ItemStack.of(Material.GOLDEN_BOOTS, 1);
		Damageable data = (Damageable)boots.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorFeet, 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessFeet, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "GOLDEN_BOOTS");
		data.setDamage(damage);
		data.setMaxDamage(117);
		boots.setItemMeta(data);
		data.lore(getBootsLore(boots));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		boots.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(boots);
		return boots;
	}

	/**
	 * Creates a custom diamond boots
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new diamond boots
	 */
	@NotNull
	public static ItemStack getDiamondBoots(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack boots = ItemStack.of(Material.DIAMOND_BOOTS, 1);
		Damageable data = (Damageable)boots.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "BOOTS");
		data.setDamage(damage);
		boots.setItemMeta(data);
		data.lore(getBootsLore(boots));
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		boots.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(boots);
		return boots;
	}

	/**
	 * Creates a custom netherite boots
	 * @param level level of boots (1-5)
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new netherite boots
	 */
	@NotNull
	public static ItemStack getNetheriteBoots(int level, int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack boots = ItemStack.of(Material.NETHERITE_BOOTS, 1);
		Damageable data = (Damageable)boots.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ARMOR, new AttributeModifier(attributeArmorFeet, 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET));
		attributes.put(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(attributeToughnessFeet, 2.875 + (0.125 * level), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET));
		attributes.put(Attribute.KNOCKBACK_RESISTANCE, new AttributeModifier(attributeKBResFeet, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET));
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "BOOTS");
		dataP.set(itemLevel, PersistentDataType.INTEGER, level);
		data.setDamage(damage);
		data.setMaxDamage(468 + (13 * level));
		boots.setItemMeta(data);
		data.lore(getBootsLore(boots));
		data.setAttributeModifiers(attributes);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		boots.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(boots);
		return boots;
	}

	/**
	 * Creates a custom bow
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new bow
	 */
	@NotNull
	public static ItemStack getBow(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack bow = ItemStack.of(Material.BOW, 1);
		Damageable data = (Damageable)bow.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "BOW");
		data.setDamage(damage);
		data.lore(getBowLore(bow));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		bow.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(bow);
		return bow;
	}

	/**
	 * Creates a custom crossbow
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new bow
	 */
	@NotNull
	public static ItemStack getCrossbow(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack crossbow = ItemStack.of(Material.CROSSBOW, 1);
		Damageable data = (Damageable)crossbow.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "CROSSBOW");
		data.setDamage(damage);
		data.lore(getCrossbowLore(crossbow));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		crossbow.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(crossbow);
		return crossbow;
	}

	/**
	 * Creates a custom trident
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new trident
	 */
	@NotNull
	public static ItemStack getTrident(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack trident = ItemStack.of(Material.TRIDENT, 1);
		Damageable data = (Damageable)trident.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "TRIDENT");
		data.setDamage(damage);
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		trident.setItemMeta(data);
		regenerateLore(trident);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(trident);
		return trident;
	}

	/**
	 * Creates a custom mace
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new mace
	 */
	@NotNull
	public static ItemStack getMace(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack mace = ItemStack.of(Material.MACE, 1);
		Damageable data = (Damageable)mace.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		ArrayListMultimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
		attributes.put(Attribute.ATTACK_DAMAGE, new AttributeModifier(attributeDamage, 5.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		attributes.put(Attribute.ATTACK_SPEED, new AttributeModifier(attributeAttackSpeed, -3.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND));
		data.setAttributeModifiers(attributes);
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "MACE");
		data.setDamage(damage);
		data.lore(getMaceLore(mace));
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		mace.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(mace);
		return mace;
	}

	/**
	 * Creates a custom fishing rod
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new fishing rod
	 */
	@NotNull
	public static ItemStack getFishingRod(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack rod = ItemStack.of(Material.FISHING_ROD, 1);
		Damageable data = (Damageable)rod.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "FISHING_ROD");
		data.setDamage(damage);
		data.lore(getFishingRodLore(rod));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		rod.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(rod);
		return rod;
	}

	/**
	 * Creates a custom shears
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new shears
	 */
	@NotNull
	public static ItemStack getShears(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack shears = ItemStack.of(Material.SHEARS, 1);
		Damageable data = (Damageable)shears.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "SHEARS");
		data.setDamage(damage);
		data.lore(getShearsLore(shears));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		shears.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(shears);
		return shears;
	}

	/**
	 * Creates a custom elytra
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new elytra
	 */
	@NotNull
	public static ItemStack getElytra(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack elytra = ItemStack.of(Material.ELYTRA, 1);
		Damageable data = (Damageable)elytra.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "ELYTRA");
		data.setDamage(damage);
		data.lore(getElytraLore(elytra));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		elytra.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(elytra);
		return elytra;
	}

	/**
	 * Creates a custom wolf armor
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new wolf armor
	 */
	@NotNull
	public static ItemStack getWolfArmor(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack wolfArmor = ItemStack.of(Material.WOLF_ARMOR, 1);
		Damageable data = (Damageable)wolfArmor.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "WOLF_ARMOR");
		data.setDamage(damage);
		data.lore(getWolfArmorLore(wolfArmor));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		wolfArmor.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(wolfArmor);
		return wolfArmor;
	}

	/**
	 * Creates a custom brush
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new brush
	 */
	@NotNull
	public static ItemStack getBrush(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack brush = ItemStack.of(Material.BRUSH, 1);
		Damageable data = (Damageable)brush.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "TOOL");
		data.setDamage(damage);
		data.lore(getToolLore(brush));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		brush.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(brush);
		return brush;
	}

	/**
	 * Creates a custom carrot on a stick
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new carrot on a stick
	 */
	@NotNull
	public static ItemStack getCarrotOnAStick(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack rod = ItemStack.of(Material.CARROT_ON_A_STICK, 1);
		Damageable data = (Damageable)rod.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "TOOL");
		data.setDamage(damage);
		data.lore(getToolLore(rod));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		rod.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(rod);
		return rod;
	}

	/**
	 * Creates a custom flint and steel
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new flint and steel
	 */
	@NotNull
	public static ItemStack getFlintAndSteel(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack flintAndSteel = ItemStack.of(Material.FLINT_AND_STEEL, 1);
		Damageable data = (Damageable)flintAndSteel.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "TOOL");
		data.setDamage(damage);
		data.lore(getToolLore(flintAndSteel));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		flintAndSteel.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(flintAndSteel);
		return flintAndSteel;
	}

	/**
	 * Creates a custom shield
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new shield
	 */
	@NotNull
	public static ItemStack getShield(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack shield = ItemStack.of(Material.SHIELD, 1);
		Damageable data = (Damageable)shield.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "TOOL");
		data.setDamage(damage);
		data.lore(getToolLore(shield));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		shield.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(shield);
		return shield;
	}

	/**
	 * Creates a custom warped fungus on a stick
	 * @param damage how much damage is already on item
	 * @param enchantments enchantments to add to this item
	 * @return new warped fungus on a stick
	 */
	@NotNull
	public static ItemStack getWarpedFungusOnAStick(int damage, @NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack rod = ItemStack.of(Material.WARPED_FUNGUS_ON_A_STICK, 1);
		Damageable data = (Damageable)rod.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "TOOL");
		data.setDamage(damage);
		data.lore(getToolLore(rod));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		rod.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(rod);
		return rod;
	}

	/**
	 * Creates a custom carved pumpkin
	 * @param enchantments enchantments to add to this item
	 * @param amount stack size
	 * @return new carved pumpkin
	 */
	@NotNull
	public static ItemStack getCarvedPumpkin(@NotNull ArrayList<EquipEnchantment> enchantments, int amount) {
		ItemStack pumpkin = ItemStack.of(Material.CARVED_PUMPKIN, amount);
		ItemMeta data = pumpkin.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "WEARABLE");
		data.lore(getWearableLore(pumpkin));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		pumpkin.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(pumpkin);
		return pumpkin;
	}

	/**
	 * Creates a custom creeper head
	 * @param enchantments enchantments to add to this item
	 * @param amount stack size
	 * @return new head
	 */
	@NotNull
	public static ItemStack getCreeperHead(@NotNull ArrayList<EquipEnchantment> enchantments, int amount) {
		ItemStack head = ItemStack.of(Material.CREEPER_HEAD, amount);
		ItemMeta data = head.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "WEARABLE");
		data.lore(getWearableLore(head));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		head.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(head);
		return head;
	}

	/**
	 * Creates a custom dragon head
	 * @param enchantments enchantments to add to this item
	 * @param amount stack size
	 * @return new head
	 */
	@NotNull
	public static ItemStack getDragonHead(@NotNull ArrayList<EquipEnchantment> enchantments, int amount) {
		ItemStack head = ItemStack.of(Material.DRAGON_HEAD, amount);
		ItemMeta data = head.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "WEARABLE");
		data.lore(getWearableLore(head));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		head.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(head);
		return head;
	}

	/**
	 * Creates a custom piglin head
	 * @param enchantments enchantments to add to this item
	 * @param amount stack size
	 * @return new head
	 */
	@NotNull
	public static ItemStack getPiglinHead(@NotNull ArrayList<EquipEnchantment> enchantments, int amount) {
		ItemStack head = ItemStack.of(Material.PIGLIN_HEAD, amount);
		ItemMeta data = head.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "WEARABLE");
		data.lore(getWearableLore(head));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		head.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(head);
		return head;
	}

	/**
	 * Creates a custom player head
	 * @param enchantments enchantments to add to this item
	 * @param amount stack size
	 * @return new head
	 */
	@NotNull
	public static ItemStack getPlayerHead(@NotNull ArrayList<EquipEnchantment> enchantments, int amount) {
		ItemStack head = ItemStack.of(Material.PLAYER_HEAD, amount);
		ItemMeta data = head.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "WEARABLE");
		data.lore(getWearableLore(head));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		head.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(head);
		return head;
	}

	/**
	 * Creates a custom skeleton skull
	 * @param enchantments enchantments to add to this item
	 * @param amount stack size
	 * @return new skull
	 */
	@NotNull
	public static ItemStack getSkeletonSkull(@NotNull ArrayList<EquipEnchantment> enchantments, int amount) {
		ItemStack head = ItemStack.of(Material.SKELETON_SKULL, amount);
		ItemMeta data = head.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "WEARABLE");
		data.lore(getWearableLore(head));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		head.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(head);
		return head;
	}

	/**
	 * Creates a custom wither skeleton skull
	 * @param enchantments enchantments to add to this item
	 * @param amount stack size
	 * @return new skull
	 */
	@NotNull
	public static ItemStack getWitherSkeletonSkull(@NotNull ArrayList<EquipEnchantment> enchantments, int amount) {
		ItemStack head = ItemStack.of(Material.WITHER_SKELETON_SKULL, amount);
		ItemMeta data = head.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "WEARABLE");
		data.lore(getWearableLore(head));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		head.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(head);
		return head;
	}

	/**
	 * Creates a custom zombie head
	 * @param enchantments enchantments to add to this item
	 * @param amount stack size
	 * @return new head
	 */
	@NotNull
	public static ItemStack getZombieHead(@NotNull ArrayList<EquipEnchantment> enchantments, int amount) {
		ItemStack head = ItemStack.of(Material.ZOMBIE_HEAD, amount);
		ItemMeta data = head.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "WEARABLE");
		data.lore(getWearableLore(head));
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		head.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(head);
		return head;
	}

	/**
	 * Creates a custom compass
	 * @param enchantments enchantments to add to this item
	 * @param amount stack size
	 * @param lodestone lodestone location of this compass
	 * @return new compass
	 */
	@NotNull
	public static ItemStack getCompass(@NotNull ArrayList<EquipEnchantment> enchantments, int amount, @Nullable Location lodestone) {
		ItemStack compass = ItemStack.of(Material.COMPASS, amount);
		CompassMeta data = (CompassMeta)compass.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "VANISHABLE");
		data.lore(getVanishableLore(compass));
		if (lodestone != null) {
			data.setLodestoneTracked(true);
			data.setLodestone(lodestone);
		}
		data.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		compass.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(compass);
		return compass;
	}

	/**
	 * Creates a custom enchanted book
	 * @param enchantments enchantments to add to this item
	 * @return new enchanted book
	 */
	@NotNull
	public static ItemStack getEnchantedBook(@NotNull ArrayList<EquipEnchantment> enchantments) {
		ItemStack book = ItemStack.of(Material.ENCHANTED_BOOK, 1);
		ItemMeta data = book.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
		dataP.set(itemVersion, PersistentDataType.INTEGER, 0);
		dataP.set(equipmentType, PersistentDataType.STRING, "ENCHANTED_BOOK");
		data.lore(getEnchantedBookLore(book));
		data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		book.setItemMeta(data);
		for (EquipEnchantment enchantment: enchantments)
			enchantment.apply(book);
		return book;
	}

	/**
	 * Creates a banknote
	 * @param amount amount of money in this banknote
	 * @return new banknote
	 */
	@NotNull
	public static ItemStack getBanknote(int amount) {
		ItemStack banknote = ItemStack.of(Material.WRITTEN_BOOK, 1);
		banknote.editMeta(data -> {
			if (data instanceof BookMeta data1) {
				data1.customName(textNI("$" + amount, NamedTextColor.GREEN));
				data1.getPersistentDataContainer().set(money, PersistentDataType.INTEGER, amount);
				data1.addPages(text("Redeem this banknote in the banking menu", NamedTextColor.DARK_GREEN));
				data1.setAuthor("Banknote");
				data1.setMaxStackSize(64);
			}
		});
		return banknote;
	}
}

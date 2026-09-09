package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import io.github.YellowStoneTorch.SMP_Expansion.Equipment.ItemManager;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static com.google.common.math.DoubleMath.log2;
import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.isCustom;
import static io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EnchantmentType.*;

/**
 * Base class for all custom enchantments
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public abstract class EquipEnchantment {
	private static final Hashtable<EnchantmentType, EquipEnchantment[]> enchantments;

	static {
		enchantments = new Hashtable<>();
		for (EnchantmentType type: EnchantmentType.values())
			enchantments.put(type, new EquipEnchantment[5]);
	}

	public final int level;
	public final EnchantmentType type;
	public final EnchantmentType[] incompatibles;
	protected final ArrayList<TextComponent> description;

	/**
	 * Creates a new enchantment instance, only for use within static factory
	 * @param level level of enchantment
	 * @param type type on enchantment
	 */
	EquipEnchantment(int level, @NotNull EnchantmentType type) {
		this.level = level;
		this.type = type;
		description = new ArrayList<>();
		incompatibles = switch (type) {
			case BANE_OF_ARTHROPODS ->
					new EnchantmentType[]{SHARPNESS, SMITE, BREACH, DENSITY, IMPALING};
			case BLAST_PROTECTION ->
					new EnchantmentType[]{REINFORCEMENT, PROTECTION, FIRE_PROTECTION};
			case BREACH ->
					new EnchantmentType[]{SHARPNESS, SMITE, BANE_OF_ARTHROPODS, DENSITY, IMPALING};
			case CHANNELING, LOYALTY ->
					new EnchantmentType[]{RIPTIDE};
			case DENSITY ->
					new EnchantmentType[]{SHARPNESS, SMITE, BANE_OF_ARTHROPODS, BREACH, IMPALING};
			case DEPTH_STRIDER ->
					new EnchantmentType[]{FROST_WALKER};
			case FIRE_PROTECTION ->
					new EnchantmentType[]{REINFORCEMENT, PROTECTION, BLAST_PROTECTION};
			case FORTUNE ->
					new EnchantmentType[]{SILK_TOUCH};
			case FROST_WALKER ->
					new EnchantmentType[]{DEPTH_STRIDER};
			case IMPALING ->
					new EnchantmentType[]{SHARPNESS, SMITE, BANE_OF_ARTHROPODS, BREACH, DENSITY};
			case INFINITY ->
					new EnchantmentType[]{MENDING};
			case MENDING ->
					new EnchantmentType[]{INFINITY};
			case MULTISHOT ->
					new EnchantmentType[]{PIERCING};
			case PIERCING ->
					new EnchantmentType[]{MULTISHOT};
			case PROTECTION ->
					new EnchantmentType[]{REINFORCEMENT, BLAST_PROTECTION, FIRE_PROTECTION};
			case REINFORCEMENT ->
					new EnchantmentType[]{PROTECTION, BLAST_PROTECTION, FIRE_PROTECTION};
			case RIPTIDE ->
					new EnchantmentType[]{CHANNELING, LOYALTY};
			case SHARPNESS ->
					new EnchantmentType[]{SMITE, BANE_OF_ARTHROPODS, BREACH, DENSITY, IMPALING};
			case SILK_TOUCH ->
					new EnchantmentType[]{FORTUNE};
			case SMITE ->
					new EnchantmentType[]{SHARPNESS, BANE_OF_ARTHROPODS, BREACH, DENSITY, IMPALING};
			default ->
					new EnchantmentType[]{};
		};
	}

	/**
	 * Static factory for Enchantment instances
	 * @param type enchantment type
	 * @param level enchantment level
	 * @return enchantment object, or null if params are invalid
	 */
	@Nullable
	public static EquipEnchantment getEnchantment(@NotNull EnchantmentType type, int level) {
		if (level < 1)
			return null;
		EquipEnchantment enchantment = enchantments.get(type)[level - 1];
		if (enchantment == null) { // Instantiate new enchantment object
			enchantment = switch (type) {
				case AQUA_AFFINITY ->
						new AquaAffinity();
				case BANE_OF_ARTHROPODS ->
						new BaneOfArthropods(level);
				case BINDING_CURSE ->
						new CurseOfBinding();
				case BLAST_PROTECTION ->
						new BlastProtection(level);
				case BREACH ->
						new Breach(level);
				case CHANNELING ->
						new Channeling();
				case DENSITY ->
						new Density(level);
				case DEPTH_STRIDER ->
						new DepthStrider(level);
				case EFFICIENCY ->
						new Efficiency(level);
				case FEATHER_FALLING ->
						new FeatherFalling(level);
				case FIRE_ASPECT ->
						new FireAspect(level);
				case FIRE_PROTECTION ->
						new FireProtection(level);
				case FLAME ->
						new Flame();
				case FORTUNE ->
						new Fortune(level);
				case FROST_WALKER ->
						new FrostWalker(level);
				case IMPALING ->
						new Impaling(level);
				case INFINITY ->
						new Infinity();
				case KNOCKBACK ->
						new Knockback(level);
				case LOOTING ->
						new Looting(level);
				case LOYALTY ->
						new Loyalty(level);
				case LUCK_OF_THE_SEA ->
						new LuckOfTheSea(level);
				case LUNGE ->
						new Lunge(level);
				case LURE ->
						new Lure(level);
				case MENDING ->
						new Mending();
				case MULTISHOT ->
						new Multishot();
				case PIERCING ->
						new Piercing(level);
				case POWER ->
						new Power(level);
				case PROTECTION ->
						new Protection(level);
				case PUNCH ->
						new Punch(level);
				case QUICK_CHARGE ->
						new QuickCharge(level);
				case REINFORCEMENT ->
						new Reinforcement(level);
				case RESPIRATION ->
						new Respiration(level);
				case RIPTIDE ->
						new Riptide(level);
				case SHARPNESS ->
						new Sharpness(level);
				case SILK_TOUCH ->
						new SilkTouch();
				case SMITE ->
						new Smite(level);
				case SOUL_SPEED ->
						new SoulSpeed(level);
				case SWEEPING_EDGE ->
						new SweepingEdge(level);
				case SWIFT_SNEAK ->
						new SwiftSneak(level);
				case THORNS ->
						new Thorns(level);
				case UNBREAKING ->
						new Unbreaking(level);
				case VANISHING_CURSE ->
						new CurseOfVanishing();
				case WIND_BURST ->
						new WindBurst(level);
			};
			EquipEnchantment[] enchantmentList = enchantments.get(type);
			enchantmentList[level - 1] = enchantment;
			enchantments.put(type, enchantmentList);
		}
		return enchantment;
	}

	/**
	 * Gets the first enchantment of the given type from the list
	 * @param type type of enchantment
	 * @param enchantmentList list of enchantments
	 * @return first enchantment of given type, or null if no enchantments exist
	 */
	@Nullable
	public static EquipEnchantment getEnchantment(EnchantmentType type, @NotNull List<EquipEnchantment> enchantmentList) {
		for (EquipEnchantment enchantment: enchantmentList)
			if (type == enchantment.type)
				return enchantment;
		return null;
	}

	/**
	 * Gets a list of all enchantments on this custom item
	 * @param item item to get enchantments from
	 * @return list of enchantments
	 */
	@NotNull
	public static ArrayList<EquipEnchantment> getEnchantments(@NotNull ItemStack item) {
		ArrayList<EquipEnchantment> enchantmentList = new ArrayList<>();
		PersistentDataContainer itemData = item.getItemMeta()
				.getPersistentDataContainer();
		for (EnchantmentType enchantment: EnchantmentType.values())
			if (itemData.has(enchantment.key))
				enchantmentList.add(getEnchantment(enchantment, itemData.getOrDefault(enchantment.key, PersistentDataType.INTEGER, 1)));
		return enchantmentList;
	}

	/**
	 * Removes all enchantments from the item except for curses
	 * @param item item to remove enchantments from
	 * @return item with enchantments removed
	 */
	public static ItemStack removeAllEnchantments(@NotNull ItemStack item) {
		ArrayList<EquipEnchantment> enchantmentList = getEnchantments(item);
		for (EquipEnchantment enchantment: enchantmentList)
			if (enchantment.type.rarity != Rarity.CURSE)
				enchantment.remove(item);
		return item;
	}

	/**
	 * Gets the maximum unlocked level for the enchantment type given the current xp
	 * @param enchantment enchantment type
	 * @param xp xp amount
	 * @return maximum level (0-5)
	 */
	public static int getLevelFromXP(@NotNull EnchantmentType enchantment, int xp) {
		int level = 0;
		switch (enchantment.rarity) {
			case COMMON -> {
				if (xp > 0) {
					level = log2(xp, RoundingMode.FLOOR) + 1;
					if (level > enchantment.maxLevel)
						level = enchantment.maxLevel;
				}
				else
					level = 1;
			}
			case RARE -> {
				if (xp > 0) {
					level = log2(xp, RoundingMode.FLOOR);
					if (level > enchantment.maxLevel)
						level = enchantment.maxLevel;
				}
			}
		}
		return level;
	}

	/**
	 * Gets the description of this enchantment
	 * @return list of text components that are the description
	 */
	public ArrayList<TextComponent> getDescription() {
		return new ArrayList<>(description);
	}

	/**
	 * Applies the enchantment to a custom item
	 * @param item original item
	 */
	public void apply(@NotNull ItemStack item) {
		ItemMeta data = item.getItemMeta();
		PersistentDataContainer dataP = data.getPersistentDataContainer();
		if (dataP.getOrDefault(isCustom, PersistentDataType.BOOLEAN, false)) {
			if (!dataP.has(type.key)) {
				dataP.set(type.key, PersistentDataType.INTEGER, level);
				Enchantment oldEnchantment = EnchantmentType.getOldEnchantment(type);
				if (oldEnchantment != null)
					data.addEnchant(oldEnchantment, level, true);
			}
		}
		item.setItemMeta(data);
		ItemManager.regenerateLore(item);
	}

	/**
	 * Removes the enchantment from a custom item
	 * @param item original item
	 */
	public void remove(@NotNull ItemStack item) {
		ItemMeta data = item.getItemMeta();
		if (data instanceof Damageable dataD) {
			PersistentDataContainer dataP = data.getPersistentDataContainer();
			if (dataP.getOrDefault(isCustom, PersistentDataType.BOOLEAN, false)) {
				dataP.remove(type.key);
				Enchantment oldEnchantment = EnchantmentType.getOldEnchantment(type);
				if (oldEnchantment != null)
					dataD.removeEnchant(oldEnchantment);
			}
		}
		item.setItemMeta(data);
		ItemManager.regenerateLore(item);
	}

	/**
	 * Gets the first enchantment that matches the type of this enchantment
	 * @param enchantmentList list of enchantments to check
	 * @return matching enchantment, or null if none match
	 */
	@Nullable
	public EquipEnchantment getMatchingEnchantment(@NotNull List<EquipEnchantment> enchantmentList) {
		for (EquipEnchantment enchantment: enchantmentList) {
			if (type == enchantment.type)
				return enchantment;
		}
		return null;
	}

	/**
	 * Checks if the enchantment is compatible with the given list
	 * @param enchantments list of enchantments
	 * @return whether no incompatible enchantment exists in the list
	 */
	public boolean isCompatible(List<EquipEnchantment> enchantments) {
		if (incompatibles.length == 0)
			return true;
		for (EquipEnchantment enchantment: enchantments) {
			for (EnchantmentType eType: incompatibles) {
				if (eType == enchantment.type)
					return false;
			}
		}
		return true;
	}

	/**
	 * Gets the title to display for this enchantment
	 * @return title
	 */
	public TextComponent getTitle() {
		return EnchantmentType.getTitle(type, level);
	}

	/**
	 * Gets the title to display for this enchantment
	 * @return title
	 */
	public TextComponent.Builder getTitleBuilder() {
		return EnchantmentType.getTitleBuilder(type, level);
	}
}

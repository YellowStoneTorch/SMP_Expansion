package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.*;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.compNI;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Enum that holds all custom enchantments
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public enum EnchantmentType {
	AQUA_AFFINITY(1, Rarity.RARE, aquaAffinity),
	BANE_OF_ARTHROPODS(5, Rarity.COMMON, baneOfArthropods),
	BINDING_CURSE(1, Rarity.CURSE, curseOfBinding),
	BLAST_PROTECTION(4, Rarity.COMMON, blastProtection),
	BREACH(4, Rarity.RARE, breach),
	CHANNELING(1, Rarity.RARE, channeling),
	DENSITY(5, Rarity.RARE, density),
	DEPTH_STRIDER(3, Rarity.RARE, depthStrider),
	EFFICIENCY(5, Rarity.COMMON, efficiency),
	FEATHER_FALLING(4, Rarity.RARE, featherFalling),
	FIRE_ASPECT(2, Rarity.RARE, fireAspect),
	FIRE_PROTECTION(4, Rarity.COMMON, fireProtection),
	FLAME(1, Rarity.RARE, flame),
	FORTUNE(3, Rarity.RARE, fortune),
	FROST_WALKER(2, Rarity.EPIC, frostWalker),
	IMPALING(5, Rarity.COMMON, impaling),
	INFINITY(1, Rarity.RARE, infinity),
	KNOCKBACK(2, Rarity.RARE, knockback),
	LOOTING(3, Rarity.RARE, looting),
	LOYALTY(3, Rarity.RARE, loyalty),
	LUCK_OF_THE_SEA(3, Rarity.RARE, luckOfTheSea),
	LUNGE(3, Rarity.RARE, lunge),
	LURE(3, Rarity.RARE, lure),
	MENDING(1, Rarity.EPIC, mending),
	MULTISHOT(1, Rarity.RARE, multishot),
	PIERCING(4, Rarity.RARE, piercing),
	POWER(5, Rarity.COMMON, power),
	PROTECTION(4, Rarity.COMMON, protection),
	PUNCH(2, Rarity.RARE, punch),
	REINFORCEMENT(4, Rarity.COMMON, reinforcement),
	QUICK_CHARGE(3, Rarity.RARE, quickCharge),
	RESPIRATION(3, Rarity.RARE, respiration),
	RIPTIDE(3, Rarity.RARE, riptide),
	SHARPNESS(5, Rarity.COMMON, sharpness),
	SILK_TOUCH(1, Rarity.RARE, silkTouch),
	SMITE(5, Rarity.COMMON, smite),
	SOUL_SPEED(3, Rarity.EPIC, soulSpeed),
	SWEEPING_EDGE(3, Rarity.RARE, sweepingEdge),
	SWIFT_SNEAK(3, Rarity.EPIC, swiftSneak),
	THORNS(3, Rarity.RARE, thorns),
	UNBREAKING(3, Rarity.COMMON, unbreaking),
	VANISHING_CURSE(1, Rarity.CURSE, curseOfVanishing),
	WIND_BURST(3, Rarity.EPIC, windBurst);

	private static final HashMap<Enchantment, EnchantmentType> enchantmentMap;
	private static final HashMap<Integer, String> romanNumerals;

	static {
		enchantmentMap = new HashMap<>();
		romanNumerals = new HashMap<>();
		enchantmentMap.put(Enchantment.AQUA_AFFINITY, AQUA_AFFINITY);
		enchantmentMap.put(Enchantment.BANE_OF_ARTHROPODS, BANE_OF_ARTHROPODS);
		enchantmentMap.put(Enchantment.BINDING_CURSE, BINDING_CURSE);
		enchantmentMap.put(Enchantment.BLAST_PROTECTION, BLAST_PROTECTION);
		enchantmentMap.put(Enchantment.BREACH, BREACH);
		enchantmentMap.put(Enchantment.CHANNELING, CHANNELING);
		enchantmentMap.put(Enchantment.DENSITY, DENSITY);
		enchantmentMap.put(Enchantment.DEPTH_STRIDER, DEPTH_STRIDER);
		enchantmentMap.put(Enchantment.EFFICIENCY, EFFICIENCY);
		enchantmentMap.put(Enchantment.FEATHER_FALLING, FEATHER_FALLING);
		enchantmentMap.put(Enchantment.FIRE_ASPECT, FIRE_ASPECT);
		enchantmentMap.put(Enchantment.FIRE_PROTECTION, FIRE_PROTECTION);
		enchantmentMap.put(Enchantment.FLAME, FLAME);
		enchantmentMap.put(Enchantment.FORTUNE, FORTUNE);
		enchantmentMap.put(Enchantment.FROST_WALKER, FROST_WALKER);
		enchantmentMap.put(Enchantment.IMPALING, IMPALING);
		enchantmentMap.put(Enchantment.INFINITY, INFINITY);
		enchantmentMap.put(Enchantment.KNOCKBACK, KNOCKBACK);
		enchantmentMap.put(Enchantment.LOOTING, LOOTING);
		enchantmentMap.put(Enchantment.LOYALTY, LOYALTY);
		enchantmentMap.put(Enchantment.LUNGE, LUNGE);
		enchantmentMap.put(Enchantment.LUCK_OF_THE_SEA, LUCK_OF_THE_SEA);
		enchantmentMap.put(Enchantment.LURE, LURE);
		enchantmentMap.put(Enchantment.MENDING, MENDING);
		enchantmentMap.put(Enchantment.MULTISHOT, MULTISHOT);
		enchantmentMap.put(Enchantment.PIERCING, PIERCING);
		enchantmentMap.put(Enchantment.POWER, POWER);
		enchantmentMap.put(Enchantment.PROJECTILE_PROTECTION, PROTECTION);
		enchantmentMap.put(Enchantment.PROTECTION, REINFORCEMENT);
		enchantmentMap.put(Enchantment.PUNCH, PUNCH);
		enchantmentMap.put(Enchantment.QUICK_CHARGE, QUICK_CHARGE);
		enchantmentMap.put(Enchantment.RESPIRATION, RESPIRATION);
		enchantmentMap.put(Enchantment.RIPTIDE, RIPTIDE);
		enchantmentMap.put(Enchantment.SHARPNESS, SHARPNESS);
		enchantmentMap.put(Enchantment.SILK_TOUCH, SILK_TOUCH);
		enchantmentMap.put(Enchantment.SMITE, SMITE);
		enchantmentMap.put(Enchantment.SOUL_SPEED, SOUL_SPEED);
		enchantmentMap.put(Enchantment.SWEEPING_EDGE, SWEEPING_EDGE);
		enchantmentMap.put(Enchantment.SWIFT_SNEAK, SWIFT_SNEAK);
		enchantmentMap.put(Enchantment.THORNS, THORNS);
		enchantmentMap.put(Enchantment.UNBREAKING, UNBREAKING);
		enchantmentMap.put(Enchantment.VANISHING_CURSE, VANISHING_CURSE);
		enchantmentMap.put(Enchantment.WIND_BURST, WIND_BURST);
		romanNumerals.put(1, "I");
		romanNumerals.put(2, "II");
		romanNumerals.put(3, "III");
		romanNumerals.put(4, "IV");
		romanNumerals.put(5, "V");
	}

	public final int maxLevel;
	public final Rarity rarity;
	public final NamespacedKey key;

	EnchantmentType(int maxLevel, Rarity rarity, NamespacedKey key) {
		this.maxLevel = maxLevel;
		this.rarity = rarity;
		this.key = key;
	}

	/**
	 * Gets the custom EnchantmentType enum from a vanilla enchantment
	 * @param enchantment vanilla enchantment
	 * @return new enchantment type
	 */
	public static EnchantmentType getNewEnchantment(Enchantment enchantment) {
		if (enchantmentMap.containsKey(enchantment))
			return enchantmentMap.get(enchantment);
		throw new RuntimeException("Enchantment not yet implemented: " + enchantment.getKey().asString());
	}

	/**
	 * Gets a vanilla enchantment from the EnchantmentType enum
	 * @param type new enchantment type
	 * @return vanilla enchantment
	 */
	@Nullable
	public static Enchantment getOldEnchantment(EnchantmentType type) {
		for (Enchantment enchantment: enchantmentMap.keySet())
			if (enchantmentMap.get(enchantment) == type)
				return enchantment;
		return null;
	}

	/**
	 * Gets the title to display in item lore for enchantment
	 * @param enchantment enchantment type
	 * @param level enchantment level
	 * @return enchantment title
	 */
	@NotNull
	public static TextComponent getTitle(@NotNull EnchantmentType enchantment, int level) {
		return switch (enchantment) {
			case AQUA_AFFINITY ->
					textNI("Aqua Affinity", NamedTextColor.AQUA);
			case BANE_OF_ARTHROPODS ->
					textNI("Bane Of Arthropods " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case BINDING_CURSE ->
					textNI("Curse of Binding", NamedTextColor.RED);
			case BLAST_PROTECTION ->
					textNI("Blast Protection " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case BREACH ->
					textNI("Breach " + romanNumerals.get(level), NamedTextColor.AQUA);
			case CHANNELING ->
					textNI("Channeling", NamedTextColor.AQUA);
			case DENSITY ->
					textNI("Density " + romanNumerals.get(level), NamedTextColor.AQUA);
			case DEPTH_STRIDER ->
					textNI("Depth Strider " + romanNumerals.get(level), NamedTextColor.AQUA);
			case EFFICIENCY ->
					textNI("Efficiency " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case FEATHER_FALLING ->
					textNI("Feather Falling " + romanNumerals.get(level), NamedTextColor.AQUA);
			case FIRE_ASPECT ->
					textNI("Fire Aspect " + romanNumerals.get(level), NamedTextColor.AQUA);
			case FIRE_PROTECTION ->
					textNI("Fire Protection " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case FLAME ->
					textNI("Flame", NamedTextColor.AQUA);
			case FORTUNE ->
					textNI("Fortune " + romanNumerals.get(level), NamedTextColor.AQUA);
			case FROST_WALKER ->
					textNI("Frost Walker " + romanNumerals.get(level), NamedTextColor.LIGHT_PURPLE);
			case IMPALING ->
					textNI("Impaling " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case INFINITY ->
					textNI("Infinity", NamedTextColor.AQUA);
			case KNOCKBACK ->
					textNI("Knockback " + romanNumerals.get(level), NamedTextColor.AQUA);
			case LOOTING ->
					textNI("Looting " + romanNumerals.get(level), NamedTextColor.AQUA);
			case LOYALTY ->
					textNI("Loyalty " + romanNumerals.get(level), NamedTextColor.AQUA);
			case LUCK_OF_THE_SEA ->
					textNI("Luck Of The Sea " + romanNumerals.get(level), NamedTextColor.AQUA);
			case LUNGE ->
					textNI("Lunge " + romanNumerals.get(level), NamedTextColor.AQUA);
			case LURE ->
					textNI("Lure " + romanNumerals.get(level), NamedTextColor.AQUA);
			case MENDING ->
					textNI("Mending", NamedTextColor.LIGHT_PURPLE);
			case MULTISHOT ->
					textNI("Multishot", NamedTextColor.AQUA);
			case PIERCING ->
					textNI("Piercing " + romanNumerals.get(level), NamedTextColor.AQUA);
			case POWER ->
					textNI("Power " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case PROTECTION ->
					textNI("Protection " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case PUNCH ->
					textNI("Punch " + romanNumerals.get(level), NamedTextColor.AQUA);
			case QUICK_CHARGE ->
					textNI("Quick Charge " + romanNumerals.get(level), NamedTextColor.AQUA);
			case REINFORCEMENT ->
					textNI("Reinforcement " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case RESPIRATION ->
					textNI("Respiration " + romanNumerals.get(level), NamedTextColor.AQUA);
			case RIPTIDE ->
					textNI("Riptide " + romanNumerals.get(level), NamedTextColor.AQUA);
			case SHARPNESS ->
					textNI("Sharpness " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case SILK_TOUCH ->
					textNI("Silk Touch", NamedTextColor.AQUA);
			case SMITE ->
					textNI("Smite " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case SOUL_SPEED ->
					textNI("Soul Speed " + romanNumerals.get(level), NamedTextColor.LIGHT_PURPLE);
			case SWEEPING_EDGE ->
					textNI("Sweeping Edge " + romanNumerals.get(level), NamedTextColor.AQUA);
			case SWIFT_SNEAK ->
					textNI("Swift Sneak " + romanNumerals.get(level), NamedTextColor.LIGHT_PURPLE);
			case THORNS ->
					textNI("Thorns " + romanNumerals.get(level), NamedTextColor.AQUA);
			case UNBREAKING ->
					textNI("Unbreaking " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case VANISHING_CURSE ->
					textNI("Curse of Vanishing", NamedTextColor.RED);
			case WIND_BURST ->
					textNI("Wind Burst " + romanNumerals.get(level), NamedTextColor.LIGHT_PURPLE);
		};
	}

	/**
	 * Gets the title to display in item lore for enchantment
	 * @param enchantment enchantment type
	 * @param level enchantment level
	 * @return enchantment title
	 */
	@NotNull
	public static TextComponent.Builder getTitleBuilder(@NotNull EnchantmentType enchantment, int level) {
		return switch (enchantment) {
			case AQUA_AFFINITY ->
					compNI("Aqua Affinity", NamedTextColor.AQUA);
			case BANE_OF_ARTHROPODS ->
					compNI("Bane Of Arthropods " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case BINDING_CURSE ->
					compNI("Curse of Binding", NamedTextColor.RED);
			case BLAST_PROTECTION ->
					compNI("Blast Protection " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case BREACH ->
					compNI("Breach " + romanNumerals.get(level), NamedTextColor.AQUA);
			case CHANNELING ->
					compNI("Channeling", NamedTextColor.AQUA);
			case DENSITY ->
					compNI("Density " + romanNumerals.get(level), NamedTextColor.AQUA);
			case DEPTH_STRIDER ->
					compNI("Depth Strider " + romanNumerals.get(level), NamedTextColor.AQUA);
			case EFFICIENCY ->
					compNI("Efficiency " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case FEATHER_FALLING ->
					compNI("Feather Falling " + romanNumerals.get(level), NamedTextColor.AQUA);
			case FIRE_ASPECT ->
					compNI("Fire Aspect " + romanNumerals.get(level), NamedTextColor.AQUA);
			case FIRE_PROTECTION ->
					compNI("Fire Protection " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case FLAME ->
					compNI("Flame", NamedTextColor.AQUA);
			case FORTUNE ->
					compNI("Fortune " + romanNumerals.get(level), NamedTextColor.AQUA);
			case FROST_WALKER ->
					compNI("Frost Walker " + romanNumerals.get(level), NamedTextColor.LIGHT_PURPLE);
			case IMPALING ->
					compNI("Impaling " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case INFINITY ->
					compNI("Infinity", NamedTextColor.AQUA);
			case KNOCKBACK ->
					compNI("Knockback " + romanNumerals.get(level), NamedTextColor.AQUA);
			case LOOTING ->
					compNI("Looting " + romanNumerals.get(level), NamedTextColor.AQUA);
			case LOYALTY ->
					compNI("Loyalty " + romanNumerals.get(level), NamedTextColor.AQUA);
			case LUCK_OF_THE_SEA ->
					compNI("Luck Of The Sea " + romanNumerals.get(level), NamedTextColor.AQUA);
			case LUNGE ->
					compNI("Lunge " + romanNumerals.get(level), NamedTextColor.AQUA);
			case LURE ->
					compNI("Lure " + romanNumerals.get(level), NamedTextColor.AQUA);
			case MENDING ->
					compNI("Mending", NamedTextColor.LIGHT_PURPLE);
			case MULTISHOT ->
					compNI("Multishot", NamedTextColor.AQUA);
			case PIERCING ->
					compNI("Piercing " + romanNumerals.get(level), NamedTextColor.AQUA);
			case POWER ->
					compNI("Power " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case PROTECTION ->
					compNI("Protection " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case PUNCH ->
					compNI("Punch " + romanNumerals.get(level), NamedTextColor.AQUA);
			case QUICK_CHARGE ->
					compNI("Quick Charge " + romanNumerals.get(level), NamedTextColor.AQUA);
			case REINFORCEMENT ->
					compNI("Reinforcement " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case RESPIRATION ->
					compNI("Respiration " + romanNumerals.get(level), NamedTextColor.AQUA);
			case RIPTIDE ->
					compNI("Riptide " + romanNumerals.get(level), NamedTextColor.AQUA);
			case SHARPNESS ->
					compNI("Sharpness " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case SILK_TOUCH ->
					compNI("Silk Touch", NamedTextColor.AQUA);
			case SMITE ->
					compNI("Smite " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case SOUL_SPEED ->
					compNI("Soul Speed " + romanNumerals.get(level), NamedTextColor.LIGHT_PURPLE);
			case SWEEPING_EDGE ->
					compNI("Sweeping Edge " + romanNumerals.get(level), NamedTextColor.AQUA);
			case SWIFT_SNEAK ->
					compNI("Swift Sneak " + romanNumerals.get(level), NamedTextColor.LIGHT_PURPLE);
			case THORNS ->
					compNI("Thorns " + romanNumerals.get(level), NamedTextColor.AQUA);
			case UNBREAKING ->
					compNI("Unbreaking " + romanNumerals.get(level), NamedTextColor.YELLOW);
			case VANISHING_CURSE ->
					compNI("Curse of Vanishing", NamedTextColor.RED);
			case WIND_BURST ->
					compNI("Wind Burst " + romanNumerals.get(level), NamedTextColor.LIGHT_PURPLE);
		};
	}

	/**
	 * Represents the rarity of the enchantment
	 * @author YellowStoneTorch
	 * @version 0.1.0-ALPHA
	 */
	public enum Rarity {
		COMMON,
		RARE,
		EPIC,
		GOLDEN,
		CURSE
	}
}

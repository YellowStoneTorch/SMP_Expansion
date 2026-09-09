package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Reduces the effectiveness of the opponent's armor
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Breach extends EquipEnchantment {
	Breach(int level) {
		super(level, EnchantmentType.BREACH);
		description.add(textNI("Reduces enemy armor effectiveness by " + (15 * level) + "%", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Sharpness, Smite,", NamedTextColor.RED));
		description.add(textNI("Bane of Arthropods, Density, and Impaling.", NamedTextColor.RED));
	}
}

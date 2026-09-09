package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases falling damage during a smash attack
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Density extends EquipEnchantment {
	Density(int level) {
		super(level, EnchantmentType.DENSITY);
		description.add(textNI("Increases fall distance used to", NamedTextColor.GRAY));
		description.add(textNI("calculate smash attacks by " + (5 * level) + "%", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Sharpness, Smite,", NamedTextColor.RED));
		description.add(textNI("Bane of Arthropods, Breach, and Impaling.", NamedTextColor.RED));
	}
}

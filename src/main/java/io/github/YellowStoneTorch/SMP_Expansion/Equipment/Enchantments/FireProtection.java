package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Provides extra protection against fire damage and reduces burning time
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class FireProtection extends EquipEnchantment {
	FireProtection(int level) {
		super(level, EnchantmentType.FIRE_PROTECTION);
		description.add(textNI("Reduces fire damage by " + (8 * level) + "% and", NamedTextColor.GRAY));
		description.add(textNI("reduces burning time by " + (15 * level) + "%", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Reinforcement,", NamedTextColor.RED));
		description.add(textNI("Protection, and Blast Protection.", NamedTextColor.RED));
	}

}

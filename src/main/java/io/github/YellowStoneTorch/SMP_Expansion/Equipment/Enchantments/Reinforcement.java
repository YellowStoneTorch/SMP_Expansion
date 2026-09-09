package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Provides extra protection against damage reduced by armor
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Reinforcement extends EquipEnchantment {
	Reinforcement(int level) {
		super(level, EnchantmentType.REINFORCEMENT);
		description.add(textNI("Reduces damage protected", NamedTextColor.GRAY));
		description.add(textNI("against by armor by " + (4 * level) + "%", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Protection,", NamedTextColor.RED));
		description.add(textNI("Blast Protection, and Fire Protection.", NamedTextColor.RED));
	}

}

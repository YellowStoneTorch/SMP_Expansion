package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Provides extra protection against damage not reduced by armor
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Protection extends EquipEnchantment {
	Protection(int level) {
		super(level, EnchantmentType.PROTECTION);
		description.add(textNI("Reduces damage not protected", NamedTextColor.GRAY));
		description.add(textNI("against by armor by " + (4 * level) + "%", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Reinforcement,", NamedTextColor.RED));
		description.add(textNI("Blast Protection, and Fire Protection.", NamedTextColor.RED));
	}

}

package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases weapon knockback
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Knockback extends EquipEnchantment {
	Knockback(int level) {
		super(level, EnchantmentType.KNOCKBACK);
		description.add(textNI("Increases melee attack knockback.", NamedTextColor.GRAY));
	}

}

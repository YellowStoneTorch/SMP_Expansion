package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases bow knockback
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Punch extends EquipEnchantment {
	Punch(int level) {
		super(level, EnchantmentType.PUNCH);
		description.add(textNI("Increases arrow knockback.", NamedTextColor.GRAY));
	}

}

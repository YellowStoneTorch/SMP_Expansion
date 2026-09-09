package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases bow and crossbow damage
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Power extends EquipEnchantment {
	Power(int level) {
		super(level, EnchantmentType.POWER);
		description.add(textNI("Increases arrow damage.", NamedTextColor.GRAY));
	}

}

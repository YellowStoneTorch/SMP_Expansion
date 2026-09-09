package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases mining speed for tools
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Efficiency extends EquipEnchantment {
	Efficiency(int level) {
		super(level, EnchantmentType.EFFICIENCY);
		description.add(textNI("Increases mining efficiency by " + (Math.pow(level, 2) + 1), NamedTextColor.GRAY));
	}

}

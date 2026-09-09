package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Decreases fishing cooldown
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Lure extends EquipEnchantment {
	Lure(int level) {
		super(level, EnchantmentType.LURE);
		description.add(textNI("Decreases fishing wait time by " + (5 * level) + " seconds.", NamedTextColor.GRAY));
	}

}

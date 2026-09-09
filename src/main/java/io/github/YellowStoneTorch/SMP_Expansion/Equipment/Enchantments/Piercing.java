package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Decreases crossbow reload time
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Piercing extends EquipEnchantment {
	Piercing(int level) {
		super(level, EnchantmentType.PIERCING);
		description.add(textNI("Arrows pierce through " + (1 + level) + " targets.", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Multishot.", NamedTextColor.RED));
	}

}

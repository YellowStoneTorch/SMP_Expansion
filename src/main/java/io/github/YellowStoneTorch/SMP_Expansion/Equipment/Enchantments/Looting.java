package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases mob drops
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Looting extends EquipEnchantment {
	Looting(int level) {
		super(level, EnchantmentType.LOOTING);
		description.add(textNI("Increases the number and likelihood", NamedTextColor.GRAY));
		description.add(textNI("of drops upon killing mobs.", NamedTextColor.GRAY));
	}

}

package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases item drops from mined blocks
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Fortune extends EquipEnchantment {
	Fortune(int level) {
		super(level, EnchantmentType.FORTUNE);
		description.add(textNI("Increases the number and likelihood", NamedTextColor.GRAY));
		description.add(textNI("of drops upon breaking blocks.", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Silk Touch.", NamedTextColor.RED));
	}

}

package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Prevents normal arrows from being used when firing the bow
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Infinity extends EquipEnchantment {
	Infinity() {
		super(1, EnchantmentType.INFINITY);
		description.add(textNI("Prevents normal arrows from being used when firing.", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Mending,", NamedTextColor.RED));
	}

}

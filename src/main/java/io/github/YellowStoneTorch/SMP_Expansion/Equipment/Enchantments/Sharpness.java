package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases weapon damage
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Sharpness extends EquipEnchantment {
	Sharpness(int level) {
		super(level, EnchantmentType.SHARPNESS);
		description.add(textNI("Increases weapon damage by " + ((1 + level) / 2.0) + ".", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Smite, Bane of Arthropods,", NamedTextColor.RED));
		description.add(textNI("Breach, Density, and Impaling.", NamedTextColor.RED));
	}

}

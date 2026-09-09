package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases trident damage against aquatic mobs
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Impaling extends EquipEnchantment {
	Impaling(int level) {
		super(level, EnchantmentType.IMPALING);
		description.add(textNI("Increases weapon damage by " + ((1 + level) / 4.0) + "with an", NamedTextColor.GRAY));
		description.add(textNI("additional " + ((1 + level) * 1.25) + " damage to aquatic mobs.", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Sharpness, Smite,", NamedTextColor.RED));
		description.add(textNI("Bane of Arthropods, Breach, and Density.", NamedTextColor.RED));
	}

}

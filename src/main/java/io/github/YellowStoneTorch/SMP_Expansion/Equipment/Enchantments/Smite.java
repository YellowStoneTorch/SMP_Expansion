package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases weapon damage with bonus damage against undead mobs
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Smite extends EquipEnchantment {
	Smite(int level) {
		super(level, EnchantmentType.SMITE);
		description.add(textNI("Increases weapon damage by " + ((1 + level) / 4.0) + " with an", NamedTextColor.GRAY));
		description.add(textNI("additional " + ((1 + level) * 1.25) + " damage to undead mobs.", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Sharpness, Bane of Arthropods,", NamedTextColor.RED));
		description.add(textNI("Breach, Density, and Impaling.", NamedTextColor.RED));
	}

}

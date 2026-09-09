package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases weapon damage with bonus damage against arthropods
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class BaneOfArthropods extends EquipEnchantment {
	BaneOfArthropods(int level) {
		super(level, EnchantmentType.BANE_OF_ARTHROPODS);
		description.add(textNI("Increases weapon damage by " + ((1 + level) / 4.0) + " with an", NamedTextColor.GRAY));
		description.add(textNI("additional " + ((1 + level) * 1.25) + " damage to arthropods.", NamedTextColor.GRAY));
		description.add(textNI("Slows attacked arthropods down.", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Sharpness, Smite,", NamedTextColor.RED));
		description.add(textNI("Breach, Density, and Impaling.", NamedTextColor.RED));
	}
}

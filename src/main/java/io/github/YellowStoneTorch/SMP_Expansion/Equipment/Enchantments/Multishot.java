package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Shoots three projectiles instead of one upon firing
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Multishot extends EquipEnchantment {
	Multishot() {
		super(1, EnchantmentType.MULTISHOT);
		description.add(textNI("Shoots three arrows instead of one.", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Piercing.", NamedTextColor.RED));
	}

}

package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Provides extra protection against fall damage
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class FeatherFalling extends EquipEnchantment {
	FeatherFalling(int level) {
		super(level, EnchantmentType.FEATHER_FALLING);
		description.add(textNI("Reduces fall damage by " + (12 * level) + "%", NamedTextColor.GRAY));
	}

}

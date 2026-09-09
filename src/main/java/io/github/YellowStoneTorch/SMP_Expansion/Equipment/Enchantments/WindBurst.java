package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Launches the player up upon completing a smash attack
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class WindBurst extends EquipEnchantment {
	WindBurst(int level) {
		super(level, EnchantmentType.WIND_BURST);
		description.add(textNI("Upon doing a smash attack, launches", NamedTextColor.GRAY));
		description.add(textNI("the player up " + (8 * level) + " blocks.", NamedTextColor.GRAY));
	}

}

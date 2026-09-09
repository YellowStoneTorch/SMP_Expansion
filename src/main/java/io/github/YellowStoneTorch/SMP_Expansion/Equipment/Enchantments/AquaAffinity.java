package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Removes underwater mining speed penalty
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class AquaAffinity extends EquipEnchantment {
	AquaAffinity() {
		super(1, EnchantmentType.AQUA_AFFINITY);
		description.add(textNI("Removes underwater mining penalty", NamedTextColor.GRAY));
	}
}

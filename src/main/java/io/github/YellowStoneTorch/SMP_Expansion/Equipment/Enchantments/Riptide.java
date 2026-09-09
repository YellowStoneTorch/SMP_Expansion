package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * When in water or rain, launches player forwards
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Riptide extends EquipEnchantment {
	Riptide(int level) {
		super(level, EnchantmentType.RIPTIDE);
		description.add(textNI("Instead of throwing, if in contact with water, launches", NamedTextColor.GRAY));
		description.add(textNI("the player forward and attacks anything in the way.", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Loyalty and Channeling.", NamedTextColor.RED));
	}

}

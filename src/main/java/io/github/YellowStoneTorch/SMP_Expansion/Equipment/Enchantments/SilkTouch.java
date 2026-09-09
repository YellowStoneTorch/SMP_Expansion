package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Makes mined blocks drop themselves
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class SilkTouch extends EquipEnchantment {
	SilkTouch() {
		super(1, EnchantmentType.SILK_TOUCH);
		description.add(textNI("Causes broken blocks to drop themselves", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Fortune", NamedTextColor.RED));
	}

}

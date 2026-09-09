package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Decreases crossbow reload time
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class QuickCharge extends EquipEnchantment {
	QuickCharge(int level) {
		super(level, EnchantmentType.QUICK_CHARGE);
		description.add(textNI("Reduces crossbow loading", NamedTextColor.GRAY));
		description.add(textNI("time by " + (0.25 * level) + " seconds.", NamedTextColor.GRAY));
	}

}

package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Deals damage to attackers at the cost of durability
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Thorns extends EquipEnchantment {
	Thorns(int level) {
		super(level, EnchantmentType.THORNS);
		description.add(textNI("Damages enemies that attack you", NamedTextColor.GRAY));
		description.add(textNI("at the cost of durability damage.", NamedTextColor.GRAY));
		description.add(textNI("Can be toggled off in settings.", NamedTextColor.GRAY));
	}

}

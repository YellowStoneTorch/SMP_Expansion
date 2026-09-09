package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Sets shot arrows on fire
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Flame extends EquipEnchantment {
	Flame() {
		super(1, EnchantmentType.FLAME);
		description.add(textNI("Sets arrows on fire dealing 4 damage.", NamedTextColor.GRAY));
	}

}

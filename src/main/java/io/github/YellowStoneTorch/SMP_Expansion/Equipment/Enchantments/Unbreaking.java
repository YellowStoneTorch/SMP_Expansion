package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.fmt2;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Reduces chance of durability use
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Unbreaking extends EquipEnchantment {
	Unbreaking(int level) {
		super(level, EnchantmentType.UNBREAKING);
		description.add(textNI("Gives a " + fmt2.format(100 * (level / (level + 1.0))) + "% chance to not", NamedTextColor.GRAY));
		description.add(textNI("do durability damage upon use.", NamedTextColor.GRAY));
	}

}

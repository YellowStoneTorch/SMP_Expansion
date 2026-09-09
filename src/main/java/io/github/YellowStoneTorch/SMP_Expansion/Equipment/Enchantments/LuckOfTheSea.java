package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.fmt1;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases the chance of treasure items while fishing
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class LuckOfTheSea extends EquipEnchantment {
	LuckOfTheSea(int level) {
		super(level, EnchantmentType.LUCK_OF_THE_SEA);
		description.add(textNI("When fishing, increases", NamedTextColor.GRAY));
		description.add(textNI("treasure chances by " + fmt1.format(2.1 * level) + "%", NamedTextColor.GRAY));
	}

}

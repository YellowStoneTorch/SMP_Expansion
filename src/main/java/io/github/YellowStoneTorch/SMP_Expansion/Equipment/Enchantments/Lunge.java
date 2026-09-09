package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.fmt2;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Jab attacks fling the player forward at the cost of saturation and durability
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Lunge extends EquipEnchantment {
	Lunge(int level) {
		super(level, EnchantmentType.LUNGE);
		description.add(textNI("Jab attacks fling the player forward at " + fmt2.format(9.16 * level) + " blocks", NamedTextColor.GRAY));
		description.add(textNI("per second at the cost of " + level + " saturation and 1 durability", NamedTextColor.GRAY));
	}

}

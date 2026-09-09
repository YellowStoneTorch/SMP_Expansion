package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.fmt2;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Makes a thrown trident return to player
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Loyalty extends EquipEnchantment {
	Loyalty(int level) {
		super(level, EnchantmentType.LOYALTY);
		description.add(textNI("When thrown, returns to player", NamedTextColor.GRAY));
		description.add(textNI("at " + fmt2.format(50 * level / 3.0) + " blocks per second.", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Riptide.", NamedTextColor.RED));
	}

}

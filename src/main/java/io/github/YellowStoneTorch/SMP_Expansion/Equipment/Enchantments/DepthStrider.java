package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.fmt2;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases water movement speed
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class DepthStrider extends EquipEnchantment {
	DepthStrider(int level) {
		super(level, EnchantmentType.DEPTH_STRIDER);
		description.add(textNI("Reduces swimming speed", NamedTextColor.GRAY));
		description.add(textNI("penalty by " + fmt2.format(100 * level / 3.0) + "%", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Frost Walker.", NamedTextColor.RED));
	}
}

package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Sets targets hit by weapon on fire
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class FireAspect extends EquipEnchantment {
	FireAspect(int level) {
		super(level, EnchantmentType.FIRE_ASPECT);
		description.add(textNI("Sets targets on fire dealing " + ((4 * level) - 1) + " damage.", NamedTextColor.GRAY));
	}

}

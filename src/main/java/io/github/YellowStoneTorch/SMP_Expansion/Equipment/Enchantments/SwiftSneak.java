package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Provides extra protection against damage not reduced by armor
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class SwiftSneak extends EquipEnchantment {
	SwiftSneak(int level) {
		super(level, EnchantmentType.SWIFT_SNEAK);
		description.add(textNI("Increases sneaking movement speed", NamedTextColor.GRAY));
		description.add(textNI("to " + (30 + (15 * level) + "% of normal speed"), NamedTextColor.GRAY));
	}

}

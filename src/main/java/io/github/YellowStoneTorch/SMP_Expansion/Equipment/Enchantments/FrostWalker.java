package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Turns surrounding water into ice to walk across
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class FrostWalker extends EquipEnchantment {
	FrostWalker(int level) {
		super(level, EnchantmentType.FROST_WALKER);
		description.add(textNI("Freezes water into ice in a radius", NamedTextColor.GRAY));
		description.add(textNI("of " + (2 + level) + " blocks upon walking onto water.", NamedTextColor.GRAY));
		description.add(textNI("Prevents damage from magma blocks and campfires.", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Depth Strider.", NamedTextColor.RED));
	}
}

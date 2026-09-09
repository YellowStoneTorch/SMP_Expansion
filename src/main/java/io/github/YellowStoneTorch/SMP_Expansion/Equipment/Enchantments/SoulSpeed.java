package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases movements speed on soul sand and soul soil at cost of durability
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class SoulSpeed extends EquipEnchantment {
	SoulSpeed(int level) {
		super(level, EnchantmentType.SOUL_SPEED);
		description.add(textNI("Increases movement speed on soul sand", NamedTextColor.GRAY));
		description.add(textNI("and soul soil by " + (100 * ((level * 0.105) + 1.3)) + "%", NamedTextColor.GRAY));
		description.add(textNI("at the cost of durability damage.", NamedTextColor.GRAY));
	}

}

package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Repairs item upon collecting XP
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Mending extends EquipEnchantment {
	Mending() {
		super(1, EnchantmentType.MENDING);
		description.add(textNI("Upon collecting XP, repairs item by 2 durability per XP.", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Infinity.", NamedTextColor.RED));
	}

}

package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Prevents removal of wearables once put on
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class CurseOfBinding extends EquipEnchantment {
	CurseOfBinding() {
		super(1, EnchantmentType.BINDING_CURSE);
		description.add(textNI("Prevents item from being taken off once worn.", NamedTextColor.GRAY));
	}
}

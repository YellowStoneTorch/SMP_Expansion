package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * During a thunderstorm, casts lightning when trident is thrown
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Channeling extends EquipEnchantment {
	Channeling() {
		super(1, EnchantmentType.CHANNELING);
		description.add(textNI("During thunderstorms, casts lightning where thrown", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Riptide.", NamedTextColor.RED));
	}
}

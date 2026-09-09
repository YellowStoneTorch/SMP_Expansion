package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Makes items disappear upon death instead of dropping
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class CurseOfVanishing extends EquipEnchantment {
	CurseOfVanishing() {
		super(1, EnchantmentType.VANISHING_CURSE);
		description.add(textNI("Causes item to be destroyed upon death instead of dropping", NamedTextColor.GRAY));
	}
}

package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Provides extra protection against explosion damage and reduces explosion knockback
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class BlastProtection extends EquipEnchantment {
	BlastProtection(int level) {
		super(level, EnchantmentType.BLAST_PROTECTION);
		description.add(textNI("Reduces explosion damage by " + (8 * level) + "% and", NamedTextColor.GRAY));
		description.add(textNI("reduces explosion knockback by " + (15 * level) + "%", NamedTextColor.GRAY));
		description.add(textNI("Incompatible with Reinforcement,", NamedTextColor.RED));
		description.add(textNI("Protection, and Fire Protection.", NamedTextColor.RED));
	}
}

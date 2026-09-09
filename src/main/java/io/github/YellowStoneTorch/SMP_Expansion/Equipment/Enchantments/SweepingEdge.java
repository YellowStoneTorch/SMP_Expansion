package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.fmt2;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases sweeping attack damage
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class SweepingEdge extends EquipEnchantment {
	SweepingEdge(int level) {
		super(level, EnchantmentType.SWEEPING_EDGE);
		description.add(textNI("Increases sweeping attack damage", NamedTextColor.GRAY));
		description.add(textNI("by " + fmt2.format(100 * (level / (level + 1.0))) + "% of weapon damage", NamedTextColor.GRAY));
	}

}

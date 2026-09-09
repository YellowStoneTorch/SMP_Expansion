package io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments;

import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Increases underwater breathing time
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Respiration extends EquipEnchantment {
	Respiration(int level) {
		super(level, EnchantmentType.RESPIRATION);
		description.add(textNI("Increases underwater breath by " + (15 * level) + " seconds and", NamedTextColor.GRAY));
		description.add(textNI("reduces chance of drowning damage by " + (100 * (level / (level + 1.0))) + "%", NamedTextColor.GRAY));
	}

}

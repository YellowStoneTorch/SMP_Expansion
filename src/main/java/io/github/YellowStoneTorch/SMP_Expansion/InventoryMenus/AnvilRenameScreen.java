package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

/**
 * Screen for entering new text for renaming items using vanilla UI
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
@SuppressWarnings("UnstableApiUsage")
public class AnvilRenameScreen extends ModifiedVanillaMenu {

	/**
	 * Creates inventory menu
	 * @param player player to open menu for
	 * @param item item to rename
	 * @param color color of new name
	 * @param decorations decorations of new name
	 */
	public AnvilRenameScreen(@NotNull Player player, @NotNull ItemStack item, @NotNull NamedTextColor color, @NotNull TextDecoration[] decorations) {
		super(MenuType.ANVIL.create(player, text("Enter Item Name", color, decorations)));
		inventory.setItem(0, item);
	}
}

package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * Menus that open a vanilla interface, but have custom functionality that must be implemented outside of the class
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public abstract class ModifiedVanillaMenu {
	protected final Inventory inventory;
	protected final InventoryView inventoryView;

	/**
	 * Constructs inventory menu from a vanilla inventory screen
	 * @param inventoryView created inventory view
	 */
	ModifiedVanillaMenu(@NotNull InventoryView inventoryView) {
		this.inventory = inventoryView.getTopInventory();
		this.inventoryView = inventoryView;
	}

	/**
	 * Gets the inventory view
	 * @return inventory view
	 */
	public InventoryView getInventoryView() {
		return inventoryView;
	}
}

package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;

/**
 * Gives onClick functionality to all inventory menus
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public abstract class InventoryMenu implements InventoryHolder {
	protected static final ItemStack empty;
	protected static final ItemStack outputSlot;
	protected static final ItemStack back;

	static {
		empty = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE, 1);
		empty.editMeta(data -> data.customName(Component.empty()));
		outputSlot = ItemStack.of(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
		outputSlot.editMeta(data -> data.customName(Component.empty()));
		back = ItemStack.of(Material.BARRIER, 1);
		back.editMeta(data -> data.customName(textNI("Back", NamedTextColor.YELLOW, TextDecoration.BOLD)));
	}

	protected final Inventory inventory;

	/**
	 * Constructs inventory menu and fills it with empty slots, to be edited later
	 * @param size size of inventory
	 * @param name name of inventory
	 */
	InventoryMenu(int size, Component name) {
		inventory = Bukkit.createInventory(this, size, name);
		for (int i = 0; i < size; i++)
			inventory.setItem(i, empty);
	}

	/**
	 * Gets the inventory to display to players
	 * @return inventory
	 */
	@NotNull
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Provides click functionality for inventory menus
	 * @param event InventoryClickEvent
	 */
	public abstract void onClick(InventoryClickEvent event);

	/**
	 * Runs code upon dragging items, mainly to ensure menu is updated properly
	 * @param event InventoryDragEvent
	 */
	public void onDrag(InventoryDragEvent event) {}

	/**
	 * Runs code upon closing the menu, mainly to return input items
	 */
	public void onClose() {}
}

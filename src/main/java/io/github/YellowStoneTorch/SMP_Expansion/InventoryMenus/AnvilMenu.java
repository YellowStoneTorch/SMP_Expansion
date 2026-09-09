package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;
import static net.kyori.adventure.text.Component.text;

/**
 * Menu for selecting which action to do in an anvil
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class AnvilMenu extends InventoryMenu {
	private static final ItemStack rename;
	private static final ItemStack repair;

	static {
		ArrayList<TextComponent> lore = new ArrayList<>();
		rename = ItemStack.of(Material.NAME_TAG, 1);
		rename.editMeta(data ->
		{
			data.customName(textNI("Rename Items", NamedTextColor.YELLOW, TextDecoration.BOLD));
			lore.add(Component.empty());
			lore.add(textNI("Click here to rename items with extra formatting options.", NamedTextColor.GRAY));
			data.lore(lore);
		});
		repair = ItemStack.of(Material.ANVIL, 1);
		repair.editMeta(data ->
		{
			data.customName(textNI("Repair and Combine Items", NamedTextColor.YELLOW, TextDecoration.BOLD));
			lore.clear();
			lore.add(Component.empty());
			lore.add(textNI("Click here to repair or combine items on this anvil.", NamedTextColor.GRAY));
			data.lore(lore);
		});
	}

	private final Block block;

	/**
	 * Creates inventory menu
	 * @param block anvil that this menu is tied to
	 */
	public AnvilMenu(Block block) {
		super(27, text("Anvil"));
		inventory.setItem(11, rename);
		inventory.setItem(15, repair);
		this.block = block;
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player)event.getWhoClicked();
		switch (event.getRawSlot()) {
			case 11 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new AnvilRenameMenu().getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
			case 15 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new AnvilCombineMenu(block).getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
		}
	}
}

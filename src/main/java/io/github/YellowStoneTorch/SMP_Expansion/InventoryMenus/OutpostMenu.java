package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.Claims.OutpostCollection;
import io.github.YellowStoneTorch.SMP_Expansion.Claims.Permission;
import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;
import static net.kyori.adventure.text.Component.text;

/**
 * Menu for managing outpost permissions
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class OutpostMenu extends InventoryMenu {
	private final OutpostCollection outpost;

	/**
	 * Creates inventory menu
	 * @param outpost outpost to open menu for
	 */
	OutpostMenu(@NotNull OutpostCollection outpost) {
		super(36, text(outpost.getName().content()));
		this.outpost = outpost;
		displayIcons();
		inventory.setItem(28, back);
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player)event.getWhoClicked();
		switch (event.getRawSlot()) {
			case 10 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				switch (outpost.permsTeleport()) {
					case ALL ->
							outpost.setTeleport(Permission.TEAMMATES);
					case TEAMMATES ->
							outpost.setTeleport(Permission.NONE);
					case NONE ->
							outpost.setTeleport(Permission.ALL);
				}
				displayIcons();
			}
			case 12 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				switch (outpost.permsUseSwitches()) {
					case ALL ->
							outpost.setUseSwitches(Permission.TEAMMATES);
					case TEAMMATES ->
							outpost.setUseSwitches(Permission.NONE);
					case NONE ->
							outpost.setUseSwitches(Permission.ALL);
				}
				displayIcons();
			}
			case 14 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				switch (outpost.permsOpenContainers()) {
					case ALL ->
							outpost.setOpenContainers(Permission.TEAMMATES);
					case TEAMMATES ->
							outpost.setOpenContainers(Permission.NONE);
					case NONE ->
							outpost.setOpenContainers(Permission.ALL);
				}
				displayIcons();
			}
			case 16 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				switch (outpost.permsEditWorld()) {
					case ALL ->
							outpost.setEditWorld(Permission.TEAMMATES);
					case TEAMMATES ->
							outpost.setEditWorld(Permission.NONE);
					case NONE ->
							outpost.setEditWorld(Permission.ALL);
				}
				displayIcons();
			}
			case 28 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new ClaimsMenu(player).getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
		}
	}

	/**
	 * Sets the icons to display based on current outpost settings
	 */
	private void displayIcons() {
		inventory.setItem(10, getTeleportIcon(outpost.permsTeleport()));
		inventory.setItem(12, getSwitchesIcon(outpost.permsUseSwitches()));
		inventory.setItem(14, getContainersIcon(outpost.permsOpenContainers()));
		inventory.setItem(16, getEditWorldIcon(outpost.permsEditWorld()));
	}

	/**
	 * Gets the icon to display for the teleportation setting
	 * @param permission current permission setting
	 * @return icon
	 */
	@NotNull
	private ItemStack getTeleportIcon(Permission permission) {
		ItemStack icon = ItemStack.of(Material.ENDER_PEARL, 1);
		icon.editMeta(data ->
		{
			data.displayName(textNI("Teleportation", NamedTextColor.GREEN, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Whether players can use ender pearls and chorus fruit.", NamedTextColor.GRAY));
			lore.add(Component.empty());
			lore.add(textNI("Allowed for:", NamedTextColor.WHITE));
			lore.add(textNI(permission.toString(), NamedTextColor.GREEN));
			lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			data.lore(lore);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for the switches setting
	 * @param permission current permission setting
	 * @return icon
	 */
	@NotNull
	private ItemStack getSwitchesIcon(Permission permission) {
		ItemStack icon = ItemStack.of(Material.OAK_DOOR, 1);
		icon.editMeta(data ->
		{
			data.displayName(textNI("Use Switches", NamedTextColor.GREEN, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Whether players can use buttons, levers, doors, etc.", NamedTextColor.GRAY));
			lore.add(Component.empty());
			lore.add(textNI("Allowed for:", NamedTextColor.WHITE));
			lore.add(textNI(permission.toString(), NamedTextColor.GREEN));
			lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			data.lore(lore);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for the containers setting
	 * @param permission current permission setting
	 * @return icon
	 */
	@NotNull
	private ItemStack getContainersIcon(Permission permission) {
		ItemStack icon = ItemStack.of(Material.CHEST, 1);
		icon.editMeta(data ->
		{
			data.displayName(textNI("Open Containers", NamedTextColor.GREEN, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Whether players can open any kind of container.", NamedTextColor.GRAY));
			lore.add(Component.empty());
			lore.add(textNI("Allowed for:", NamedTextColor.WHITE));
			lore.add(textNI(permission.toString(), NamedTextColor.GREEN));
			lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			data.lore(lore);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for the edit world setting
	 * @param permission current permission setting
	 * @return icon
	 */
	@NotNull
	private ItemStack getEditWorldIcon(Permission permission) {
		ItemStack icon = ItemStack.of(Material.IRON_PICKAXE, 1);
		icon.editMeta(data ->
		{
			data.displayName(textNI("Edit World", NamedTextColor.GREEN, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Whether players can place, edit, or break blocks.", NamedTextColor.GRAY));
			lore.add(Component.empty());
			lore.add(textNI("Allowed for:", NamedTextColor.WHITE));
			lore.add(textNI(permission.toString(), NamedTextColor.GREEN));
			lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			data.lore(lore);
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		return icon;
	}
}

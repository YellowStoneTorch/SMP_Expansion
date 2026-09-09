package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.Claims.ClaimCollection;
import io.github.YellowStoneTorch.SMP_Expansion.Claims.ClaimManager;
import io.github.YellowStoneTorch.SMP_Expansion.Claims.OutpostCollection;
import io.github.YellowStoneTorch.SMP_Expansion.Config;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.money;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;
import static net.kyori.adventure.text.Component.text;

/**
 * Menu for viewing claim information
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class ClaimsMenu extends InventoryMenu {
	private static final ItemStack map;
	private static final int[] outpostSlots;

	static {
		map = ItemStack.of(Material.MAP, 1);
		map.editMeta(data -> data.customName(textNI("Click to view outpost map", NamedTextColor.YELLOW, TextDecoration.BOLD)));
		outpostSlots = new int[]{19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
	}

	private final ArrayList<Integer> currentOutpostSlots;

	/**
	 * Creates inventory menu
	 * @param player player to display menu to
	 */
	public ClaimsMenu(@NotNull Player player) {
		super(54, text("Claims"));
		PersistentDataContainer playerData = player.getPersistentDataContainer();
		ClaimCollection claims = ClaimManager.getPlayerClaims(player);
		int balance = playerData.getOrDefault(money, PersistentDataType.INTEGER, 0);
		currentOutpostSlots = new ArrayList<>();
		inventory.setItem(11, map);
		inventory.setItem(13, getCommandInfoIcon());
		inventory.setItem(15, getOutpostsIcon(claims.getClaimedOutposts().size(), claims.getOutposts(), balance));
		inventory.setItem(16, getClaimsIcon(claims.getClaims(), balance));
		for (int i = 0; i < claims.getClaimedOutposts().size() || i >= 21; i++) {
			inventory.setItem(outpostSlots[i], getOutpostIcon(claims.getClaimedOutposts().get(i)));
			currentOutpostSlots.add(outpostSlots[i]);
		}
		inventory.setItem(46, back);
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player)event.getWhoClicked();
		PersistentDataContainer playerData = player.getPersistentDataContainer();
		ClaimCollection claims = ClaimManager.getPlayerClaims(player);
		int slot = event.getRawSlot();
		switch (slot) {
			case 11 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new ClaimMapMenu(player.getChunk()).getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
			case 15 -> {
				int balance = playerData.getOrDefault(money, PersistentDataType.INTEGER, 0);
				if (event.isShiftClick() && balance >= 1000 && claims.getClaimedOutposts().size() + claims.getOutposts() < 21) {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					playerData.set(money, PersistentDataType.INTEGER, balance - 1000);
					claims.incOutposts(1);
					inventory.setItem(15, getOutpostsIcon(claims.getClaimedOutposts().size(), claims.getOutposts(), balance - 1000));
					inventory.setItem(16, getClaimsIcon(claims.getClaims(), balance - 1000));
				}
			}
			case 16 -> {
				int balance = playerData.getOrDefault(money, PersistentDataType.INTEGER, 0);
				if (event.isShiftClick() && balance >= 100) {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					playerData.set(money, PersistentDataType.INTEGER, balance - 100);
					claims.incClaims(1);
					inventory.setItem(15, getOutpostsIcon(claims.getClaimedOutposts().size(), claims.getOutposts(), balance - 100));
					inventory.setItem(16, getClaimsIcon(claims.getClaims(), balance - 100));
				}
			}
			case 46 -> {
				player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(new MainMenu().getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
			default -> {
				if (currentOutpostSlots.contains(slot)) {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					new BukkitRunnable() {
						@Override
						public void run() {
							player.openInventory(new OutpostMenu(claims.getClaimedOutposts().get(currentOutpostSlots.indexOf(slot))).getInventory());
						}
					}.runTask(SMP_Expansion.getPlugin());
				}
			}
		}
	}

	/**
	 * Gets the icon to display command info
	 * @return command info icon
	 */
	@NotNull
	private ItemStack getCommandInfoIcon() {
		ItemStack icon = ItemStack.of(Material.WHITE_BANNER, 1);
		icon.editMeta(data -> {
			data.customName(textNI("Claim Commands", NamedTextColor.YELLOW, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("/claim add outpost [outpost name] [outpost color]", NamedTextColor.WHITE));
			lore.add(textNI("Places a new outpost in the current chunk with the given name and color.", NamedTextColor.GRAY));
			if (!Config.getAllowClaims())
				lore.add(textNI("Note: Claiming new outposts has been disabled by the server.", NamedTextColor.YELLOW));
			lore.add(Component.empty());
			lore.add(textNI("/claim add chunk [outpost name]", NamedTextColor.WHITE));
			lore.add(textNI("Adds this chunk to the given outpost.", NamedTextColor.GRAY));
			if (!Config.getAllowClaims())
				lore.add(textNI("Note: Claiming new chunks has been disabled by the server.", NamedTextColor.YELLOW));
			lore.add(Component.empty());
			lore.add(textNI("/claim list", NamedTextColor.WHITE));
			lore.add(textNI("Lists all placed outposts.", NamedTextColor.GRAY));
			lore.add(Component.empty());
			lore.add(textNI("/claim remove [confirmation]", NamedTextColor.WHITE));
			lore.add(textNI("If this chunk is an outpost and [confirmation] is the outpost name, removes this outpost.", NamedTextColor.GRAY));
			lore.add(textNI("If this chunk is a claim and [confirmation] is \"confirm\", removes this claim.", NamedTextColor.GRAY));
			data.lore(lore);
		});
		return icon;
	}

	/**
	 * Gets the icon to display available outposts
	 * @param placed current number of placed outposts
	 * @param available current number of available outposts
	 * @param balance balance of player
	 * @return claims icon
	 */
	@NotNull
	private ItemStack getOutpostsIcon(int placed, int available, int balance) {
		ItemStack icon = ItemStack.of(Material.RED_BANNER, 1);
		icon.editMeta(data -> {
			data.customName(textNI("Outposts", NamedTextColor.YELLOW, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Outposts are used to claim chunks and protect them.", NamedTextColor.GRAY));
			lore.add(textNI("They may be placed in any unclaimed chunks.", NamedTextColor.GRAY));
			lore.add(Component.empty());
			lore.add(textNI("You have " + available + " outposts remaining.", NamedTextColor.GRAY));
			if (placed + available < 21) {
				lore.add(textNI("Shift Click to purchase 1 outpost ($1000)", NamedTextColor.YELLOW));
				lore.add(textNI("You have $" + balance, NamedTextColor.GREEN));
			}
			else
				lore.add(textNI("Maximum number of outposts owned", NamedTextColor.YELLOW));
			data.lore(lore);
		});
		return icon;
	}

	/**
	 * Gets the icon to display available claims
	 * @param available current number of available claims
	 * @param balance balance of player
	 * @return claims icon
	 */
	@NotNull
	private ItemStack getClaimsIcon(int available, int balance) {
		ItemStack icon = ItemStack.of(Material.RED_WOOL, 1);
		icon.editMeta(data -> {
			data.customName(textNI("Claims", NamedTextColor.YELLOW, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Claims are used to expand outposts.", NamedTextColor.GRAY));
			lore.add(textNI("They must be connected to an outpost.", NamedTextColor.GRAY));
			lore.add(Component.empty());
			lore.add(textNI("You have " + available + " claims remaining.", NamedTextColor.GRAY));
			lore.add(textNI("Shift Click to purchase 1 claim ($100)", NamedTextColor.YELLOW));
			lore.add(textNI("You have $" + balance, NamedTextColor.GREEN));
			data.lore(lore);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for a single outpost
	 * @param outpost outpost to display icon for
	 * @return icon
	 */
	@NotNull
	private ItemStack getOutpostIcon(@NotNull OutpostCollection outpost) {
		ItemStack icon = ItemStack.of(ClaimManager.getBanner(outpost.getColor()), 1);
		icon.editMeta(data -> {
			data.customName(outpost.getNameBuilder().decoration(TextDecoration.ITALIC, false).build());
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Size: " + (outpost.getClaimedChunks().size() + 1) + " chunks", NamedTextColor.WHITE));
			lore.add(Component.empty());
			lore.add(textNI("Teleportation: " + outpost.permsTeleport().toString(), NamedTextColor.GRAY));
			lore.add(textNI("Use Switches: " + outpost.permsUseSwitches().toString(), NamedTextColor.GRAY));
			lore.add(textNI("Open Containers: " + outpost.permsOpenContainers().toString(), NamedTextColor.GRAY));
			lore.add(textNI("Edit World: " + outpost.permsEditWorld().toString(), NamedTextColor.GRAY));
			lore.add(Component.empty());
			lore.add(textNI("Click to edit permissions", NamedTextColor.YELLOW));
			data.lore(lore);
		});
		return icon;
	}
}

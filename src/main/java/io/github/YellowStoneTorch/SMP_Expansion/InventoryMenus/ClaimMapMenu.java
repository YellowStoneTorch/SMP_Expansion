package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.Claims.ClaimManager;
import io.github.YellowStoneTorch.SMP_Expansion.Claims.OutpostCollection;
import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import io.github.YellowStoneTorch.SMP_Expansion.Teams.PlayerTeam;
import io.github.YellowStoneTorch.SMP_Expansion.Teams.TeamManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.*;
import static net.kyori.adventure.text.Component.text;

/**
 * Menu for displaying nearby outposts and claims
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class ClaimMapMenu extends InventoryMenu {

	public ClaimMapMenu(Chunk centerChunk) {
		super(54, text("Outpost Map"));
		displayIcons(centerChunk);
		inventory.setItem(46, back);
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player)event.getWhoClicked();
		int slot = event.getRawSlot();
		if (slot == 46) {
			player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
			new BukkitRunnable() {
				@Override
				public void run() {
					player.openInventory(new ClaimsMenu(player).getInventory());
				}
			}.runTask(SMP_Expansion.getPlugin());
		}
	}

	/**
	 * Displays the map for the player
	 * @param centerChunk the center chunk for the player
	 */
	private void displayIcons(@NotNull Chunk centerChunk) {
		int invSlot = 0;
		for (int i = -2; i < 3; i++) {
			for (int j = -4; j < 5; j++) {
				Chunk chunk = centerChunk.getWorld().getChunkAt(centerChunk.getX() + i, centerChunk.getZ() + j);
				inventory.setItem(invSlot, getOutpostIcon(chunk, i == 0 && j == 0));
				invSlot++;
			}
		}
	}

	/**
	 * Gets the icon to display for a certain chunk
	 * @param chunk chunk to display icon for
	 * @param isCenter whether this chunk is in the center of the map
	 * @return icon
	 */

	private ItemStack getOutpostIcon(@NotNull Chunk chunk, boolean isCenter) {
		OutpostCollection outpost = ClaimManager.getOutpost(chunk);
		ItemStack icon;
		if (outpost == null) {
			icon = ItemStack.of(Material.WHITE_STAINED_GLASS_PANE, 1);
			icon.editMeta(data -> {
				data.customName(textNI("Unclaimed Chunk", NamedTextColor.GRAY, TextDecoration.BOLD));
				ArrayList<TextComponent> lore = new ArrayList<>();
				lore.add(Component.empty());
				lore.add(textNI("Chunk coordinates: (" + chunk.getX() + ", " + chunk.getZ() + ")", NamedTextColor.WHITE));
				if (isCenter) {
					lore.add(Component.empty());
					lore.add(textNI("You are here", NamedTextColor.YELLOW));
				}
				data.lore(lore);
			});
			return icon;
		}
		else if (outpost.getOutpostChunk().getChunkKey() == chunk.getChunkKey())
			icon = ItemStack.of(ClaimManager.getBanner(outpost.getColor()));
		else
			icon = ItemStack.of(ClaimManager.getWool(outpost.getColor()));
		icon.editMeta(data -> {
			data.customName(outpost.getNameBuilder().decoration(TextDecoration.ITALIC, false).build());
			PlayerTeam team = TeamManager.getPlayerTeam(outpost.getOwner());
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (team != null)
				lore.add(build(compNI("Owner: ", NamedTextColor.WHITE), TeamManager.getPlayerName(outpost.getOwner()).decoration(TextDecoration.ITALIC, false)));
			else
				lore.add(textNI("Owner: " + outpost.getOwner().getName(), NamedTextColor.WHITE));
			lore.add(textNI("Size: " + (outpost.getClaimedChunks().size() + 1) + " chunks", NamedTextColor.WHITE));
			lore.add(textNI("Chunk coordinates: (" + chunk.getX() + ", " + chunk.getZ() + ")", NamedTextColor.WHITE));
			lore.add(Component.empty());
			lore.add(textNI("Teleportation: " + outpost.permsTeleport().toString(), NamedTextColor.GRAY));
			lore.add(textNI("Use Switches: " + outpost.permsUseSwitches().toString(), NamedTextColor.GRAY));
			lore.add(textNI("Open Containers: " + outpost.permsOpenContainers().toString(), NamedTextColor.GRAY));
			lore.add(textNI("Edit World: " + outpost.permsEditWorld().toString(), NamedTextColor.GRAY));
			if (isCenter) {
				lore.add(Component.empty());
				lore.add(textNI("You are here", NamedTextColor.YELLOW));
			}
			data.lore(lore);
		});
		return icon;
	}

}

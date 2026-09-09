package io.github.YellowStoneTorch.SMP_Expansion.Claims;

import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static io.github.YellowStoneTorch.SMP_Expansion.Claims.ClaimCollection.overworld;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.*;

/**
 * Manages player land claims
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class ClaimManager {
	static final File claimFiles;
	private static final HashMap<OfflinePlayer, ClaimCollection> playerClaims;
	private static final HashMap<Chunk, OutpostCollection> chunkClaims;
	private static final TextComponent.Builder prefix;

	static {
		playerClaims = new HashMap<>();
		chunkClaims = new HashMap<>();
		prefix = unite(comp("["), comp("Claims", NamedTextColor.GOLD, TextDecoration.BOLD), comp("] "));
		claimFiles = new File(Bukkit.getWorldContainer(), "plugins/SMP_Expansion/player_claims");
		if (!claimFiles.isDirectory())
			//noinspection ResultOfMethodCallIgnored
			claimFiles.mkdirs();
	}

	/**
	 * Loads all claims from their files
	 */
	public static void loadClaims() {
		try {
			String[] claims = claimFiles.list();
			if (claims == null)
				throw new IOException("Directory plugins/SMP_Expansion/player_claims not found");
			for (String path: claims) {
				File directory = new File(claimFiles, path);
				if (directory.isDirectory()) {
					ClaimCollection claimCollection = ClaimCollection.fromFile(directory);
					playerClaims.put(claimCollection.getOwner(), claimCollection);
					for (OutpostCollection outpostCollection: claimCollection.getClaimedOutposts()) {
						chunkClaims.put(outpostCollection.getOutpostChunk(), outpostCollection);
						for (Chunk chunk: outpostCollection.getClaimedChunks())
							chunkClaims.put(chunk, outpostCollection);
					}
				}
			}
		}
		catch (IOException e) {
			SMP_Expansion.loadIOException(e);
		}
	}

	/**
	 * Gets this player's claim collection, creating and saving a new one if necessary
	 * @param player player to get claim collection of
	 * @return claim collection, or null if an I/O exception occurs
	 */
	@NotNull
	public static ClaimCollection getPlayerClaims(@NotNull OfflinePlayer player) {
		ClaimCollection collection = playerClaims.get(player);
		if (collection == null) {
			ClaimCollection newCollection = new ClaimCollection(player);
			playerClaims.put(player, newCollection);
			return newCollection;
		}
		return collection;
	}

	/**
	 * Gets this chunk's outpost collection
	 * @param chunk chunk to get outpost collection of
	 * @return outpost collection, or null if not claimed
	 */
	@Nullable
	public static OutpostCollection getOutpost(@NotNull Chunk chunk) {
		return chunkClaims.get(chunk);
	}

	/**
	 * Attempts to claim a new outpost for the player
	 * @param name name of this new outpost
	 * @param color color of this new outpost
	 * @param player player to claim outpost for
	 * @param chunk chunk to host this outpost
	 */
	public static void claimOutpost(@NotNull String name, @NotNull NamedTextColor color, @NotNull Player player, @NotNull Chunk chunk) {
		ClaimCollection collection = getPlayerClaims(player);
		if (chunk.getWorld().getEnvironment() != World.Environment.NORMAL) {
			player.sendMessage(build(prefix, comp("You can't claim chunks in this world.", NamedTextColor.RED)));
			return;
		}
		if (getOutpost(chunk) != null) {
			player.sendMessage(build(prefix, comp("This chunk has already been claimed.", NamedTextColor.RED)));
			return;
		}
		if (collection.getOutposts() < 1) {
			player.sendMessage(build(prefix, comp("You don't have any remaining outposts. Purchase more in the claims menu.", NamedTextColor.RED)));
			return;
		}
		for (OutpostCollection outpost: collection.getClaimedOutposts())
			if (outpost.getName().content().equals(name)) {
				player.sendMessage(build(prefix, comp("You already have an outpost with this name.", NamedTextColor.RED)));
				return;
			}
		OutpostCollection newOutpost = new OutpostCollection(player, name, color, chunk, collection.getOutpostsDirectory());
		collection.addOutpost(newOutpost);
		chunkClaims.put(chunk, newOutpost);
		collection.incOutposts(-1);
		player.sendMessage(build(prefix, comp("Claimed new outpost: ", NamedTextColor.YELLOW), newOutpost.getNameBuilder(), comp(". You now have " + collection.getOutposts() + " outposts remaining.", NamedTextColor.YELLOW)));
	}

	/**
	 * Attempts to add a new chunk to an outpost
	 * @param name name of outpost selected
	 * @param player player to claim chunk for
	 * @param chunk chunk to claim
	 */
	public static void claimChunk(@NotNull String name, @NotNull Player player, @NotNull Chunk chunk) {
		ClaimCollection collection = getPlayerClaims(player);
		if (getOutpost(chunk) != null) {
			player.sendMessage(build(prefix, comp("This chunk has already been claimed.", NamedTextColor.RED)));
			return;
		}
		if (collection.getClaims() < 1) {
			player.sendMessage(build(prefix, comp("You don't have any remaining claims. Purchase more in the claims menu.", NamedTextColor.RED)));
			return;
		}
		OutpostCollection outpost = collection.getOutpost(name);
		if (outpost == null) {
			player.sendMessage(build(prefix, comp("You don't have an outpost with that name.", NamedTextColor.RED)));
			return;
		}
		int chunkX = chunk.getX();
		int chunkZ = chunk.getZ();
		Chunk up = overworld.getChunkAt(chunkX, chunkZ + 1);
		Chunk down = overworld.getChunkAt(chunkX, chunkZ - 1);
		Chunk right = overworld.getChunkAt(chunkX + 1, chunkZ);
		Chunk left = overworld.getChunkAt(chunkX - 1, chunkZ);
		if (getOutpost(up) == outpost || getOutpost(down) == outpost || getOutpost(right) == outpost || getOutpost(left) == outpost) {
			outpost.addChunk(chunk);
			chunkClaims.put(chunk, outpost);
			collection.incClaims(-1);
			player.sendMessage(build(prefix, comp("Claimed this chunk. You now have " + collection.getClaims() + " claims remaining.", NamedTextColor.YELLOW)));
		}
		else {
			player.sendMessage(build(prefix, comp("You need to be in a chunk adjacent to this outpost to claim it.", NamedTextColor.RED)));
		}

	}

	/**
	 * Lists all claimed outposts owned by the player
	 * @param player player to list claimed outposts for
	 */
	public static void listOutposts(@NotNull Player player) {
		ClaimCollection collection = getPlayerClaims(player);
		ArrayList<OutpostCollection> outposts = collection.getClaimedOutposts();
		if (outposts.isEmpty())
			player.sendMessage(build(prefix, comp("You don't have any claimed outposts.", NamedTextColor.YELLOW)));
		else {
			player.sendMessage(build(prefix, comp("Claimed outposts:", NamedTextColor.YELLOW)));
			for (OutpostCollection outpost: outposts) {
				player.sendMessage(build(outpost.getNameBuilder(), comp(" (" + (outpost.getClaimedChunks().size() + 1) + " chunks)", NamedTextColor.YELLOW)));
			}
		}
	}

	/**
	 * Attempts to remove a claimed chunk or outpost
	 * @param player player to remove chunk or outpost
	 * @param chunk chunk to remove claim
	 * @param confirmation whether the removal was confirmed
	 */
	public static void removeClaim(@NotNull Player player, @NotNull Chunk chunk, @NotNull String confirmation) {
		ClaimCollection collection = getPlayerClaims(player);
		OutpostCollection outpost = getOutpost(chunk);
		if (outpost == null) {
			player.sendMessage(build(prefix, comp("This chunk isn't claimed.", NamedTextColor.RED)));
			return;
		}
		if (!outpost.getOwner().equals(player)) {
			player.sendMessage(build(prefix, comp("You don't own this claim.", NamedTextColor.RED)));
			return;
		}
		if (outpost.getOutpostChunk().equals(chunk)) {
			if (confirmation.equals(outpost.getName().content())) {
				chunkClaims.remove(chunk);
				ArrayList<Chunk> chunks = outpost.getClaimedChunks();
				int removedChunks = chunks.size();
				for (Chunk remove: chunks)
					chunkClaims.remove(remove);
				collection.removeOutpost(outpost);
				collection.incOutposts(1);
				collection.incClaims(removedChunks);
				player.sendMessage(build(prefix, comp("Removed outpost ", NamedTextColor.YELLOW), outpost.getNameBuilder(), comp(". You now have " + collection.getOutposts() + " outposts remaining.", NamedTextColor.YELLOW)));
				if (removedChunks > 0)
					player.sendMessage(build(prefix, comp("Removed an additional " + removedChunks + " chunks. You now have " + collection.getClaims() + " claims remaining.", NamedTextColor.YELLOW)));
			}
			else
				player.sendMessage(build(prefix, comp("Type ", NamedTextColor.RED), comp(outpost.getName().content()), comp(" in confirmation to confirm removal of this outpost.", NamedTextColor.RED)));
		}
		else {
			if (confirmation.equals("confirm")) {
				chunkClaims.remove(chunk);
				outpost.removeChunk(chunk);
				ArrayList<Chunk> disconnected = verifyChunks(outpost.getOutpostChunk(), outpost.getClaimedChunks());
				for (Chunk remove: disconnected) {
					chunkClaims.remove(remove);
					outpost.removeChunk(remove);
				}
				collection.incClaims(disconnected.size() + 1);
				if (disconnected.isEmpty())
					player.sendMessage(build(prefix, comp("Removed claim on this chunk. You now have " + collection.getClaims() + " claims remaining.", NamedTextColor.YELLOW)));
				else
					player.sendMessage(build(prefix, comp("Removed claim on this chunk. Also removed an additional " + disconnected.size() + " chunks that were disconnected. You now have " + collection.getClaims() + " claims remaining.", NamedTextColor.YELLOW)));
			}
			else
				player.sendMessage(build(prefix, comp("Type \"", NamedTextColor.RED), comp("confirm"), comp("\" in confirmation to confirm removal of this chunk.", NamedTextColor.RED)));
		}
	}

	/**
	 * Gets the banner for this color
	 * @param color color to get banner for
	 * @return banner
	 */
	public static Material getBanner(@NotNull TextColor color) {
		return switch (color.asHexString()) {
			case "#AA0000" ->
					Material.BROWN_BANNER;
			case "#FF5555" ->
					Material.RED_BANNER;
			case "#FFAA00" ->
					Material.ORANGE_BANNER;
			case "#FFFF55" ->
					Material.YELLOW_BANNER;
			case "#55FF55" ->
					Material.LIME_BANNER;
			case "#00AA00" ->
					Material.GREEN_BANNER;
			case "#00AAAA" ->
					Material.CYAN_BANNER;
			case "#55FFFF" ->
					Material.LIGHT_BLUE_BANNER;
			case "#5555FF" ->
					Material.BLUE_BANNER;
			case "#0000AA" ->
					Material.PURPLE_BANNER;
			case "#AA00AA" ->
					Material.MAGENTA_BANNER;
			case "#FF55FF" ->
					Material.PINK_BANNER;
			case "#AAAAAA" ->
					Material.LIGHT_GRAY_BANNER;
			case "#555555" ->
					Material.GRAY_BANNER;
			case "#000000" ->
					Material.BLACK_BANNER;
			default ->
					Material.WHITE_BANNER;
		};
	}

	/**
	 * Gets the wool for this color
	 * @param color color to get banner for
	 * @return banner
	 */
	public static Material getWool(@NotNull TextColor color) {
		return switch (color.asHexString()) {
			case "#AA0000" ->
					Material.BROWN_WOOL;
			case "#FF5555" ->
					Material.RED_WOOL;
			case "#FFAA00" ->
					Material.ORANGE_WOOL;
			case "#FFFF55" ->
					Material.YELLOW_WOOL;
			case "#55FF55" ->
					Material.LIME_WOOL;
			case "#00AA00" ->
					Material.GREEN_WOOL;
			case "#00AAAA" ->
					Material.CYAN_WOOL;
			case "#55FFFF" ->
					Material.LIGHT_BLUE_WOOL;
			case "#5555FF" ->
					Material.BLUE_WOOL;
			case "#0000AA" ->
					Material.PURPLE_WOOL;
			case "#AA00AA" ->
					Material.MAGENTA_WOOL;
			case "#FF55FF" ->
					Material.PINK_WOOL;
			case "#AAAAAA" ->
					Material.LIGHT_GRAY_WOOL;
			case "#555555" ->
					Material.GRAY_WOOL;
			case "#000000" ->
					Material.BLACK_WOOL;
			default ->
					Material.WHITE_WOOL;
		};
	}

	/**
	 * Gets a list of all claimed chunks that aren't connected to their outpost for removal
	 * @param outpost outpost chunk
	 * @param claims all claims tied to this outpost
	 * @return list of removed chunks
	 */
	@NotNull
	private static ArrayList<Chunk> verifyChunks(@NotNull Chunk outpost, @NotNull ArrayList<Chunk> claims) {
		ArrayList<Chunk> found = new ArrayList<>();
		found.add(outpost);
		search(0, overworld.getChunkAt(outpost.getX(), outpost.getZ() - 1), claims, found);
		search(1, overworld.getChunkAt(outpost.getX() + 1, outpost.getZ()), claims, found);
		search(2, overworld.getChunkAt(outpost.getX(), outpost.getZ() + 1), claims, found);
		search(3, overworld.getChunkAt(outpost.getX() - 1, outpost.getZ()), claims, found);
		ArrayList<Chunk> removedChunks = new ArrayList<>();
		for (Chunk chunk: claims)
			if (!found.contains(chunk))
				removedChunks.add(chunk);
		return removedChunks;
	}

	/**
	 * Compiles a list of all chunks attached to this outpost chunk
	 * @param direction direction that the search is proceeding (0-north, 1-east, etc.)
	 * @param check chunk to be checked
	 * @param claims all claims tied to this outpost
	 * @param found all claims that currently have been found (new chunks will be added)
	 */
	private static void search(int direction, @NotNull Chunk check, @NotNull ArrayList<Chunk> claims, @NotNull ArrayList<Chunk> found) {
		if (claims.contains(check) && !found.contains(check)) {
			found.add(check);
			if (direction != 0)
				search(2, overworld.getChunkAt(check.getX(), check.getZ() + 1), claims, found);
			if (direction != 1)
				search(3, overworld.getChunkAt(check.getX() - 1, check.getZ()), claims, found);
			if (direction != 2)
				search(0, overworld.getChunkAt(check.getX(), check.getZ() - 1), claims, found);
			if (direction != 3)
				search(1, overworld.getChunkAt(check.getX() + 1, check.getZ()), claims, found);
		}
	}
}

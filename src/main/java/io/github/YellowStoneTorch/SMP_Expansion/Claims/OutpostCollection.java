package io.github.YellowStoneTorch.SMP_Expansion.Claims;

import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static io.github.YellowStoneTorch.SMP_Expansion.Claims.ClaimCollection.overworld;
import static io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion.GSON;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.comp;

/**
 * A representation of claimed chunks tied to an outpost
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class OutpostCollection {
	private final OfflinePlayer owner;
	private final TextComponent.Builder name;
	private final NamedTextColor color;
	private final Chunk outpostChunk;
	private final ArrayList<Chunk> claimedChunks;
	private final File outpostFile;
	private Permission teleport;
	private Permission useSwitches;
	private Permission openContainers;
	private Permission editWorld;

	/**
	 * Creates an outpost collection with default options
	 * @param owner owner of this outpost collection
	 * @param name name of this outpost collection
	 * @param color color of this outpost collection
	 * @param outpostChunk chunk of the outpost
	 * @param directory directory of this player's outpost files
	 */
	OutpostCollection(@NotNull OfflinePlayer owner, @NotNull String name, @NotNull NamedTextColor color, @NotNull Chunk outpostChunk, @NotNull File directory) {
		this.owner = owner;
		this.name = comp(name, color, TextDecoration.BOLD);
		this.color = color;
		this.outpostChunk = outpostChunk;
		this.claimedChunks = new ArrayList<>();
		outpostFile = new File(directory, name + ".json");
		teleport = Permission.TEAMMATES;
		useSwitches = Permission.TEAMMATES;
		openContainers = Permission.TEAMMATES;
		editWorld = Permission.TEAMMATES;
		saveToFile();
	}

	/**
	 * Creates an outpost collection from raw form
	 * @param collection raw outpost collection
	 * @param directory directory of this player's outpost files
	 */
	private OutpostCollection(@NotNull RawOutputCollection collection, @NotNull File directory) {
		owner = Bukkit.getOfflinePlayer(UUID.fromString(collection.owner));
		name = comp(collection.name, TextColor.color(collection.color), TextDecoration.BOLD);
		color = NamedTextColor.nearestTo(TextColor.color(collection.color));
		outpostChunk = overworld.getChunkAt(collection.outpostChunk);
		claimedChunks = new ArrayList<>();
		for (long key: collection.claimedChunks)
			claimedChunks.add(overworld.getChunkAt(key));
		outpostFile = new File(directory, name.content() + ".json");
		teleport = Permission.getPermission(collection.teleport);
		useSwitches = Permission.getPermission(collection.useSwitches);
		openContainers = Permission.getPermission(collection.openContainers);
		editWorld = Permission.getPermission(collection.editWorld);
	}

	/**
	 * Gets an outpost collection from its file if it already exists
	 * @param file file to get outpost collection from
	 * @return outpost collection from file
	 * @throws IOException if an I/O exception occurs
	 */
	@NotNull
	static OutpostCollection fromFile(@NotNull File file) throws IOException {
		try (FileReader reader = new FileReader(file)) {
			return new OutpostCollection(GSON.fromJson(reader, RawOutputCollection.class), file.getParentFile());
		}
	}

	/**
	 * Gets the outpost name as a text component
	 * @return outpost name
	 */
	public TextComponent getName() {
		return name.build();
	}

	/**
	 * Gets the outpost name as a text component
	 * @return outpost name
	 */
	public TextComponent.Builder getNameBuilder() {
		return name;
	}

	/**
	 * Gets the color of this outpost
	 * @return color of outpost
	 */
	public NamedTextColor getColor() {
		return color;
	}

	/**
	 * Gets the owner of this outpost
	 * @return outpost owner
	 */
	public OfflinePlayer getOwner() {
		return owner;
	}

	/**
	 * Gets the chunk containing the outpost
	 * @return chunk containing the outpost
	 */
	public Chunk getOutpostChunk() {
		return outpostChunk;
	}

	/**
	 * Gets an immutable list containing all other chunks tied to this outpost
	 * @return immutable list of claimed chunks
	 */
	public ArrayList<Chunk> getClaimedChunks() {
		return new ArrayList<>(claimedChunks);
	}

	/**
	 * Gets the permissions for teleporting using ender pearls and chorus fruits
	 * @return permissions for teleporting
	 */
	public Permission permsTeleport() {
		return teleport;
	}

	/**
	 * Gets the permissions for using switches, like buttons and doors, within claim
	 * @return permissions for using switches
	 */
	public Permission permsUseSwitches() {
		return useSwitches;
	}

	/**
	 * Gets the permissions for opening chests, furnaces, etc. within claim
	 * @return permissions for opening containers
	 */
	public Permission permsOpenContainers() {
		return openContainers;
	}

	/**
	 * Gets the permissions for placing/breaking blocks and interacting with mobs within claim
	 * @return permissions for editing world
	 */
	public Permission permsEditWorld() {
		return editWorld;
	}

	/**
	 * Sets the permissions for teleporting using ender pearls and chorus fruits
	 * @param teleport permission
	 */
	public void setTeleport(Permission teleport) {
		this.teleport = teleport;
		saveToFile();
	}

	/**
	 * Sets the permissions for using switches, like buttons and doors, within claim
	 * @param useSwitches permission
	 */
	public void setUseSwitches(Permission useSwitches) {
		this.useSwitches = useSwitches;
		saveToFile();
	}

	/**
	 * Sets the permissions for opening chests, furnaces, etc. within claim
	 * @param openContainers permission
	 */
	public void setOpenContainers(Permission openContainers) {
		this.openContainers = openContainers;
		saveToFile();
	}

	/**
	 * Sets the permissions for placing/breaking blocks and interacting with mobs within claim
	 * @param editWorld permission
	 */
	public void setEditWorld(Permission editWorld) {
		this.editWorld = editWorld;
		saveToFile();
	}

	/**
	 * Saves this outpost collection to its file
	 */
	void saveToFile() {
		try (FileWriter writer = new FileWriter(outpostFile)) {
			GSON.toJson(new RawOutputCollection(this), writer);
		}
		catch (Exception e) {
			SMP_Expansion.saveIOException(new IOException("Something went wrong when saving an outpost collection", e));
		}
	}

	/**
	 * Adds a chunk to this outpost collection
	 * @param chunk chunk to add
	 */
	void addChunk(Chunk chunk) {
		claimedChunks.add(chunk);
		saveToFile();
	}

	/**
	 * Removes a chunk from this outpost collection
	 * @param chunk chunk to remove
	 */
	void removeChunk(Chunk chunk) {
		claimedChunks.remove(chunk);
		saveToFile();
	}

	/**
	 * Deletes the outpost file associated with this outpost
	 */
	void deleteOutpost() {
		//noinspection ResultOfMethodCallIgnored
		outpostFile.delete();
	}

	/**
	 * Converts outpost collection into raw form for storage
	 * @author YellowStoneTorch
	 * @version 0.1.0-ALPHA
	 */
	private static class RawOutputCollection {
		private final String name;
		private final int color;
		private final String owner;
		private final long outpostChunk;
		private final long[] claimedChunks;
		int teleport;
		int useSwitches;
		int openContainers;
		int editWorld;

		/**
		 * Converts outpost collection into raw form for storage
		 * @param collection collection to convert
		 */
		RawOutputCollection(@NotNull OutpostCollection collection) {
			TextComponent collectionName = collection.getName();
			name = collectionName.content();
			color = collection.color.value();
			owner = collection.owner.getUniqueId().toString();
			outpostChunk = collection.outpostChunk.getChunkKey();
			claimedChunks = new long[collection.claimedChunks.size()];
			ArrayList<Chunk> claimedChunkList = collection.claimedChunks;
			for (int i = 0; i < claimedChunks.length; i++)
				claimedChunks[i] = claimedChunkList.get(i).getChunkKey();
			teleport = collection.teleport.data;
			useSwitches = collection.useSwitches.data;
			openContainers = collection.openContainers.data;
			editWorld = collection.editWorld.data;
		}
	}
}

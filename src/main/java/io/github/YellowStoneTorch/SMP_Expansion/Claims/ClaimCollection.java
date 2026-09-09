package io.github.YellowStoneTorch.SMP_Expansion.Claims;

import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static io.github.YellowStoneTorch.SMP_Expansion.Claims.ClaimManager.claimFiles;
import static io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion.GSON;

/**
 * Holds all claimed outposts of a player
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class ClaimCollection {
	static final World overworld;

	static {
		overworld = Bukkit.getServer().getRespawnWorld();
	}

	private final OfflinePlayer owner;
	private final ArrayList<OutpostCollection> claimedOutposts;
	private final File claimFileDirectory;
	private final File outpostFileDirectory;
	private final File claimCollectionFile;
	private int outposts;
	private int claims;

	/**
	 * Creates an empty claim collection for a specified player with default values
	 * @param owner player to create claim collection for
	 */
	ClaimCollection(@NotNull OfflinePlayer owner) {
		this.owner = owner;
		this.claimedOutposts = new ArrayList<>();
		claimFileDirectory = new File(claimFiles, owner.getUniqueId().toString());
		if (!claimFileDirectory.isDirectory())
			//noinspection ResultOfMethodCallIgnored
			claimFileDirectory.mkdirs();
		outpostFileDirectory = new File(claimFileDirectory, "outposts");
		if (!outpostFileDirectory.isDirectory())
			//noinspection ResultOfMethodCallIgnored
			outpostFileDirectory.mkdirs();
		claimCollectionFile = new File(claimFileDirectory, owner.getUniqueId() + ".json");
		outposts = 1;
		claims = 24;
		saveToFile(true);
	}

	/**
	 * Creates a claim collection from raw form
	 * @param collection raw claim collection
	 * @param claimedOutposts separately loaded list of claimed outposts
	 */
	private ClaimCollection(@NotNull RawClaimCollection collection, @NotNull ArrayList<OutpostCollection> claimedOutposts) {
		owner = Bukkit.getOfflinePlayer(UUID.fromString(collection.owner));
		this.claimedOutposts = claimedOutposts;
		claimFileDirectory = new File(claimFiles, owner.getUniqueId().toString());
		if (!claimFileDirectory.isDirectory())
			//noinspection ResultOfMethodCallIgnored
			claimFileDirectory.mkdirs();
		outpostFileDirectory = new File(claimFileDirectory, "outposts");
		if (!outpostFileDirectory.isDirectory())
			//noinspection ResultOfMethodCallIgnored
			outpostFileDirectory.mkdirs();
		claimCollectionFile = new File(claimFileDirectory, owner.getUniqueId() + ".json");
		outposts = collection.outposts;
		claims = collection.claims;
	}

	/**
	 * Gets a claim collection from its files including its outposts
	 * @param directory directory of player's claim and outpost collections
	 * @return claim collection from file, or null if unable to load from file
	 * @throws IOException if an I/O exception occurs
	 */
	@NotNull
	static ClaimCollection fromFile(@NotNull File directory) throws IOException {
		File[] claimFiles = directory.listFiles();
		File outpostsDirectory = new File(directory, "outposts");
		File[] outpostFiles = outpostsDirectory.listFiles();
		if (claimFiles == null)
			throw new IOException("Directory " + directory.getPath() + " not found");
		if (claimFiles.length == 0)
			throw new IOException("File " + directory.getName() + ".json not found");
		if (outpostFiles == null)
			throw new IOException("Directory " + outpostsDirectory.getPath() + " not found");
		ArrayList<OutpostCollection> outposts = new ArrayList<>();
		for (File outpostFile: outpostFiles)
			outposts.add(OutpostCollection.fromFile(outpostFile));
		String fileName = directory.getName() + ".json";
		for (File file: claimFiles)
			if (file.getName().equals(fileName))
				try (FileReader reader = new FileReader(claimFiles[0])) {
					return new ClaimCollection(GSON.fromJson(reader, RawClaimCollection.class), outposts);
				}
		throw new IOException("File " + fileName + " not found");
	}

	/**
	 * Saves this claim collection to its file if it already exists
	 * @param allFiles whether to save all outpost files as well
	 */
	void saveToFile(boolean allFiles) {
		try (FileWriter writer = new FileWriter(claimCollectionFile)) {
			if (allFiles)
				for (OutpostCollection collection: claimedOutposts)
					collection.saveToFile();
			GSON.toJson(new RawClaimCollection(this), writer);
		}
		catch (Exception e) {
			SMP_Expansion.saveIOException(new IOException("Something went wrong when saving a claim collection", e));
		}
	}

	/**
	 * Gets the owner of this claim collection
	 * @return claim collection owner
	 */
	@NotNull
	public OfflinePlayer getOwner() {
		return owner;
	}

	/**
	 * Gets an immutable list of the claimed outposts of this collection
	 * @return list of outposts
	 */
	@NotNull
	public ArrayList<OutpostCollection> getClaimedOutposts() {
		return new ArrayList<>(claimedOutposts);
	}

	/**
	 * Gets a claimed outpost based on its name, or null if none are found
	 * @param name name of outpost
	 * @return outpost containing this name, or null if none are found
	 */
	@Nullable
	public OutpostCollection getOutpost(String name) {
		for (OutpostCollection outpost: claimedOutposts)
			if (outpost.getName().content().equals(name))
				return outpost;
		return null;
	}

	/**
	 * Gets the number of available outposts for this player
	 * @return number of available outposts
	 */
	public int getOutposts() {
		return outposts;
	}

	/**
	 * Sets the number of available outposts for this player
	 * @param amount number of available outposts
	 */
	public void setOutposts(int amount) {
		outposts = amount;
		saveToFile(false);
	}

	/**
	 * Changes the number of available outposts for this player
	 * @param change change in number of available outposts
	 */
	public void incOutposts(int change) {
		outposts += change;
		saveToFile(false);
	}

	/**
	 * Gets the number of available normal claims for this player
	 * @return number of available claims
	 */
	public int getClaims() {
		return claims;
	}

	/**
	 * Sets the number of available claims for this player
	 * @param amount number of available claims
	 */
	public void setClaims(int amount) {
		claims = amount;
		saveToFile(false);
	}

	/**
	 * Changes the number of available normal claims for this player
	 * @param change change in number of available claims
	 */
	public void incClaims(int change) {
		claims += change;
		saveToFile(false);
	}

	/**
	 * Gets the directory where the outpost files are stored
	 * @return directory containing outpost files
	 */
	File getOutpostsDirectory() {
		return outpostFileDirectory;
	}

	/**
	 * Adds an outpost collection to this claim collection
	 * @param collection new outpost collection
	 */
	void addOutpost(@NotNull OutpostCollection collection) {
		claimedOutposts.add(collection);
		saveToFile(false);
	}

	/**
	 * Removes an outpost collection from this claim collection
	 * @param outpost outpost collection to remove
	 */
	void removeOutpost(@NotNull OutpostCollection outpost) {
		claimedOutposts.remove(outpost);
		outpost.deleteOutpost();
	}

	/**
	 * Converts claim collection into raw form for storage
	 * @author YellowStoneTorch
	 * @version 0.1.0-ALPHA
	 */
	private static class RawClaimCollection {
		private final String owner;
		private final int outposts;
		private final int claims;

		/**
		 * Converts a claim collection into raw form for storage
		 * @param collection collection to convert
		 */
		RawClaimCollection(@NotNull ClaimCollection collection) {
			owner = collection.owner.getUniqueId().toString();
			outposts = collection.outposts;
			claims = collection.claims;
		}
	}
}

package io.github.YellowStoneTorch.SMP_Expansion.Claims;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents permissions for other players within an outpost
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public enum Permission {
	ALL(2),
	TEAMMATES(1),
	NONE(0);

	public final int data;

	/**
	 * Constructor
	 * @param data how this enum is stored in file
	 */
	Permission(int data) {
		this.data = data;
	}

	/**
	 * Gets the permission from its numerical form
	 * @param data numerical permission
	 * @return permission enum
	 */
	public static Permission getPermission(int data) {
		return switch (data) {
			case 2 ->
					ALL;
			case 1 ->
					TEAMMATES;
			default ->
					NONE;
		};
	}

	/**
	 * Gets the string for this permission to display in game
	 * @return string
	 */
	@Contract(pure = true)
	@Override
	public @NotNull String toString() {
		return switch (this) {
			case ALL ->
					"Everyone";
			case TEAMMATES ->
					"Teammates";
			case NONE ->
					"Only You";
		};
	}
}

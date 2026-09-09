package io.github.YellowStoneTorch.SMP_Expansion;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Manages time displays in seconds or ticks
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class TimeModule {
	/**
	 * Gets the formatted time display from the number of ticks
	 * @param ticks number of ticks to display
	 * @return formatted time string
	 */
	@NotNull
	public static String getTimeDisplayTicks(int ticks) {
		return getTimeDisplaySeconds(ticks / 20);
	}

	/**
	 * Gets the formatted time display from the number of ticks
	 * @param seconds number of seconds to display
	 * @return formatted time string
	 */
	@NotNull
	public static String getTimeDisplaySeconds(int seconds) {
		int days = seconds / 86400;
		seconds = seconds % 86400;
		int hours = seconds / 3600;
		seconds = seconds % 3600;
		int minutes = seconds / 60;
		seconds = seconds % 60;
		String time;
		if (days > 0)
			time = getNumberDisplay(days) + ":" + getNumberDisplay(hours) + ":" + getNumberDisplay(minutes) + ":" + getNumberDisplay(seconds);
		else if (hours > 0)
			time = getNumberDisplay(hours) + ":" + getNumberDisplay(minutes) + ":" + getNumberDisplay(seconds);
		else
			time = getNumberDisplay(minutes) + ":" + getNumberDisplay(seconds);
		return time;
	}

	/**
	 * Formats all single digit numbers as double digit numbers
	 * @param number number to display
	 * @return formatted number
	 */
	@Contract(pure = true)
	@NotNull
	private static String getNumberDisplay(int number) {
		if (number < 10)
			return "0" + number;
		else
			return Integer.toString(number);
	}
}

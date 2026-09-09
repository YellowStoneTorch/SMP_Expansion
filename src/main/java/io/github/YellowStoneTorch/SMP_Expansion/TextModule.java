package io.github.YellowStoneTorch.SMP_Expansion;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Manages text component creation and usage
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class TextModule {
	public static final DecimalFormat fmt1;
	public static final DecimalFormat fmt2;
	public static final DecimalFormat fmt3;

	static {
		fmt1 = new DecimalFormat("#.#");
		fmt2 = new DecimalFormat("#.##");
		fmt3 = new DecimalFormat("#.###");
		fmt1.setRoundingMode(RoundingMode.HALF_UP);
		fmt2.setRoundingMode(RoundingMode.HALF_UP);
		fmt3.setRoundingMode(RoundingMode.HALF_UP);
	}

	/**
	 * Creates a TextComponent with normal formatting options, removing italics for item components
	 * @param content text
	 * @param color color of text
	 * @param decorations list of decorations to add
	 * @return component
	 */
	@NotNull
	public static TextComponent textNI(String content, TextColor color, TextDecoration... decorations) {
		return Component.text().content(content).style(Style.style(color, decorations)).decoration(TextDecoration.ITALIC, false).build();
	}

	/**
	 * Creates a builder for plain text, for combining into a single line
	 * @param content text
	 * @return builder
	 */
	@NotNull
	public static TextComponent.Builder comp(String content) {
		return Component.text().content(content);
	}

	/**
	 * Creates a builder with normal formatting options
	 * @param content text
	 * @param color color of text
	 * @param decorations list of decorations to add
	 * @return builder
	 */
	@NotNull
	public static TextComponent.Builder comp(String content, TextColor color, TextDecoration... decorations) {
		return Component.text().content(content).style(Style.style(color, decorations));
	}

	/**
	 * Creates a builder with normal formatting options, but no italics
	 * @param content text
	 * @param color color of text
	 * @param decorations list of decorations to add
	 * @return builder
	 */
	@NotNull
	public static TextComponent.Builder compNI(String content, TextColor color, TextDecoration... decorations) {
		return Component.text().content(content).style(Style.style(color, decorations)).decoration(TextDecoration.ITALIC, false);
	}

	/**
	 * Creates a builder with normal formatting options and a click event
	 * @param clickEvent click event
	 * @param content text
	 * @param color color of text
	 * @param decorations list of decorations to add
	 * @return builder
	 */
	@NotNull
	public static TextComponent.Builder compClick(ClickEvent<?> clickEvent, String content, TextColor color, TextDecoration... decorations) {
		return Component.text().content(content).style(Style.style(color, decorations)).clickEvent(clickEvent);
	}

	/**
	 * Unites a set of TextComponent Builders as one line
	 * @param texts text component builders to unite
	 * @return united builder
	 */
	@NotNull
	public static TextComponent.Builder unite(TextComponent.Builder @NotNull ... texts) {
		TextComponent.Builder combined = Component.text();
		for (TextComponent.Builder text: texts)
			combined.append(text);
		return combined;
	}

	/**
	 * Builds a set of TextComponents as one line
	 * @param texts text components to build
	 * @return component
	 */
	@NotNull
	public static TextComponent build(TextComponent.Builder @NotNull ... texts) {
		return unite(texts).build();
	}
}

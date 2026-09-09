package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.*;
import static net.kyori.adventure.text.Component.text;

/**
 * Menu for selecting items to be renamed as well as formatting options
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class AnvilRenameMenu extends InventoryMenu {
	private static final ItemStack resetIcon;

	static {
		resetIcon = ItemStack.of(Material.BARRIER, 1);
		ItemMeta data = resetIcon.getItemMeta();
		data.customName(textNI("Click to Reset Formatting Options", NamedTextColor.YELLOW, TextDecoration.BOLD));
		resetIcon.setItemMeta(data);
	}

	private NamedTextColor color;
	private boolean isBold;
	private boolean isItalic;
	private boolean isUnderlined;
	private boolean isStrikethrough;
	private boolean isObfuscated;
	private boolean isReady;

	/**
	 * Creates inventory menu
	 */
	AnvilRenameMenu() {
		super(54, text("Select Formatting Options"));
		color = NamedTextColor.WHITE;
		isBold = false;
		isItalic = false;
		isUnderlined = false;
		isStrikethrough = false;
		isObfuscated = false;
		isReady = false;
		inventory.setItem(10, resetIcon);
		inventory.setItem(28, null);
		displayIcons();
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		int slot = event.getRawSlot();
		if (slot < 54 && slot != 28) {
			event.setCancelled(true);
			switch (slot) {
				case 10 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.WHITE;
					isBold = false;
					isItalic = false;
					isUnderlined = false;
					isStrikethrough = false;
					isObfuscated = false;
				}
				case 12 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.DARK_RED;
				}
				case 13 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.RED;
				}
				case 14 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.GOLD;
				}
				case 15 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.YELLOW;
				}
				case 16 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.GREEN;
				}
				case 21 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.DARK_GREEN;
				}
				case 22 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.DARK_AQUA;
				}
				case 23 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.AQUA;
				}
				case 24 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.BLUE;
				}
				case 25 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.DARK_BLUE;
				}
				case 30 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.DARK_PURPLE;
				}
				case 31 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.LIGHT_PURPLE;
				}
				case 32 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.WHITE;
				}
				case 33 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.GRAY;
				}
				case 34 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					color = NamedTextColor.DARK_GRAY;
				}
				case 37 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					ItemStack input = inventory.getItem(28);
					if (input != null)
						new BukkitRunnable() {
							@Override
							public void run() {
								isReady = true;
								player.openInventory(new AnvilRenameScreen(player, input, color, getTextDecorations()).getInventoryView());
							}
						}.runTask(SMP_Expansion.getPlugin());
				}
				case 39 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					isBold = !isBold;
				}
				case 40 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					isItalic = !isItalic;
				}
				case 41 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					isUnderlined = !isUnderlined;
				}
				case 42 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					isStrikethrough = !isStrikethrough;
				}
				case 43 -> {
					player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 0.25f, 1);
					isObfuscated = !isObfuscated;
				}
			}
		}
		if (slot != 37)
			new BukkitRunnable() {
				@Override
				public void run() {
					displayIcons();
				}
			}.runTask(SMP_Expansion.getPlugin());
	}

	@Override
	public void onClose() {
		List<HumanEntity> viewers = inventory.getViewers();
		if (!isReady && !viewers.isEmpty() && viewers.getFirst() instanceof Player player) {
			if (inventory.getItem(28) instanceof ItemStack item) {
				player.give(item);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.updateInventory();
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
		}
	}

	@Override
	public void onDrag(InventoryDragEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				displayIcons();
			}
		}.runTask(SMP_Expansion.getPlugin());
	}

	/**
	 * Sets the icons to display based on the current formatting options selected
	 */
	private void displayIcons() {
		inventory.setItem(12, getDarkRedIcon(color == NamedTextColor.DARK_RED));
		inventory.setItem(13, getRedIcon(color == NamedTextColor.RED));
		inventory.setItem(14, getOrangeIcon(color == NamedTextColor.GOLD));
		inventory.setItem(15, getYellowIcon(color == NamedTextColor.YELLOW));
		inventory.setItem(16, getLimeIcon(color == NamedTextColor.GREEN));
		inventory.setItem(21, getGreenIcon(color == NamedTextColor.DARK_GREEN));
		inventory.setItem(22, getCyanIcon(color == NamedTextColor.DARK_AQUA));
		inventory.setItem(23, getLightBlueIcon(color == NamedTextColor.AQUA));
		inventory.setItem(24, getBlueIcon(color == NamedTextColor.BLUE));
		inventory.setItem(25, getDarkBlueIcon(color == NamedTextColor.DARK_BLUE));
		inventory.setItem(30, getPurpleIcon(color == NamedTextColor.DARK_PURPLE));
		inventory.setItem(31, getPinkIcon(color == NamedTextColor.LIGHT_PURPLE));
		inventory.setItem(32, getWhiteIcon(color == NamedTextColor.WHITE));
		inventory.setItem(33, getLightGrayIcon(color == NamedTextColor.GRAY));
		inventory.setItem(34, getDarkGrayIcon(color == NamedTextColor.DARK_GRAY));
		inventory.setItem(37, getInputIcon());
		inventory.setItem(39, getBoldIcon(isBold));
		inventory.setItem(40, getItalicIcon(isItalic));
		inventory.setItem(41, getUnderlinedIcon(isUnderlined));
		inventory.setItem(42, getStrikethroughIcon(isStrikethrough));
		inventory.setItem(43, getObfuscateIcon(isObfuscated));
	}

	/**
	 * Returns the text decorations as an array for application
	 * @return array of text decorations
	 */
	private TextDecoration[] getTextDecorations() {
		ArrayList<TextDecoration> formattingOptions = new ArrayList<>();
		if (isBold)
			formattingOptions.add(TextDecoration.BOLD);
		if (isItalic)
			formattingOptions.add(TextDecoration.ITALIC);
		if (isUnderlined)
			formattingOptions.add(TextDecoration.UNDERLINED);
		if (isStrikethrough)
			formattingOptions.add(TextDecoration.STRIKETHROUGH);
		if (isObfuscated)
			formattingOptions.add(TextDecoration.OBFUSCATED);
		return formattingOptions.toArray(TextDecoration[]::new);
	}

	/**
	 * Gets the input icon with a preview of formatting options
	 * @return input icon
	 */
	@NotNull
	private ItemStack getInputIcon() {
		ItemStack icon = ItemStack.of(Material.NAME_TAG, 1);
		ItemMeta data = icon.getItemMeta();
		data.customName(textNI("Place Items to Rename Here", NamedTextColor.YELLOW));
		ArrayList<TextComponent> lore = new ArrayList<>();
		if (inventory.getItem(28) != null) {
			lore.add(Component.empty());
			lore.add(textNI("Click here once formatting options are selected", NamedTextColor.GRAY));
		}
		lore.add(Component.empty());
		if (isItalic)
			lore.add(build(compNI("Formatting Preview: ", NamedTextColor.GRAY), comp("Minecraft", color, getTextDecorations())));
		else
			lore.add(build(compNI("Formatting Preview: ", NamedTextColor.GRAY), compNI("Minecraft", color, getTextDecorations())));
		data.lore(lore);
		icon.setItemMeta(data);
		return icon;
	}

	/**
	 * Gets the icon to display for dark red color
	 * @param isSelected whether this color is selected
	 * @return dark red icon
	 */
	@NotNull
	private ItemStack getDarkRedIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.NETHER_BRICK, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Dark Red", NamedTextColor.DARK_RED, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for red color
	 * @param isSelected whether this color is selected
	 * @return red icon
	 */
	@NotNull
	private ItemStack getRedIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.RED_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Red", NamedTextColor.RED, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for orange color
	 * @param isSelected whether this color is selected
	 * @return orange icon
	 */
	@NotNull
	private ItemStack getOrangeIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.ORANGE_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Orange", NamedTextColor.GOLD, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for yellow color
	 * @param isSelected whether this color is selected
	 * @return yellow icon
	 */
	@NotNull
	private ItemStack getYellowIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.YELLOW_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Yellow", NamedTextColor.YELLOW, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for lime color
	 * @param isSelected whether this color is selected
	 * @return lime icon
	 */
	@NotNull
	private ItemStack getLimeIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.LIME_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Lime", NamedTextColor.GREEN, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for green color
	 * @param isSelected whether this color is selected
	 * @return green icon
	 */
	@NotNull
	private ItemStack getGreenIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.GREEN_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Green", NamedTextColor.DARK_GREEN, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for cyan color
	 * @param isSelected whether this color is selected
	 * @return cyan icon
	 */
	@NotNull
	private ItemStack getCyanIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.CYAN_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Cyan", NamedTextColor.DARK_AQUA, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for light blue color
	 * @param isSelected whether this color is selected
	 * @return light blue icon
	 */
	@NotNull
	private ItemStack getLightBlueIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.LIGHT_BLUE_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Light Blue", NamedTextColor.AQUA, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for blue color
	 * @param isSelected whether this color is selected
	 * @return blue icon
	 */
	@NotNull
	private ItemStack getBlueIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.BLUE_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Blue", NamedTextColor.BLUE, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for dark blue color
	 * @param isSelected whether this color is selected
	 * @return dark blue icon
	 */
	@NotNull
	private ItemStack getDarkBlueIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.BLUE_CONCRETE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Dark Blue", NamedTextColor.DARK_BLUE, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for purple color
	 * @param isSelected whether this color is selected
	 * @return purple icon
	 */
	@NotNull
	private ItemStack getPurpleIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.PURPLE_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Purple", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for pink color
	 * @param isSelected whether this color is selected
	 * @return pink icon
	 */
	@NotNull
	private ItemStack getPinkIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.PINK_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Pink", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for white color
	 * @param isSelected whether this color is selected
	 * @return white icon
	 */
	@NotNull
	private ItemStack getWhiteIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.WHITE_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("White", NamedTextColor.WHITE, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for light gray color
	 * @param isSelected whether this color is selected
	 * @return light gray icon
	 */
	@NotNull
	private ItemStack getLightGrayIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.LIGHT_GRAY_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Light Gray", NamedTextColor.GRAY, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for gray color
	 * @param isSelected whether this color is selected
	 * @return gray icon
	 */
	@NotNull
	private ItemStack getDarkGrayIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.GRAY_DYE, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Dark Gray", NamedTextColor.DARK_GRAY, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			else
				lore.add(textNI("Click to Select", NamedTextColor.YELLOW));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for bold formatting
	 * @param isSelected whether this format is selected
	 * @return bold icon
	 */
	@NotNull
	private ItemStack getBoldIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.PAPER, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Bold Text", NamedTextColor.WHITE, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for italic formatting
	 * @param isSelected whether this format is selected
	 * @return italic icon
	 */
	@NotNull
	private ItemStack getItalicIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.PAPER, 1);
		icon.editMeta(data ->
		{
			data.customName(text("Italic Text", NamedTextColor.WHITE));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for underlined formatting
	 * @param isSelected whether this format is selected
	 * @return underlined icon
	 */
	@NotNull
	private ItemStack getUnderlinedIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.PAPER, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Underlined Text", NamedTextColor.WHITE, TextDecoration.UNDERLINED));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for strikethrough formatting
	 * @param isSelected whether this format is selected
	 * @return strikethrough icon
	 */
	@NotNull
	private ItemStack getStrikethroughIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.PAPER, 1);
		icon.editMeta(data ->
		{
			data.customName(textNI("Strikethrough Text", NamedTextColor.WHITE, TextDecoration.STRIKETHROUGH));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}

	/**
	 * Gets the icon to display for bold formatting
	 * @param isSelected whether this format is selected
	 * @return bold icon
	 */
	@NotNull
	private ItemStack getObfuscateIcon(boolean isSelected) {
		ItemStack icon = ItemStack.of(Material.PAPER, 1);
		icon.editMeta(data ->
		{
			data.customName(build(compNI("Obfuscated Text (", NamedTextColor.WHITE), compNI("xxxx", NamedTextColor.WHITE, TextDecoration.OBFUSCATED), compNI(")", NamedTextColor.WHITE)));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Click to Toggle", NamedTextColor.YELLOW));
			if (isSelected)
				lore.add(textNI("Selected!", NamedTextColor.GREEN));
			data.lore(lore);
			data.setEnchantmentGlintOverride(isSelected);
		});
		return icon;
	}
}

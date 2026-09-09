package io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus;

import io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EquipEnchantment;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.ItemManager;
import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.itemLevel;
import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.renamed;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;
import static net.kyori.adventure.text.Component.text;
import static org.bukkit.Material.*;

/**
 * Smithing table interface
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class SmithingTableMenu extends InventoryMenu {
	private static final ItemStack infoIcon;
	private static final ItemStack equipmentIcon;
	private static final ItemStack materialsIcon;
	private static final ItemStack templateIcon;
	private static final ItemStack outputIcon;

	static {
		ArrayList<TextComponent> lore = new ArrayList<>();
		infoIcon = ItemStack.of(SMITHING_TABLE, 1);
		infoIcon.editMeta(data ->
		{
			data.customName(textNI("Smithing Table", NamedTextColor.YELLOW, TextDecoration.BOLD));
			lore.add(Component.empty());
			lore.add(textNI("Here, you can upgrade equipment levels or tiers.", NamedTextColor.GRAY));
			lore.add(textNI("Doing so will require certain materials.", NamedTextColor.GRAY));
			lore.add(textNI("You can also apply trims to armor.", NamedTextColor.GRAY));
			data.lore(lore);
		});
		equipmentIcon = ItemStack.of(DIAMOND_SWORD, 1);
		equipmentIcon.editMeta(data ->
		{
			data.customName(textNI("Place Equipment Here", NamedTextColor.YELLOW));
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
		materialsIcon = ItemStack.of(NETHERITE_INGOT, 1);
		materialsIcon.editMeta(data ->
				data.customName(textNI("Place Upgrade Materials Here", NamedTextColor.YELLOW)));
		templateIcon = ItemStack.of(PAPER, 1);
		templateIcon.editMeta(data ->
		{
			data.customName(textNI("Place Template Here", NamedTextColor.YELLOW));
			lore.clear();
			lore.add(Component.empty());
			lore.add(textNI("Armor trims will not be consumed,", NamedTextColor.GRAY));
			lore.add(textNI("but netherite upgrade templates will.", NamedTextColor.GRAY));
			data.lore(lore);
		});
		outputIcon = ItemStack.of(NETHERITE_SWORD, 1);
		outputIcon.editMeta(data ->
		{
			data.customName(textNI("Upgraded Equipment", NamedTextColor.YELLOW));
			data.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		});
	}

	/**
	 * Creates inventory menu
	 */
	public SmithingTableMenu() {
		super(54, text("Smithing Table"));
		inventory.setItem(13, infoIcon);
		inventory.setItem(29, null);
		inventory.setItem(30, null);
		inventory.setItem(31, null);
		inventory.setItem(33, outputSlot);
		inventory.setItem(38, equipmentIcon);
		inventory.setItem(39, materialsIcon);
		inventory.setItem(40, templateIcon);
		inventory.setItem(42, outputIcon);
	}

	@Override
	public void onClick(@NotNull InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		int slot = event.getRawSlot();
		boolean isOutputting;
		if (slot == 33) // Click on output
		{
			ItemStack outputItem = inventory.getItem(33);
			if (outputItem != null && outputItem.getType() != LIGHT_GRAY_STAINED_GLASS_PANE) {
				inventory.setItem(29, null);
				if (inventory.getItem(30) instanceof ItemStack item)
					inventory.setItem(30, item.add(-1));
				if (inventory.getItem(31) instanceof ItemStack item && item.getType() == NETHERITE_UPGRADE_SMITHING_TEMPLATE)
					inventory.setItem(31, item.add(-1));
				player.playSound(player, Sound.BLOCK_SMITHING_TABLE_USE, SoundCategory.BLOCKS, 1, 1);
				isOutputting = true;
			}
			else {
				event.setCancelled(true);
				isOutputting = false;
			}
		}
		else if (slot < 54 && (slot < 29 || slot > 31)) // Clicks inside the menu not on input slots
		{
			event.setCancelled(true);
			isOutputting = false;
		}
		else
			isOutputting = false;
		new BukkitRunnable() {
			@Override
			public void run() {
				if (isOutputting && event.getClick() == ClickType.NUMBER_KEY && inventory.getItem(33) instanceof ItemStack item)
					player.give(item);
				inventory.setItem(33, getOutput());
				inventory.setItem(39, getRequiredMaterials());
			}
		}.runTask(SMP_Expansion.getPlugin());
	}

	@Override
	public void onClose() {
		List<HumanEntity> viewers = inventory.getViewers();
		if (!viewers.isEmpty() && viewers.getFirst() instanceof Player player) {
			for (int i = 29; i < 32; i++) {
				ItemStack item = inventory.getItem(i);
				if (item != null)
					player.give(item);
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					player.updateInventory();
				}
			}.runTask(SMP_Expansion.getPlugin());
		}
	}

	@Override
	public void onDrag(InventoryDragEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				inventory.setItem(33, getOutput());
				inventory.setItem(39, getRequiredMaterials());
			}
		}.runTask(SMP_Expansion.getPlugin());
	}

	/**
	 * Gets the required materials to display
	 * @return required materials
	 */
	private ItemStack getRequiredMaterials() {
		ItemStack input = inventory.getItem(29);
		ItemStack template = inventory.getItem(31);
		if (input != null) {
			if (getTrimPattern(template) != null && input.getItemMeta() instanceof ArmorMeta) // Trim application attempted
			{
				ItemStack materials = ItemStack.of(NETHERITE_INGOT, 1);
				ItemMeta data = materials.getItemMeta();
				ArrayList<TextComponent> lore = new ArrayList<>();
				data.customName(textNI("Place Upgrade Materials Here", NamedTextColor.YELLOW));
				lore.add(Component.empty());
				lore.add(textNI("Required: 1 Trim Material", NamedTextColor.WHITE));
				data.lore(lore);
				materials.setItemMeta(data);
				return materials;
			}
			else // Item upgrade attempted
			{
				ItemStack materials = ItemStack.of(NETHERITE_INGOT, 1);
				ItemMeta data = materials.getItemMeta();
				ArrayList<TextComponent> lore = new ArrayList<>();
				data.customName(textNI("Place Upgrade Materials Here", NamedTextColor.YELLOW));
				switch (input.getType()) {
					case DIAMOND_SWORD, DIAMOND_AXE, DIAMOND_PICKAXE, DIAMOND_SHOVEL, DIAMOND_HOE, DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS -> {
						lore.add(Component.empty());
						lore.add(textNI("Required: 1 Netherite Ingot", NamedTextColor.WHITE));
						lore.add(textNI("Netherite Upgrade Template Required", NamedTextColor.WHITE));
					}
					case NETHERITE_SWORD, NETHERITE_AXE, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> {
						int level = input.getItemMeta()
								.getPersistentDataContainer()
								.getOrDefault(itemLevel, PersistentDataType.INTEGER, 0);
						switch (level) {
							case 1 -> {
								lore.add(Component.empty());
								lore.add(textNI("Required: 1 Nether Star", NamedTextColor.WHITE));
								lore.add(textNI("Netherite Upgrade Template Required", NamedTextColor.WHITE));
							}
							case 2 -> {
								lore.add(Component.empty());
								lore.add(textNI("Required: 1 Conduit", NamedTextColor.WHITE));
								lore.add(textNI("Netherite Upgrade Template Required", NamedTextColor.WHITE));
							}
							case 3 -> {
								lore.add(Component.empty());
								lore.add(textNI("Required: 1 Enchanted Golden Apple", NamedTextColor.WHITE));
								lore.add(textNI("Netherite Upgrade Template Required", NamedTextColor.WHITE));
							}
							case 4 -> {
								lore.add(Component.empty());
								lore.add(textNI("Required: 1 Heavy Core", NamedTextColor.WHITE));
								lore.add(textNI("Netherite Upgrade Template Required", NamedTextColor.WHITE));
							}
						}
					}
				}
				data.lore(lore);
				materials.setItemMeta(data);
				return materials;
			}
		}
		else
			return materialsIcon;
	}

	/**
	 * Gets the output item to display depending on inputs
	 * @return output item
	 */
	private ItemStack getOutput() {
		ItemStack input = inventory.getItem(29);
		ItemStack materials = inventory.getItem(30);
		ItemStack template = inventory.getItem(31);
		if (input != null) {
			ItemMeta data = input.getItemMeta();
			int damage = data instanceof Damageable damageable ? damageable.getDamage() : 0;
			ArrayList<EquipEnchantment> enchantments = EquipEnchantment.getEnchantments(input);
			Component customName = data.customName();
			if (getTrimMaterial(materials) instanceof TrimMaterial material && getTrimPattern(template) instanceof TrimPattern pattern) {
				ItemStack output = input.clone();
				if (output.getItemMeta() instanceof ArmorMeta armorData) {
					armorData.setTrim(new ArmorTrim(material, pattern));
					output.setItemMeta(armorData);
					return output;
				}
				else
					return outputSlot;
			}
			else if (materials != null & template != null && template.getType() == NETHERITE_UPGRADE_SMITHING_TEMPLATE) {
				int level = data.getPersistentDataContainer()
						.getOrDefault(itemLevel, PersistentDataType.INTEGER, 0);
				ItemStack output = switch (materials.getType()) {
					case NETHERITE_INGOT ->
							switch (input.getType()) {
								case DIAMOND_SWORD ->
										ItemManager.getNetheriteSword(1, damage, enchantments);
								case DIAMOND_AXE ->
										ItemManager.getNetheriteAxe(1, damage, enchantments);
								case DIAMOND_PICKAXE ->
										ItemManager.getNetheritePickaxe(damage, enchantments);
								case DIAMOND_SHOVEL ->
										ItemManager.getNetheriteShovel(damage, enchantments);
								case DIAMOND_HOE ->
										ItemManager.getNetheriteHoe(damage, enchantments);
								case DIAMOND_HELMET ->
										ItemManager.getNetheriteHelmet(1, damage, enchantments);
								case DIAMOND_CHESTPLATE ->
										ItemManager.getNetheriteChestplate(1, damage, enchantments);
								case DIAMOND_LEGGINGS ->
										ItemManager.getNetheriteLeggings(1, damage, enchantments);
								case DIAMOND_BOOTS ->
										ItemManager.getNetheriteBoots(1, damage, enchantments);
								default ->
										outputSlot;
							};
					case NETHER_STAR -> {
						if (level == 1)
							yield switch (input.getType()) {
								case NETHERITE_SWORD ->
										ItemManager.getNetheriteSword(2, damage, enchantments);
								case NETHERITE_AXE ->
										ItemManager.getNetheriteAxe(2, damage, enchantments);
								case NETHERITE_HELMET ->
										ItemManager.getNetheriteHelmet(2, damage, enchantments);
								case NETHERITE_CHESTPLATE ->
										ItemManager.getNetheriteChestplate(2, damage, enchantments);
								case NETHERITE_LEGGINGS ->
										ItemManager.getNetheriteLeggings(2, damage, enchantments);
								case NETHERITE_BOOTS ->
										ItemManager.getNetheriteBoots(2, damage, enchantments);
								default ->
										outputSlot;
							};
						else
							yield outputSlot;
					}
					case CONDUIT -> {
						if (level == 2)
							yield switch (input.getType()) {
								case NETHERITE_SWORD ->
										ItemManager.getNetheriteSword(3, damage, enchantments);
								case NETHERITE_AXE ->
										ItemManager.getNetheriteAxe(3, damage, enchantments);
								case NETHERITE_HELMET ->
										ItemManager.getNetheriteHelmet(3, damage, enchantments);
								case NETHERITE_CHESTPLATE ->
										ItemManager.getNetheriteChestplate(3, damage, enchantments);
								case NETHERITE_LEGGINGS ->
										ItemManager.getNetheriteLeggings(3, damage, enchantments);
								case NETHERITE_BOOTS ->
										ItemManager.getNetheriteBoots(3, damage, enchantments);
								default ->
										outputSlot;
							};
						else
							yield outputSlot;
					}
					case ENCHANTED_GOLDEN_APPLE -> {
						if (level == 3)
							yield switch (input.getType()) {
								case NETHERITE_SWORD ->
										ItemManager.getNetheriteSword(4, damage, enchantments);
								case NETHERITE_AXE ->
										ItemManager.getNetheriteAxe(4, damage, enchantments);
								case NETHERITE_HELMET ->
										ItemManager.getNetheriteHelmet(4, damage, enchantments);
								case NETHERITE_CHESTPLATE ->
										ItemManager.getNetheriteChestplate(4, damage, enchantments);
								case NETHERITE_LEGGINGS ->
										ItemManager.getNetheriteLeggings(4, damage, enchantments);
								case NETHERITE_BOOTS ->
										ItemManager.getNetheriteBoots(4, damage, enchantments);
								default ->
										outputSlot;
							};
						else
							yield outputSlot;
					}
					case HEAVY_CORE -> {
						if (level == 4)
							yield switch (input.getType()) {
								case NETHERITE_SWORD ->
										ItemManager.getNetheriteSword(5, damage, enchantments);
								case NETHERITE_AXE ->
										ItemManager.getNetheriteAxe(5, damage, enchantments);
								case NETHERITE_HELMET ->
										ItemManager.getNetheriteHelmet(5, damage, enchantments);
								case NETHERITE_CHESTPLATE ->
										ItemManager.getNetheriteChestplate(5, damage, enchantments);
								case NETHERITE_LEGGINGS ->
										ItemManager.getNetheriteLeggings(5, damage, enchantments);
								case NETHERITE_BOOTS ->
										ItemManager.getNetheriteBoots(5, damage, enchantments);
								default ->
										outputSlot;
							};
						else
							yield outputSlot;
					}
					default ->
							outputSlot;
				};
				ItemMeta outputData = output.getItemMeta();
				if (input.getItemMeta() instanceof ArmorMeta inputTrim && outputData instanceof ArmorMeta outputTrim)
					outputTrim.setTrim(inputTrim.getTrim());
				if (output != outputSlot && customName != null && data.getPersistentDataContainer()
						.has(renamed))
					outputData.customName(customName);
				output.setItemMeta(outputData);
				return output;
			}
			else
				return outputSlot;
		}
		else
			return outputSlot;
	}

	/**
	 * Gets the trim pattern for this material type, or null if it doesn't have one
	 * @param item material to test
	 * @return trim pattern
	 */
	@Nullable
	private TrimPattern getTrimPattern(ItemStack item) {
		if (item != null) {
			return switch (item.getType()) {
				case BOLT_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.BOLT;
				case COAST_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.COAST;
				case DUNE_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.DUNE;
				case EYE_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.EYE;
				case FLOW_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.FLOW;
				case HOST_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.HOST;
				case RAISER_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.RAISER;
				case RIB_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.RIB;
				case SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.SENTRY;
				case SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.SHAPER;
				case SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.SILENCE;
				case SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.SNOUT;
				case SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.SPIRE;
				case TIDE_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.TIDE;
				case VEX_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.VEX;
				case WARD_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.WARD;
				case WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.WAYFINDER;
				case WILD_ARMOR_TRIM_SMITHING_TEMPLATE ->
						TrimPattern.WILD;
				default ->
						null;
			};
		}
		else
			return null;
	}

	/**
	 * Gets the trim material for this material type, or null if it doesn't have one
	 * @param item material to test
	 * @return trim material
	 */
	@Nullable
	private TrimMaterial getTrimMaterial(ItemStack item) {
		if (item != null) {
			return switch (item.getType()) {
				case AMETHYST_SHARD ->
						TrimMaterial.AMETHYST;
				case COPPER_INGOT ->
						TrimMaterial.COPPER;
				case DIAMOND ->
						TrimMaterial.DIAMOND;
				case EMERALD ->
						TrimMaterial.EMERALD;
				case GOLD_INGOT ->
						TrimMaterial.GOLD;
				case IRON_INGOT ->
						TrimMaterial.IRON;
				case LAPIS_LAZULI ->
						TrimMaterial.LAPIS;
				case NETHERITE_INGOT ->
						TrimMaterial.NETHERITE;
				case QUARTZ ->
						TrimMaterial.QUARTZ;
				case REDSTONE ->
						TrimMaterial.REDSTONE;
				case RESIN_BRICK ->
						TrimMaterial.RESIN;
				default ->
						null;
			};
		}
		else
			return null;
	}
}

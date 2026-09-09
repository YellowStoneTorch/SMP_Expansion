package io.github.YellowStoneTorch.SMP_Expansion.Villagers;

import io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EnchantmentType;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EquipEnchantment;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.ItemManager;
import io.papermc.paper.potion.SuspiciousEffectEntry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.villagerHasTradeLevel;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.textNI;
import static org.bukkit.entity.Villager.Profession.*;

/**
 * Manages villager types and custom trades
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class VillagerData {
	private static final Random random = new Random();
	private static final MerchantRecipe error;

	static {
		ItemStack errorIcon = new ItemStack(Material.BARRIER, 1);
		errorIcon.editMeta(data -> {
			data.customName(textNI("Error: Invalid villager type", NamedTextColor.RED, TextDecoration.BOLD));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("Please report this error in a bug report", NamedTextColor.RED));
			lore.add(textNI("Your villager will need to be replaced", NamedTextColor.RED));
			data.lore(lore);
		});
		error = new MerchantRecipe(errorIcon, 1);
		error.addIngredient(errorIcon);
	}

	/**
	 * Gets a randomly generated profession type from the given profession
	 * @param profession villager profession
	 * @param biome villager biome type for cartographers
	 * @return numbered profession type
	 */
	public static int generateVillagerType(Villager.Profession profession, Villager.Type biome) {
		if (profession == BUTCHER || profession == CLERIC || profession == FISHERMAN || profession == WEAPONSMITH)
			return random.nextInt(1, 3);
		else if (profession == ARMORER || profession == FARMER || profession == LEATHERWORKER || profession == TOOLSMITH)
			return random.nextInt(1, 5);
		else if (profession == MASON || profession == SHEPHERD)
			return random.nextInt(1, 17);
		else if (profession == LIBRARIAN)
			return random.nextInt(1, 31);
		else if (profession == FLETCHER)
			return random.nextInt(1, 43);
		else if (profession == CARTOGRAPHER) {
			if (biome == Villager.Type.PLAINS || biome == Villager.Type.TAIGA || biome == Villager.Type.SAVANNA)
				return random.nextInt(1, 5);
			else if (biome == Villager.Type.JUNGLE || biome == Villager.Type.SWAMP)
				return random.nextInt(1, 4);
			else if (biome == Villager.Type.SNOW || biome == Villager.Type.DESERT)
				return random.nextInt(1, 6);
			else
				return 0;
		}
		else
			return 0;
	}

	/**
	 * Sets the new trades to the villager
	 * @param villager villager to set trades to
	 * @param profession villager profession
	 * @param biome villager biome type
	 * @param type numbered villager type
	 * @param level new villager level
	 */
	public static void setTrades(@NotNull Villager villager, Villager.Profession profession, Villager.Type biome, int type, int level) {
		PersistentDataContainer villagerData = villager.getPersistentDataContainer();
		ArrayList<MerchantRecipe> recipes = new ArrayList<>(villager.getRecipes());
		int currentTradeAmount = villagerData.getOrDefault(villagerHasTradeLevel, PersistentDataType.INTEGER, 0);
		if (currentTradeAmount < villager.getVillagerLevel()) {
			if (profession == ARMORER)
				recipes.addAll(getArmorerTrades(type, level));
			else if (profession == BUTCHER)
				recipes.addAll(getButcherTrades(type, level));
			else if (profession == CARTOGRAPHER)
				recipes.addAll(getCartographerTrades(type, level, biome));
			else if (profession == CLERIC)
				recipes.addAll(getClericTrades(type, level));
			else if (profession == FARMER)
				recipes.addAll(getFarmerTrades(type, level));
			else if (profession == FISHERMAN)
				recipes.addAll(getFishermanTrades(type, level, biome));
			else if (profession == FLETCHER)
				recipes.addAll(getFletcherTrades(type, level));
			else if (profession == LEATHERWORKER)
				recipes.addAll(getLeatherworkerTrades(type, level));
			else if (profession == LIBRARIAN)
				recipes.addAll(getLibrarianTrades(type, level));
			else if (profession == MASON)
				recipes.addAll(getMasonTrades(type, level));
			else if (profession == SHEPHERD)
				recipes.addAll(getShepherdTrades(type, level));
			else if (profession == TOOLSMITH)
				recipes.addAll(getToolsmithTrades(type, level));
			else if (profession == WEAPONSMITH)
				recipes.addAll(getWeaponsmithTrades(type, level));
			else
				recipes.add(error);
		}
		villager.setRecipes(recipes);
	}

	/**
	 * Gets the new trades for the armorer villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getArmorerTrades(int type, int level) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.COAL, 15));
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						recipe2 = new MerchantRecipe(ItemManager.getCopperHelmet(0, new ArrayList<>()), 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 2 -> {
						recipe2 = new MerchantRecipe(ItemManager.getCopperChestplate(0, new ArrayList<>()), 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 3));
					}
					case 3 -> {
						recipe2 = new MerchantRecipe(ItemManager.getCopperLeggings(0, new ArrayList<>()), 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 2));
					}
					case 4 -> {
						recipe2 = new MerchantRecipe(ItemManager.getCopperBoots(0, new ArrayList<>()), 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1;
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.COPPER_INGOT, 12));
						recipe2 = new MerchantRecipe(ItemManager.getChainmailHelmet(0, new ArrayList<>()), 0, 12, true, 5, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 2 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.COPPER_INGOT, 12));
						recipe2 = new MerchantRecipe(ItemManager.getChainmailChesplate(0, new ArrayList<>()), 0, 12, true, 5, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 3));
					}
					case 3 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.IRON_INGOT, 4));
						recipe2 = new MerchantRecipe(ItemManager.getChainmailLeggings(0, new ArrayList<>()), 0, 12, true, 5, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 2));
					}
					case 4 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.IRON_INGOT, 4));
						recipe2 = new MerchantRecipe(ItemManager.getChainmailBoots(0, new ArrayList<>()), 0, 12, true, 5, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					default -> {
						recipe1 = error;
						recipe2 = error;
					}
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1;
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 20, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.LAVA_BUCKET, 1));
						recipe2 = new MerchantRecipe(ItemManager.getIronHelmet(0, new ArrayList<>()), 0, 12, true, 10, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 4));
					}
					case 2 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 20, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.LAVA_BUCKET, 1));
						recipe2 = new MerchantRecipe(ItemManager.getIronChestplate(0, new ArrayList<>()), 0, 12, true, 10, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 8));
					}
					case 3 -> {
						recipe1 = new MerchantRecipe(ItemManager.getShield(0, new ArrayList<>()), 0, 12, true, 10, 0.2f, false);
						recipe1.addIngredient(ItemStack.of(Material.EMERALD, 5));
						recipe2 = new MerchantRecipe(ItemManager.getIronLeggings(0, new ArrayList<>()), 0, 12, true, 10, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 7));
					}
					case 4 -> {
						recipe1 = new MerchantRecipe(ItemManager.getShield(0, new ArrayList<>()), 0, 12, true, 10, 0.2f, false);
						recipe1.addIngredient(ItemStack.of(Material.EMERALD, 5));
						recipe2 = new MerchantRecipe(ItemManager.getIronBoots(0, new ArrayList<>()), 0, 12, true, 10, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 4));
					}
					default -> {
						recipe1 = error;
						recipe2 = error;
					}
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 4), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.DIAMOND, 1));
				MerchantRecipe recipe2;
				switch (type) {
					case 1, 2 -> {
						ArrayList<EquipEnchantment> armorEnchantments = new ArrayList<>();
						armorEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.REINFORCEMENT, 1));
						recipe2 = new MerchantRecipe(ItemManager.getDiamondHelmet(0, armorEnchantments), 0, 3, true, 15, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 18));
					}
					case 3, 4 -> {
						ArrayList<EquipEnchantment> armorEnchantments = new ArrayList<>();
						armorEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.REINFORCEMENT, 1));
						recipe2 = new MerchantRecipe(ItemManager.getDiamondBoots(0, armorEnchantments), 0, 3, true, 15, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 16));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 5 -> {
				MerchantRecipe recipe;
				switch (type) {
					case 1, 2 -> {
						ArrayList<EquipEnchantment> armorEnchantments = new ArrayList<>();
						armorEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.REINFORCEMENT, 1));
						recipe = new MerchantRecipe(ItemManager.getDiamondChestplate(0, armorEnchantments), 0, 3, true, 30, 0.2f, false);
						recipe.addIngredient(ItemStack.of(Material.EMERALD, 30));
					}
					case 3, 4 -> {
						ArrayList<EquipEnchantment> armorEnchantments = new ArrayList<>();
						armorEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.REINFORCEMENT, 1));
						recipe = new MerchantRecipe(ItemManager.getDiamondLeggings(0, armorEnchantments), 0, 3, true, 30, 0.2f, false);
						recipe.addIngredient(ItemStack.of(Material.EMERALD, 28));
					}
					default ->
							recipe = error;
				}
				recipes.add(recipe);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the butcher villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getButcherTrades(int type, int level) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1;
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.CHICKEN, 14));
						recipe2 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.PORKCHOP, 7));
					}
					case 2 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.RABBIT, 4));
						recipe2 = new MerchantRecipe(ItemStack.of(Material.RABBIT_STEW, 1), 0, 12, true, 1, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					default -> {
						recipe1 = error;
						recipe2 = error;
					}
				}
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1;
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.COAL, 15));
						recipe2 = new MerchantRecipe(ItemStack.of(Material.COOKED_CHICKEN, 8), 0, 16, true, 5, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 2 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.COOKED_PORKCHOP, 5), 0, 16, true, 5, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.EMERALD, 1));
						recipe2 = new MerchantRecipe(ItemStack.of(Material.COOKED_RABBIT, 3), 0, 16, true, 5, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					default -> {
						recipe1 = error;
						recipe2 = error;
					}
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.BEEF, 8));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.MUTTON, 10));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.DRIED_KELP_BLOCK, 10));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.COOKED_MUTTON, 7), 0, 12, true, 15, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 5 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.SWEET_BERRIES, 10));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.COOKED_BEEF, 6), 0, 12, true, 30, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the cartographer villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @param biome villager biome type
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getCartographerTrades(int type, int level, Villager.Type biome) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 2, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.PAPER, 24));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.MAP, 1), 0, 12, true, 1, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 7));
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);

			}
			case 2 -> {
				MerchantRecipe recipe = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 10, 0.05f, false);
				recipe.addIngredient(ItemStack.of(Material.GLASS_PANE, 8));
				recipes.add(recipe);
			}
			case 3 -> {
				MerchantRecipe recipe = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
				recipe.addIngredient(ItemStack.of(Material.COMPASS, 1));
				recipes.add(recipe);
			}
			case 4 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.ITEM_FRAME, 1), 0, 12, true, 15, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.EMERALD, 7));
				recipes.add(recipe1);
				recipes.add(getCartographerBannerTrade(type, biome));
			}
			case 5 -> {
				MerchantRecipe recipe = new MerchantRecipe(ItemStack.of(Material.GLOBE_BANNER_PATTERN, 1), 0, 12, true, 30, 0.05f, false);
				recipe.addIngredient(ItemStack.of(Material.EMERALD, 8));
				recipes.add(recipe);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the cleric villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getClericTrades(int type, int level) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.ROTTEN_FLESH, 32));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.REDSTONE, 1), 0, 12, true, 1, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.GOLD_INGOT, 3));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.LAPIS_LAZULI, 1), 0, 12, true, 5, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 20, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.RABBIT_FOOT, 2));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.GLOWSTONE, 1), 0, 12, true, 10, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 4));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe1;
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.ENDER_PEARL, 1), 0, 12, true, 15, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 5));
				switch (type) {
					case 1 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 30, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.TURTLE_SCUTE, 4));
					}
					case 2 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 30, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.GLASS_BOTTLE, 9));
					}
					default ->
							recipe1 = error;
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 5 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.NETHER_WART, 22));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.EXPERIENCE_BOTTLE, 1), 0, 12, true, 30, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the farmer villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getFarmerTrades(int type, int level) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1;
				switch (type) {
					case 1 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.WHEAT, 20));
					}
					case 2 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.CARROT, 22));
					}
					case 3 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.POTATO, 26));
					}
					case 4 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 1, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.BEETROOT, 15));
					}
					default ->
							recipe1 = error;
				}
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.BREAD, 6), 0, 16, true, 1, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.PUMPKIN, 6));
				MerchantRecipe recipe2;
				switch (type) {
					case 1, 2 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.PUMPKIN_PIE, 4), 0, 12, true, 5, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 3, 4 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.APPLE, 4), 0, 16, true, 5, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 20, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.MELON, 4));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.COOKIE, 18), 0, 12, true, 10, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 3));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.CAKE, 1), 0, 12, true, 15, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				recipes.add(getSuspiciousStewTrade());
				recipes.add(recipe2);
			}
			case 5 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.GOLDEN_CARROT, 3), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.EMERALD, 3));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.GLISTERING_MELON_SLICE, 3), 0, 12, true, 30, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 4));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the fisherman villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @param biome villager biome type
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getFishermanTrades(int type, int level, Villager.Type biome) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1;
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.STRING, 20));
						recipe2 = new MerchantRecipe(ItemStack.of(Material.COD_BUCKET, 1), 0, 16, true, 1, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 3));
					}
					case 2 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.COAL, 10));
						recipe2 = new MerchantRecipe(ItemStack.of(Material.COOKED_COD, 6), 0, 16, true, 1, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.COD, 6));
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					default -> {
						recipe1 = error;
						recipe2 = error;
					}
				}
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 10, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.COD, 15));
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.CAMPFIRE, 1), 0, 12, true, 5, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 2));
					}
					case 2 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.COOKED_SALMON, 6), 0, 16, true, 5, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.SALMON, 6));
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.SALMON, 13));
				ArrayList<EquipEnchantment> rodEnchantments = new ArrayList<>();
				rodEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.LURE, 1));
				rodEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.LUCK_OF_THE_SEA, 1));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemManager.getFishingRod(0, rodEnchantments), 0, 3, true, 10, 0.2f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 14));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 3, true, 30, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 14));
				recipes.add(getBoatTrade(biome));
				recipes.add(recipe2);
			}
			case 5 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.PUFFERFISH, 4));
				ArrayList<EquipEnchantment> rodEnchantments = new ArrayList<>();
				rodEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.LURE, 3));
				rodEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.LUCK_OF_THE_SEA, 3));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemManager.getFishingRod(0, rodEnchantments), 0, 3, true, 30, 0.2f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 35));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the fletcher villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getFletcherTrades(int type, int level) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.STICK, 32));
				MerchantRecipe recipe2;
				if (type % 2 == 0) {
					recipe2 = new MerchantRecipe(ItemStack.of(Material.FLINT, 10), 0, 12, true, 1, 0.05f, false);
					recipe2.addIngredient(ItemStack.of(Material.GRAVEL, 10));
					recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				}
				else {
					recipe2 = new MerchantRecipe(ItemStack.of(Material.ARROW, 16), 0, 12, true, 1, 0.05f, false);
					recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				}
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.FLINT, 26));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemManager.getBow(0, new ArrayList<>()), 0, 12, true, 5, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 2));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.STRING, 14));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemManager.getCrossbow(0, new ArrayList<>()), 0, 12, true, 10, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 3));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.FEATHER, 24));
				ArrayList<EquipEnchantment> bowEnchantments = new ArrayList<>();
				bowEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.POWER, 3));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemManager.getBow(0, bowEnchantments), 0, 3, true, 15, 0.2f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 20));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 5 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.TRIPWIRE_HOOK, 8));
				MerchantRecipe recipe2;
				if (type == 1) {
					ArrayList<EquipEnchantment> crossbowEnchantments = new ArrayList<>();
					crossbowEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.POWER, 3));
					recipe2 = new MerchantRecipe(ItemManager.getCrossbow(0, crossbowEnchantments), 0, 3, true, 30, 0.2f, false);
					recipe2.addIngredient(ItemStack.of(Material.EMERALD, 22));
				}
				else
					recipe2 = getTippedArrowTrade(type);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the leatherworker villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getLeatherworkerTrades(int type, int level) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.LEATHER, 6));
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						ItemStack helmet = ItemManager.getLeatherCap(0, new ArrayList<>());
						helmet.editMeta(data -> {
							if (data instanceof ColorableArmorMeta colorableArmorData)
								colorableArmorData.setColor(getRandomColor());
						});
						recipe2 = new MerchantRecipe(helmet, 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 2));
					}
					case 2 -> {
						ItemStack chestplate = ItemManager.getLeatherTunic(0, new ArrayList<>());
						chestplate.editMeta(data -> {
							if (data instanceof ColorableArmorMeta colorableArmorData)
								colorableArmorData.setColor(getRandomColor());
						});
						recipe2 = new MerchantRecipe(chestplate, 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 2));
					}
					case 3 -> {
						ItemStack leggings = ItemManager.getLeatherPants(0, new ArrayList<>());
						leggings.editMeta(data -> {
							if (data instanceof ColorableArmorMeta colorableArmorData)
								colorableArmorData.setColor(getRandomColor());
						});
						recipe2 = new MerchantRecipe(leggings, 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 2));
					}
					case 4 -> {
						ItemStack boots = ItemManager.getLeatherBoots(0, new ArrayList<>());
						boots.editMeta(data -> {
							if (data instanceof ColorableArmorMeta colorableArmorData)
								colorableArmorData.setColor(getRandomColor());
						});
						recipe2 = new MerchantRecipe(boots, 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 2));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.FLINT, 26));
				MerchantRecipe recipe2;
				switch (type) {
					case 1, 2 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 5, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 8));
					}
					case 3, 4 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 5, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 5));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 20, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.RABBIT_HIDE, 9));
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 14));
					}
					case 2 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 14));
					}
					case 3 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 14));
					}
					case 4 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 14));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.TURTLE_SCUTE, 4));
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 22));
					}
					case 2 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 22));
					}
					case 3 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 22));
					}
					case 4 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 22));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 5 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.SADDLE, 1), 0, 12, true, 30, 0.2f, false);
				recipe1.addIngredient(ItemStack.of(Material.EMERALD, 6));
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 28));
					}
					case 2 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 28));
					}
					case 3 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 28));
					}
					case 4 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 0, 12, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 28));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the librarian villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getLibrarianTrades(int type, int level) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.PAPER, 24));
				MerchantRecipe recipe2;
				if (type == 1) {
					recipe2 = new MerchantRecipe(ItemStack.of(Material.BOOKSHELF, 1), 0, 12, true, 1, 0.05f, false);
					recipe2.addIngredient(ItemStack.of(Material.EMERALD, 9));
				}
				else
					recipe2 = getEnchantedBook1Trade(type);
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.BOOK, 4));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.LANTERN, 1), 0, 12, true, 5, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 20, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.INK_SAC, 5));
				MerchantRecipe recipe2;
				if (type == 1) {
					recipe2 = new MerchantRecipe(ItemStack.of(Material.GLASS, 4), 0, 12, true, 10, 0.05f, false);
					recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				}
				else
					recipe2 = getEnchantedBook2Trade(type);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.WRITABLE_BOOK, 1));
				recipe1.addIngredient(ItemStack.of(Material.WRITABLE_BOOK, 1));
				MerchantRecipe recipe2;
				if (type % 2 == 1) {
					recipe2 = new MerchantRecipe(ItemStack.of(Material.COMPASS, 1), 0, 12, true, 15, 0.05f, false);
					recipe2.addIngredient(ItemStack.of(Material.EMERALD, 4));
				}
				else {
					recipe2 = new MerchantRecipe(ItemStack.of(Material.CLOCK, 1), 0, 12, true, 15, 0.05f, false);
					recipe2.addIngredient(ItemStack.of(Material.EMERALD, 5));
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 5 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.ENCHANTING_TABLE, 1), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.EMERALD, 38));
				MerchantRecipe recipe2;
				if (type == 1) {
					ArrayList<EquipEnchantment> enchantment = new ArrayList<>();
					enchantment.add(EquipEnchantment.getEnchantment(EnchantmentType.MENDING, 1));
					recipe2 = new MerchantRecipe(ItemManager.getEnchantedBook(enchantment), 0, 12, true, 30, 0.2f, false);
					recipe2.addIngredient(ItemStack.of(Material.EMERALD, 42));
					recipe2.addIngredient(ItemStack.of(Material.BOOK, 1));
				}
				else
					recipe2 = getEnchantedBook3Trade(type);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the mason villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getMasonTrades(int type, int level) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.CLAY_BALL, 10));
				MerchantRecipe recipe2;
				switch (type) {
					case 1, 2, 3, 4, 5, 6, 7, 8 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.BRICK, 10), 0, 16, true, 1, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 9, 10, 11, 12 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.SAND, 4), 0, 16, true, 1, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 13, 14, 15, 16 -> {
						recipe2 = new MerchantRecipe(ItemStack.of(Material.RED_SAND, 4), 0, 16, true, 1, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 10, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.STONE, 20));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.CHISELED_STONE_BRICKS, 4), 0, 16, true, 5, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1;
				MerchantRecipe recipe2;
				switch (type) {
					case 1, 2, 3, 4 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.GRANITE, 16));
						recipe2 = new MerchantRecipe(ItemStack.of(Material.POLISHED_GRANITE, 4), 0, 16, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 5, 6, 7, 8 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.ANDESITE, 16));
						recipe2 = new MerchantRecipe(ItemStack.of(Material.POLISHED_ANDESITE, 4), 0, 16, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 9, 10, 11, 12 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.DIORITE, 16));
						recipe2 = new MerchantRecipe(ItemStack.of(Material.POLISHED_DIORITE, 4), 0, 16, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 13, 14, 15, 16 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.POINTED_DRIPSTONE, 32));
						recipe2 = new MerchantRecipe(ItemStack.of(Material.DRIPSTONE_BLOCK, 4), 0, 16, true, 10, 0.05f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					default -> {
						recipe1 = error;
						recipe2 = error;
					}
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe1 = getColoredTerracottaTrade(type);
				MerchantRecipe recipe2 = getGlazedTerracottaTrade(type);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 5 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.QUARTZ, 12));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.QUARTZ_BLOCK, 1), 0, 12, true, 30, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the shepherd villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getShepherdTrades(int type, int level) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1;
				switch (type) {
					case 1, 2, 3, 4 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.WHITE_WOOL, 18));
					}
					case 5, 6, 7, 8 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.BROWN_WOOL, 18));
					}
					case 9, 10, 11, 12 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.BLACK_WOOL, 18));
					}
					case 13, 14, 15, 16 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.GRAY_WOOL, 18));
					}
					default ->
							recipe1 = error;
				}
				MerchantRecipe recipe2 = new MerchantRecipe(ItemManager.getShears(0, new ArrayList<>()), 0, 12, true, 1, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1;
				switch (type) {
					case 1, 2, 3, 4 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.WHITE_DYE, 12));
					}
					case 5, 6, 7, 8 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.GRAY_DYE, 12));
					}
					case 9, 10, 11, 12 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.BLACK_DYE, 12));
					}
					case 13, 14, 15, 16 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.LIGHT_BLUE_DYE, 12));
					}
					default ->
							recipe1 = error;
				}
				MerchantRecipe recipe2 = getWoolTrade(type);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1;
				switch (type) {
					case 1, 2, 3, 4 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.LIME_DYE, 12));
					}
					case 5, 6, 7, 8 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.YELLOW_DYE, 12));
					}
					case 9, 10, 11, 12 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.LIGHT_GRAY_DYE, 12));
					}
					case 13, 14, 15, 16 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 20, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.ORANGE_DYE, 12));
					}
					default ->
							recipe1 = error;
				}
				MerchantRecipe recipe2 = getBedTrade(type);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe1;
				switch (type) {
					case 1, 2, 3, 4 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 30, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.RED_DYE, 12));
					}
					case 5, 6, 7, 8 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 30, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.PINK_DYE, 12));
					}
					case 9, 10, 11, 12 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 30, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.BROWN_DYE, 12));
					}
					case 13, 14, 15, 16 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 30, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.PURPLE_DYE, 12));
					}
					default ->
							recipe1 = error;
				}
				MerchantRecipe recipe2 = getBannerTrade(type);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 5 -> {
				MerchantRecipe recipe1;
				switch (type) {
					case 1, 2, 3, 4 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 30, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.BLUE_DYE, 12));
					}
					case 5, 6, 7, 8 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 30, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.GREEN_DYE, 12));
					}
					case 9, 10, 11, 12 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 30, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.MAGENTA_DYE, 12));
					}
					case 13, 14, 15, 16 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 30, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.CYAN_DYE, 12));
					}
					default ->
							recipe1 = error;
				}
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.PAINTING, 3), 0, 12, true, 30, 0.05f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 2));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the toolsmith villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getToolsmithTrades(int type, int level) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.COAL, 15));
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						recipe2 = new MerchantRecipe(ItemManager.getCopperAxe(0, new ArrayList<>()), 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 2 -> {
						recipe2 = new MerchantRecipe(ItemManager.getCopperPickaxe(0, new ArrayList<>()), 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 3 -> {
						recipe2 = new MerchantRecipe(ItemManager.getCopperShovel(0, new ArrayList<>()), 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					case 4 -> {
						recipe2 = new MerchantRecipe(ItemManager.getCopperHoe(0, new ArrayList<>()), 0, 12, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 1));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1;
				switch (type) {
					case 1, 2 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.COPPER_INGOT, 12));
					}
					case 3, 4 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.IRON_INGOT, 4));
					}
					default ->
							recipe1 = error;
				}
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.BELL, 1), 0, 12, true, 5, 0.2f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 36));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 20, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.FLINT, 30));
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						ArrayList<EquipEnchantment> toolEnchantments = new ArrayList<>();
						toolEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.EFFICIENCY, 2));
						recipe2 = new MerchantRecipe(ItemManager.getIronAxe(0, toolEnchantments), 0, 3, true, 10, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 12));
					}
					case 2 -> {
						ArrayList<EquipEnchantment> toolEnchantments = new ArrayList<>();
						toolEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.EFFICIENCY, 2));
						recipe2 = new MerchantRecipe(ItemManager.getIronPickaxe(0, toolEnchantments), 0, 3, true, 10, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 12));
					}
					case 3 -> {
						ArrayList<EquipEnchantment> toolEnchantments = new ArrayList<>();
						toolEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.EFFICIENCY, 2));
						recipe2 = new MerchantRecipe(ItemManager.getIronShovel(0, toolEnchantments), 0, 3, true, 10, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 10));
					}
					case 4 -> {
						recipe2 = new MerchantRecipe(ItemManager.getDiamondHoe(0, new ArrayList<>()), 0, 3, true, 10, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 4));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 4), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.DIAMOND, 1));
				MerchantRecipe recipe2;
				switch (type) {
					case 1, 2 -> {
						ArrayList<EquipEnchantment> toolEnchantments = new ArrayList<>();
						toolEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.EFFICIENCY, 2));
						recipe2 = new MerchantRecipe(ItemManager.getDiamondAxe(0, toolEnchantments), 0, 3, true, 15, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 32));
					}
					case 3, 4 -> {
						ArrayList<EquipEnchantment> toolEnchantments = new ArrayList<>();
						toolEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.EFFICIENCY, 2));
						recipe2 = new MerchantRecipe(ItemManager.getDiamondShovel(0, toolEnchantments), 0, 3, true, 15, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 25));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 5 -> {
				ArrayList<EquipEnchantment> toolEnchantments = new ArrayList<>();
				toolEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.EFFICIENCY, 2));
				MerchantRecipe recipe = new MerchantRecipe(ItemManager.getDiamondPickaxe(0, toolEnchantments), 0, 3, true, 30, 0.2f, false);
				recipe.addIngredient(ItemStack.of(Material.EMERALD, 34));
				recipes.add(recipe);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the new trades for the weaponsmith villager
	 * @param type numbered villager type
	 * @param level new villager level
	 * @return list of new trades to add
	 */
	@NotNull
	private static ArrayList<MerchantRecipe> getWeaponsmithTrades(int type, int level) {
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		switch (level) {
			case 1 -> {
				MerchantRecipe villagerType = new MerchantRecipe(getTypeIcon(type), 1);
				villagerType.addIngredient(getTypeIcon(type));
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 2, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.COAL, 15));
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						ArrayList<EquipEnchantment> weaponEnchantments = new ArrayList<>();
						weaponEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.SHARPNESS, 1));
						recipe2 = new MerchantRecipe(ItemManager.getCopperSword(0, weaponEnchantments), 0, 3, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 2));
					}
					case 2 -> {
						ArrayList<EquipEnchantment> weaponEnchantments = new ArrayList<>();
						weaponEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.SHARPNESS, 1));
						recipe2 = new MerchantRecipe(ItemManager.getCopperAxe(0, weaponEnchantments), 0, 3, true, 1, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 2));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(villagerType);
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 2 -> {
				MerchantRecipe recipe1;
				switch (type) {
					case 1 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.COPPER_INGOT, 12));
					}
					case 2 -> {
						recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
						recipe1.addIngredient(ItemStack.of(Material.IRON_INGOT, 4));
					}
					default ->
							recipe1 = error;
				}
				MerchantRecipe recipe2 = new MerchantRecipe(ItemStack.of(Material.BELL, 1), 0, 12, true, 5, 0.2f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 36));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 3 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 20, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.FLINT, 30));
				MerchantRecipe recipe2;
				switch (type) {
					case 1 -> {
						ArrayList<EquipEnchantment> weaponEnchantments = new ArrayList<>();
						weaponEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.SHARPNESS, 2));
						recipe2 = new MerchantRecipe(ItemManager.getIronSword(0, weaponEnchantments), 0, 3, true, 10, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 14));
					}
					case 2 -> {
						ArrayList<EquipEnchantment> weaponEnchantments = new ArrayList<>();
						weaponEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.SHARPNESS, 2));
						recipe2 = new MerchantRecipe(ItemManager.getIronAxe(0, weaponEnchantments), 0, 3, true, 10, 0.2f, false);
						recipe2.addIngredient(ItemStack.of(Material.EMERALD, 16));
					}
					default ->
							recipe2 = error;
				}
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 4 -> {
				MerchantRecipe recipe1 = new MerchantRecipe(ItemStack.of(Material.EMERALD, 4), 0, 12, true, 30, 0.05f, false);
				recipe1.addIngredient(ItemStack.of(Material.DIAMOND, 1));
				ArrayList<EquipEnchantment> weaponEnchantments = new ArrayList<>();
				weaponEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.SHARPNESS, 3));
				MerchantRecipe recipe2 = new MerchantRecipe(ItemManager.getDiamondAxe(0, weaponEnchantments), 0, 3, true, 15, 0.2f, false);
				recipe2.addIngredient(ItemStack.of(Material.EMERALD, 32));
				recipes.add(recipe1);
				recipes.add(recipe2);
			}
			case 5 -> {
				ArrayList<EquipEnchantment> weaponEnchantments = new ArrayList<>();
				weaponEnchantments.add(EquipEnchantment.getEnchantment(EnchantmentType.SHARPNESS, 3));
				MerchantRecipe recipe = new MerchantRecipe(ItemManager.getDiamondSword(0, weaponEnchantments), 0, 3, true, 30, 0.2f, false);
				recipe.addIngredient(ItemStack.of(Material.EMERALD, 29));
				recipes.add(recipe);
			}
			default ->
					recipes.add(error);
		}
		return recipes;
	}

	/**
	 * Gets the banner trade for the cartographer
	 * @param type numbered villager type
	 * @param biome villager biome type
	 * @return banner trade
	 */
	@NotNull
	private static MerchantRecipe getCartographerBannerTrade(int type, @NotNull Villager.Type biome) {
		ItemStack banner;
		if (biome == Villager.Type.PLAINS) {
			switch (type) {
				case 1 ->
						banner = ItemStack.of(Material.BROWN_BANNER, 1);
				case 2 ->
						banner = ItemStack.of(Material.PINK_BANNER, 1);
				case 3 ->
						banner = ItemStack.of(Material.WHITE_BANNER, 1);
				case 4 ->
						banner = ItemStack.of(Material.YELLOW_BANNER, 1);
				default -> {
					return error;
				}
			}
		}
		else if (biome == Villager.Type.TAIGA) {
			switch (type) {
				case 1 ->
						banner = ItemStack.of(Material.BLUE_BANNER, 1);
				case 2 ->
						banner = ItemStack.of(Material.LIME_BANNER, 1);
				case 3 ->
						banner = ItemStack.of(Material.PINK_BANNER, 1);
				case 4 ->
						banner = ItemStack.of(Material.PURPLE_BANNER, 1);
				default -> {
					return error;
				}
			}
		}
		else if (biome == Villager.Type.SNOW) {
			switch (type) {
				case 1 ->
						banner = ItemStack.of(Material.BLUE_BANNER, 1);
				case 2 ->
						banner = ItemStack.of(Material.CYAN_BANNER, 1);
				case 3 ->
						banner = ItemStack.of(Material.LIGHT_BLUE_BANNER, 1);
				case 4 ->
						banner = ItemStack.of(Material.RED_BANNER, 1);
				case 5 ->
						banner = ItemStack.of(Material.WHITE_BANNER, 1);
				default -> {
					return error;
				}
			}
		}
		else if (biome == Villager.Type.DESERT) {
			switch (type) {
				case 1 ->
						banner = ItemStack.of(Material.CYAN_BANNER, 1);
				case 2 ->
						banner = ItemStack.of(Material.GRAY_BANNER, 1);
				case 3 ->
						banner = ItemStack.of(Material.GREEN_BANNER, 1);
				case 4 ->
						banner = ItemStack.of(Material.LIME_BANNER, 1);
				case 5 ->
						banner = ItemStack.of(Material.ORANGE_BANNER, 1);
				default -> {
					return error;
				}
			}
		}
		else if (biome == Villager.Type.JUNGLE) {
			switch (type) {
				case 1 ->
						banner = ItemStack.of(Material.BROWN_BANNER, 1);
				case 2 ->
						banner = ItemStack.of(Material.GREEN_BANNER, 1);
				case 3 ->
						banner = ItemStack.of(Material.YELLOW_BANNER, 1);
				default -> {
					return error;
				}
			}
		}
		else if (biome == Villager.Type.SAVANNA) {
			switch (type) {
				case 1 ->
						banner = ItemStack.of(Material.GREEN_BANNER, 1);
				case 2 ->
						banner = ItemStack.of(Material.MAGENTA_BANNER, 1);
				case 3 ->
						banner = ItemStack.of(Material.ORANGE_BANNER, 1);
				case 4 ->
						banner = ItemStack.of(Material.RED_BANNER, 1);
				default -> {
					return error;
				}
			}
		}
		else if (biome == Villager.Type.SWAMP) {
			switch (type) {
				case 1 ->
						banner = ItemStack.of(Material.BLACK_BANNER, 1);
				case 2 ->
						banner = ItemStack.of(Material.LIGHT_BLUE_BANNER, 1);
				case 3 ->
						banner = ItemStack.of(Material.PURPLE_BANNER, 1);
				default -> {
					return error;
				}
			}
		}
		else
			return error;
		MerchantRecipe recipe = new MerchantRecipe(banner, 0, 12, true, 15, 0.05f, false);
		recipe.addIngredient(ItemStack.of(Material.EMERALD, 3));
		return recipe;
	}

	/**
	 * Gets the suspicious stew trade with a random effect
	 * @return suspicious stew trade
	 */
	@NotNull
	private static MerchantRecipe getSuspiciousStewTrade() {
		ItemStack stew = ItemStack.of(Material.SUSPICIOUS_STEW, 1);
		stew.editMeta(data -> {
			if (data instanceof SuspiciousStewMeta suspiciousStewData) {
				switch (random.nextInt(1, 7)) {
					case 1 ->
							suspiciousStewData.addCustomEffect(SuspiciousEffectEntry.create(PotionEffectType.BLINDNESS, 120), true);
					case 2 ->
							suspiciousStewData.addCustomEffect(SuspiciousEffectEntry.create(PotionEffectType.JUMP_BOOST, 160), true);
					case 3 ->
							suspiciousStewData.addCustomEffect(SuspiciousEffectEntry.create(PotionEffectType.NIGHT_VISION, 100), true);
					case 4 ->
							suspiciousStewData.addCustomEffect(SuspiciousEffectEntry.create(PotionEffectType.POISON, 280), true);
					case 5 ->
							suspiciousStewData.addCustomEffect(SuspiciousEffectEntry.create(PotionEffectType.SATURATION, 7), true);
					case 6 ->
							suspiciousStewData.addCustomEffect(SuspiciousEffectEntry.create(PotionEffectType.WEAKNESS, 140), true);
				}
			}
		});
		MerchantRecipe recipe = new MerchantRecipe(stew, 0, 12, true, 15, 0.05f, false);
		recipe.addIngredient(ItemStack.of(Material.EMERALD, 1));
		return recipe;
	}

	/**
	 * Gets the boat trade for this villager biome
	 * @param biome villager biome type
	 * @return boat trade
	 */
	@NotNull
	private static MerchantRecipe getBoatTrade(Villager.Type biome) {
		Material boatType;
		if (biome == Villager.Type.PLAINS)
			boatType = Material.OAK_BOAT;
		else if (biome == Villager.Type.TAIGA || biome == Villager.Type.SNOW)
			boatType = Material.SPRUCE_BOAT;
		else if (biome == Villager.Type.DESERT || biome == Villager.Type.JUNGLE)
			boatType = Material.JUNGLE_BOAT;
		else if (biome == Villager.Type.SAVANNA)
			boatType = Material.ACACIA_BOAT;
		else if (biome == Villager.Type.SWAMP)
			boatType = Material.DARK_OAK_BOAT;
		else
			return error;
		MerchantRecipe recipe = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 30, 0.05f, false);
		recipe.addIngredient(ItemStack.of(boatType, 1));
		return recipe;
	}

	/**
	 * Gets the tipped arrow trade for this villager type
	 * @param type numbered villager type
	 * @return tipped arrow trade
	 */
	@NotNull
	private static MerchantRecipe getTippedArrowTrade(int type) {
		ItemStack arrow = ItemStack.of(Material.TIPPED_ARROW, 5);
		arrow.editMeta(data -> {
			if (data instanceof PotionMeta potionData) {
				potionData.setBasePotionType(switch (type) {
					case 2 ->
							PotionType.REGENERATION;
					case 3 ->
							PotionType.LONG_REGENERATION;
					case 4 ->
							PotionType.STRONG_REGENERATION;
					case 5 ->
							PotionType.SWIFTNESS;
					case 6 ->
							PotionType.LONG_SWIFTNESS;
					case 7 ->
							PotionType.STRONG_SWIFTNESS;
					case 8 ->
							PotionType.FIRE_RESISTANCE;
					case 9 ->
							PotionType.LONG_FIRE_RESISTANCE;
					case 10 ->
							PotionType.HEALING;
					case 11 ->
							PotionType.STRONG_HEALING;
					case 12 ->
							PotionType.NIGHT_VISION;
					case 13 ->
							PotionType.LONG_NIGHT_VISION;
					case 14 ->
							PotionType.STRENGTH;
					case 15 ->
							PotionType.LONG_STRENGTH;
					case 16 ->
							PotionType.STRONG_STRENGTH;
					case 17 ->
							PotionType.LEAPING;
					case 18 ->
							PotionType.LONG_LEAPING;
					case 19 ->
							PotionType.STRONG_LEAPING;
					case 20 ->
							PotionType.INVISIBILITY;
					case 21 ->
							PotionType.LONG_INVISIBILITY;
					case 22 ->
							PotionType.POISON;
					case 23 ->
							PotionType.LONG_POISON;
					case 24 ->
							PotionType.STRONG_POISON;
					case 25 ->
							PotionType.WEAKNESS;
					case 26 ->
							PotionType.LONG_WEAKNESS;
					case 27 ->
							PotionType.SLOWNESS;
					case 28 ->
							PotionType.LONG_SLOWNESS;
					case 29 ->
							PotionType.STRONG_SLOWNESS;
					case 30 ->
							PotionType.HARMING;
					case 31 ->
							PotionType.STRONG_HARMING;
					case 32 ->
							PotionType.WATER_BREATHING;
					case 33 ->
							PotionType.LONG_WATER_BREATHING;
					case 34 ->
							PotionType.TURTLE_MASTER;
					case 35 ->
							PotionType.LONG_TURTLE_MASTER;
					case 36 ->
							PotionType.STRONG_TURTLE_MASTER;
					case 37 ->
							PotionType.SLOW_FALLING;
					case 38 ->
							PotionType.LONG_SLOW_FALLING;
					case 39 ->
							PotionType.OOZING;
					case 40 ->
							PotionType.WIND_CHARGED;
					case 41 ->
							PotionType.WEAVING;
					case 42 ->
							PotionType.INFESTED;
					default ->
							null;
				});
			}
		});
		MerchantRecipe recipe = new MerchantRecipe(arrow, 0, 12, true, 30, 0.05f, false);
		recipe.addIngredient(ItemStack.of(Material.ARROW, 5));
		recipe.addIngredient(ItemStack.of(Material.EMERALD, 2));
		return recipe;
	}

	/**
	 * Gets the first enchanted book trade for this villager type
	 * @param type numbered villager type
	 * @return first enchanted book trade
	 */
	@NotNull
	private static MerchantRecipe getEnchantedBook1Trade(int type) {
		EquipEnchantment enchantment;
		int cost;
		switch (type) {
			case 2 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.UNBREAKING, 1);
				cost = 5;
			}
			case 3 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.SHARPNESS, 1);
				cost = 8;
			}
			case 4 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.SMITE, 1);
				cost = 6;
			}
			case 5 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.BANE_OF_ARTHROPODS, 1);
				cost = 6;
			}
			case 6 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.POWER, 2);
				cost = 16;
			}
			case 7 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.FIRE_ASPECT, 1);
				cost = 7;
			}
			case 8 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.KNOCKBACK, 1);
				cost = 8;
			}
			case 9 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LOOTING, 1);
				cost = 6;
			}
			case 10 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.SWEEPING_EDGE, 1);
				cost = 5;
			}
			case 11 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LUNGE, 1);
				cost = 5;
			}
			case 12 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.QUICK_CHARGE, 1);
				cost = 6;
			}
			case 13 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.PIERCING, 2);
				cost = 9;
			}
			case 14 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.IMPALING, 1);
				cost = 6;
			}
			case 15 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LOYALTY, 1);
				cost = 8;
			}
			case 16 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.RIPTIDE, 1);
				cost = 9;
			}
			case 17 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.DENSITY, 1);
				cost = 8;
			}
			case 18 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.BREACH, 1);
				cost = 7;
			}
			case 19 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.EFFICIENCY, 1);
				cost = 6;
			}
			case 20 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.FORTUNE, 1);
				cost = 7;
			}
			case 21 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.REINFORCEMENT, 1);
				cost = 8;
			}
			case 22 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.PROTECTION, 1);
				cost = 6;
			}
			case 23 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.BLAST_PROTECTION, 1);
				cost = 6;
			}
			case 24 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.FIRE_PROTECTION, 1);
				cost = 6;
			}
			case 25 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.THORNS, 1);
				cost = 8;
			}
			case 26 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.RESPIRATION, 1);
				cost = 7;
			}
			case 27 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.FEATHER_FALLING, 1);
				cost = 6;
			}
			case 28 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.DEPTH_STRIDER, 1);
				cost = 5;
			}
			case 29 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LURE, 1);
				cost = 6;
			}
			case 30 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LUCK_OF_THE_SEA, 1);
				cost = 6;
			}
			default -> {
				return error;
			}
		}
		ArrayList<EquipEnchantment> enchantmentList = new ArrayList<>();
		enchantmentList.add(enchantment);
		MerchantRecipe recipe = new MerchantRecipe(ItemManager.getEnchantedBook(enchantmentList), 0, 12, true, 1, 0.2f, false);
		recipe.addIngredient(ItemStack.of(Material.EMERALD, cost));
		recipe.addIngredient(ItemStack.of(Material.BOOK, 1));
		return recipe;
	}

	/**
	 * Gets the second enchanted book trade for this villager type
	 * @param type numbered villager type
	 * @return second enchanted book trade
	 */
	@NotNull
	private static MerchantRecipe getEnchantedBook2Trade(int type) {
		EquipEnchantment enchantment;
		int cost;
		switch (type) {
			case 2 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.UNBREAKING, 2);
				cost = 12;
			}
			case 3 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.SHARPNESS, 3);
				cost = 29;
			}
			case 4 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.SMITE, 3);
				cost = 21;
			}
			case 5 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.BANE_OF_ARTHROPODS, 3);
				cost = 21;
			}
			case 6 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.INFINITY, 1);
				cost = 21;
			}
			case 7 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.FLAME, 1);
				cost = 9;
			}
			case 8 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.PUNCH, 2);
				cost = 19;
			}
			case 9 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LOOTING, 2);
				cost = 12;
			}
			case 10 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.SWEEPING_EDGE, 2);
				cost = 11;
			}
			case 11 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LUNGE, 2);
				cost = 13;
			}
			case 12 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.QUICK_CHARGE, 2);
				cost = 11;
			}
			case 13 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.MULTISHOT, 1);
				cost = 12;
			}
			case 14 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.IMPALING, 3);
				cost = 21;
			}
			case 15 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.CHANNELING, 1);
				cost = 10;
			}
			case 16 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.RIPTIDE, 2);
				cost = 21;
			}
			case 17 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.DENSITY, 3);
				cost = 29;
			}
			case 18 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.BREACH, 3);
				cost = 27;
			}
			case 19 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.EFFICIENCY, 3);
				cost = 21;
			}
			case 20 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.SILK_TOUCH, 1);
				cost = 11;
			}
			case 21 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.REINFORCEMENT, 3);
				cost = 29;
			}
			case 22 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.PROTECTION, 3);
				cost = 21;
			}
			case 23 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.BLAST_PROTECTION, 3);
				cost = 21;
			}
			case 24 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.FIRE_PROTECTION, 3);
				cost = 21;
			}
			case 25 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.THORNS, 2);
				cost = 19;
			}
			case 26 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.AQUA_AFFINITY, 1);
				cost = 9;
			}
			case 27 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.FEATHER_FALLING, 3);
				cost = 21;
			}
			case 28 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.DEPTH_STRIDER, 2);
				cost = 12;
			}
			case 29 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LURE, 2);
				cost = 13;
			}
			case 30 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LUCK_OF_THE_SEA, 2);
				cost = 13;
			}
			default -> {
				return error;
			}
		}
		ArrayList<EquipEnchantment> enchantmentList = new ArrayList<>();
		enchantmentList.add(enchantment);
		MerchantRecipe recipe = new MerchantRecipe(ItemManager.getEnchantedBook(enchantmentList), 0, 12, true, 10, 0.2f, false);
		recipe.addIngredient(ItemStack.of(Material.EMERALD, cost));
		recipe.addIngredient(ItemStack.of(Material.BOOK, 1));
		return recipe;
	}

	/**
	 * Gets the third enchanted book trade for this villager type
	 * @param type numbered villager type
	 * @return third enchanted book trade
	 */
	@NotNull
	private static MerchantRecipe getEnchantedBook3Trade(int type) {
		EquipEnchantment enchantment;
		int cost;
		switch (type) {
			case 2 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.UNBREAKING, 3);
				cost = 23;
			}
			case 3 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.SHARPNESS, 5);
				cost = 52;
			}
			case 4 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.SMITE, 5);
				cost = 43;
			}
			case 5 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.BANE_OF_ARTHROPODS, 5);
				cost = 43;
			}
			case 6 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.POWER, 5);
				cost = 52;
			}
			case 7 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.FIRE_ASPECT, 2);
				cost = 16;
			}
			case 8 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.KNOCKBACK, 2);
				cost = 14;
			}
			case 9 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LOOTING, 3);
				cost = 20;
			}
			case 10 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.SWEEPING_EDGE, 3);
				cost = 18;
			}
			case 11 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LUNGE, 3);
				cost = 23;
			}
			case 12 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.QUICK_CHARGE, 3);
				cost = 21;
			}
			case 13 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.PIERCING, 4);
				cost = 24;
			}
			case 14 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.IMPALING, 5);
				cost = 43;
			}
			case 15 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LOYALTY, 3);
				cost = 29;
			}
			case 16 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.RIPTIDE, 3);
				cost = 34;
			}
			case 17 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.DENSITY, 5);
				cost = 52;
			}
			case 18 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.BREACH, 4);
				cost = 39;
			}
			case 19 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.EFFICIENCY, 5);
				cost = 43;
			}
			case 20 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.FORTUNE, 3);
				cost = 24;
			}
			case 21 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.REINFORCEMENT, 4);
				cost = 46;
			}
			case 22 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.PROTECTION, 4);
				cost = 37;
			}
			case 23 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.BLAST_PROTECTION, 4);
				cost = 37;
			}
			case 24 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.FIRE_PROTECTION, 4);
				cost = 37;
			}
			case 25 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.THORNS, 3);
				cost = 31;
			}
			case 26 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.RESPIRATION, 3);
				cost = 24;
			}
			case 27 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.FEATHER_FALLING, 4);
				cost = 37;
			}
			case 28 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.DEPTH_STRIDER, 3);
				cost = 20;
			}
			case 29 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LURE, 3);
				cost = 21;
			}
			case 30 -> {
				enchantment = EquipEnchantment.getEnchantment(EnchantmentType.LUCK_OF_THE_SEA, 3);
				cost = 21;
			}
			default -> {
				return error;
			}
		}
		ArrayList<EquipEnchantment> enchantmentList = new ArrayList<>();
		enchantmentList.add(enchantment);
		MerchantRecipe recipe = new MerchantRecipe(ItemManager.getEnchantedBook(enchantmentList), 0, 12, true, 30, 0.2f, false);
		recipe.addIngredient(ItemStack.of(Material.EMERALD, cost));
		recipe.addIngredient(ItemStack.of(Material.BOOK, 1));
		return recipe;
	}

	/**
	 * Gets the colored terracotta trade for this villager type
	 * @param type numbered villager type
	 * @return colored terracotta trade
	 */
	@NotNull
	private static MerchantRecipe getColoredTerracottaTrade(int type) {
		ItemStack terracotta;
		switch (type) {
			case 1 ->
					terracotta = ItemStack.of(Material.BROWN_TERRACOTTA, 1);
			case 2 ->
					terracotta = ItemStack.of(Material.RED_TERRACOTTA, 1);
			case 3 ->
					terracotta = ItemStack.of(Material.ORANGE_TERRACOTTA, 1);
			case 4 ->
					terracotta = ItemStack.of(Material.YELLOW_TERRACOTTA, 1);
			case 5 ->
					terracotta = ItemStack.of(Material.LIME_TERRACOTTA, 1);
			case 6 ->
					terracotta = ItemStack.of(Material.GREEN_TERRACOTTA, 1);
			case 7 ->
					terracotta = ItemStack.of(Material.CYAN_TERRACOTTA, 1);
			case 8 ->
					terracotta = ItemStack.of(Material.LIGHT_BLUE_TERRACOTTA, 1);
			case 9 ->
					terracotta = ItemStack.of(Material.BLUE_TERRACOTTA, 1);
			case 10 ->
					terracotta = ItemStack.of(Material.PURPLE_TERRACOTTA, 1);
			case 11 ->
					terracotta = ItemStack.of(Material.MAGENTA_TERRACOTTA, 1);
			case 12 ->
					terracotta = ItemStack.of(Material.PINK_TERRACOTTA, 1);
			case 13 ->
					terracotta = ItemStack.of(Material.BLACK_TERRACOTTA, 1);
			case 14 ->
					terracotta = ItemStack.of(Material.GRAY_TERRACOTTA, 1);
			case 15 ->
					terracotta = ItemStack.of(Material.LIGHT_GRAY_TERRACOTTA, 1);
			case 16 ->
					terracotta = ItemStack.of(Material.WHITE_TERRACOTTA, 1);
			default -> {
				return error;
			}
		}
		MerchantRecipe recipe = new MerchantRecipe(terracotta, 0, 12, true, 15, 0.05f, false);
		recipe.addIngredient(ItemStack.of(Material.EMERALD, 1));
		return recipe;
	}

	/**
	 * Gets the glazed terracotta trade for this villager type
	 * @param type numbered villager type
	 * @return glazed terracotta trade
	 */
	@NotNull
	private static MerchantRecipe getGlazedTerracottaTrade(int type) {
		ItemStack terracotta;
		switch (type) {
			case 1 ->
					terracotta = ItemStack.of(Material.BROWN_GLAZED_TERRACOTTA, 1);
			case 2 ->
					terracotta = ItemStack.of(Material.RED_GLAZED_TERRACOTTA, 1);
			case 3 ->
					terracotta = ItemStack.of(Material.ORANGE_GLAZED_TERRACOTTA, 1);
			case 4 ->
					terracotta = ItemStack.of(Material.YELLOW_GLAZED_TERRACOTTA, 1);
			case 5 ->
					terracotta = ItemStack.of(Material.LIME_GLAZED_TERRACOTTA, 1);
			case 6 ->
					terracotta = ItemStack.of(Material.GREEN_GLAZED_TERRACOTTA, 1);
			case 7 ->
					terracotta = ItemStack.of(Material.CYAN_GLAZED_TERRACOTTA, 1);
			case 8 ->
					terracotta = ItemStack.of(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, 1);
			case 9 ->
					terracotta = ItemStack.of(Material.BLUE_GLAZED_TERRACOTTA, 1);
			case 10 ->
					terracotta = ItemStack.of(Material.PURPLE_GLAZED_TERRACOTTA, 1);
			case 11 ->
					terracotta = ItemStack.of(Material.MAGENTA_GLAZED_TERRACOTTA, 1);
			case 12 ->
					terracotta = ItemStack.of(Material.PINK_GLAZED_TERRACOTTA, 1);
			case 13 ->
					terracotta = ItemStack.of(Material.BLACK_GLAZED_TERRACOTTA, 1);
			case 14 ->
					terracotta = ItemStack.of(Material.GRAY_GLAZED_TERRACOTTA, 1);
			case 15 ->
					terracotta = ItemStack.of(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, 1);
			case 16 ->
					terracotta = ItemStack.of(Material.WHITE_GLAZED_TERRACOTTA, 1);
			default -> {
				return error;
			}
		}
		MerchantRecipe recipe = new MerchantRecipe(terracotta, 0, 12, true, 15, 0.05f, false);
		recipe.addIngredient(ItemStack.of(Material.EMERALD, 1));
		return recipe;
	}

	/**
	 * Gets the wool trade for this villager type
	 * @param type numbered villager type
	 * @return wool trade
	 */
	@NotNull
	private static MerchantRecipe getWoolTrade(int type) {
		ItemStack wool;
		switch (type) {
			case 1 ->
					wool = ItemStack.of(Material.BROWN_WOOL, 1);
			case 2 ->
					wool = ItemStack.of(Material.RED_WOOL, 1);
			case 3 ->
					wool = ItemStack.of(Material.ORANGE_WOOL, 1);
			case 4 ->
					wool = ItemStack.of(Material.YELLOW_WOOL, 1);
			case 5 ->
					wool = ItemStack.of(Material.LIME_WOOL, 1);
			case 6 ->
					wool = ItemStack.of(Material.GREEN_WOOL, 1);
			case 7 ->
					wool = ItemStack.of(Material.CYAN_WOOL, 1);
			case 8 ->
					wool = ItemStack.of(Material.LIGHT_BLUE_WOOL, 1);
			case 9 ->
					wool = ItemStack.of(Material.BLUE_WOOL, 1);
			case 10 ->
					wool = ItemStack.of(Material.PURPLE_WOOL, 1);
			case 11 ->
					wool = ItemStack.of(Material.MAGENTA_WOOL, 1);
			case 12 ->
					wool = ItemStack.of(Material.PINK_WOOL, 1);
			case 13 ->
					wool = ItemStack.of(Material.BLACK_WOOL, 1);
			case 14 ->
					wool = ItemStack.of(Material.GRAY_WOOL, 1);
			case 15 ->
					wool = ItemStack.of(Material.LIGHT_GRAY_WOOL, 1);
			case 16 ->
					wool = ItemStack.of(Material.WHITE_WOOL, 1);
			default -> {
				return error;
			}
		}
		MerchantRecipe recipe = new MerchantRecipe(wool, 0, 16, true, 5, 0.05f, false);
		recipe.addIngredient(ItemStack.of(Material.EMERALD, 1));
		return recipe;
	}

	/**
	 * Gets the banner trade for this villager type
	 * @param type numbered villager type
	 * @return banner trade
	 */
	@NotNull
	private static MerchantRecipe getBannerTrade(int type) {
		ItemStack banner;
		switch (type) {
			case 1 ->
					banner = ItemStack.of(Material.BROWN_BANNER, 1);
			case 2 ->
					banner = ItemStack.of(Material.RED_BANNER, 1);
			case 3 ->
					banner = ItemStack.of(Material.ORANGE_BANNER, 1);
			case 4 ->
					banner = ItemStack.of(Material.YELLOW_BANNER, 1);
			case 5 ->
					banner = ItemStack.of(Material.LIME_BANNER, 1);
			case 6 ->
					banner = ItemStack.of(Material.GREEN_BANNER, 1);
			case 7 ->
					banner = ItemStack.of(Material.CYAN_BANNER, 1);
			case 8 ->
					banner = ItemStack.of(Material.LIGHT_BLUE_BANNER, 1);
			case 9 ->
					banner = ItemStack.of(Material.BLUE_BANNER, 1);
			case 10 ->
					banner = ItemStack.of(Material.PURPLE_BANNER, 1);
			case 11 ->
					banner = ItemStack.of(Material.MAGENTA_BANNER, 1);
			case 12 ->
					banner = ItemStack.of(Material.PINK_BANNER, 1);
			case 13 ->
					banner = ItemStack.of(Material.BLACK_BANNER, 1);
			case 14 ->
					banner = ItemStack.of(Material.GRAY_BANNER, 1);
			case 15 ->
					banner = ItemStack.of(Material.LIGHT_GRAY_BANNER, 1);
			case 16 ->
					banner = ItemStack.of(Material.WHITE_BANNER, 1);
			default -> {
				return error;
			}
		}
		MerchantRecipe recipe = new MerchantRecipe(banner, 0, 12, true, 15, 0.05f, false);
		recipe.addIngredient(ItemStack.of(Material.EMERALD, 3));
		return recipe;
	}

	/**
	 * Gets the bed trade for this villager type
	 * @param type numbered villager type
	 * @return bed trade
	 */
	@NotNull
	private static MerchantRecipe getBedTrade(int type) {
		ItemStack bed;
		switch (type) {
			case 1 ->
					bed = ItemStack.of(Material.BROWN_BED, 1);
			case 2 ->
					bed = ItemStack.of(Material.RED_BED, 1);
			case 3 ->
					bed = ItemStack.of(Material.ORANGE_BED, 1);
			case 4 ->
					bed = ItemStack.of(Material.YELLOW_BED, 1);
			case 5 ->
					bed = ItemStack.of(Material.LIME_BED, 1);
			case 6 ->
					bed = ItemStack.of(Material.GREEN_BED, 1);
			case 7 ->
					bed = ItemStack.of(Material.CYAN_BED, 1);
			case 8 ->
					bed = ItemStack.of(Material.LIGHT_BLUE_BED, 1);
			case 9 ->
					bed = ItemStack.of(Material.BLUE_BED, 1);
			case 10 ->
					bed = ItemStack.of(Material.PURPLE_BED, 1);
			case 11 ->
					bed = ItemStack.of(Material.MAGENTA_BED, 1);
			case 12 ->
					bed = ItemStack.of(Material.PINK_BED, 1);
			case 13 ->
					bed = ItemStack.of(Material.BLACK_BED, 1);
			case 14 ->
					bed = ItemStack.of(Material.GRAY_BED, 1);
			case 15 ->
					bed = ItemStack.of(Material.LIGHT_GRAY_BED, 1);
			case 16 ->
					bed = ItemStack.of(Material.WHITE_BED, 1);
			default -> {
				return error;
			}
		}
		MerchantRecipe recipe = new MerchantRecipe(bed, 0, 12, true, 10, 0.05f, false);
		recipe.addIngredient(ItemStack.of(Material.EMERALD, 3));
		return recipe;
	}

	/**
	 * Gets a random color to display on leather armor
	 * @return random color
	 */
	@NotNull
	private static Color getRandomColor() {
		DyeColor[] colors = DyeColor.values();
		return colors[random.nextInt(1, colors.length)].getColor().mixDyes(colors[random.nextInt(1, colors.length)]);
	}

	/**
	 * Gets the icon to display the villager type
	 * @param type numbered villager type
	 * @return type icon
	 */
	@NotNull
	private static ItemStack getTypeIcon(int type) {
		ItemStack icon = ItemStack.of(Material.PAPER, 1);
		icon.editMeta(data -> {
			data.customName(textNI("Villager Type: " + type, NamedTextColor.YELLOW));
			ArrayList<TextComponent> lore = new ArrayList<>();
			lore.add(Component.empty());
			lore.add(textNI("For information on villager offers, see the documentation", NamedTextColor.GRAY));
			data.lore(lore);
		});
		return icon;
	}
}

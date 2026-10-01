package io.github.YellowStoneTorch.SMP_Expansion;

import com.destroystokyo.paper.event.player.PlayerRecipeBookClickEvent;
import io.github.YellowStoneTorch.SMP_Expansion.Claims.ClaimManager;
import io.github.YellowStoneTorch.SMP_Expansion.Claims.OutpostCollection;
import io.github.YellowStoneTorch.SMP_Expansion.Claims.Permission;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EnchantmentType;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EquipEnchantment;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.ItemManager;
import io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus.AnvilMenu;
import io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus.EnchantingTableMenu;
import io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus.InventoryMenu;
import io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus.SmithingTableMenu;
import io.github.YellowStoneTorch.SMP_Expansion.Teams.TeamManager;
import io.github.YellowStoneTorch.SMP_Expansion.Villagers.VillagerData;
import io.papermc.paper.event.player.PlayerInsertLecternBookEvent;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Switch;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.*;
import static io.github.YellowStoneTorch.SMP_Expansion.TextModule.*;
import static net.kyori.adventure.text.Component.text;
import static org.bukkit.Tag.ENTITY_TYPES_ARTHROPOD;
import static org.bukkit.Tag.ENTITY_TYPES_UNDEAD;
import static org.bukkit.entity.Villager.Profession.NITWIT;
import static org.bukkit.entity.Villager.Profession.NONE;

/**
 * Listens for all events, then passes off code
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class EventListener implements Listener {

	/**
	 * Event called when a block is broken by a player
	 * @param event BlockBreakEvent
	 */
	@EventHandler
	public void onBlockBreak(@NotNull BlockBreakEvent event) {
		event.setCancelled(editWorldCheck(event.getBlock().getChunk(), event.getPlayer(), true));
	}

	/**
	 * Event called when a block explodes
	 * @param event BlockExplodeEvent
	 */
	@EventHandler
	public void onBlockExplode(@NotNull BlockExplodeEvent event) {
		List<Block> explodedBlocks = event.blockList();
		for (int i = 0; i < explodedBlocks.size(); ) {
			Chunk chunk = explodedBlocks.get(i).getChunk();
			if (ClaimManager.getOutpost(chunk) instanceof OutpostCollection outpost && outpost.permsEditWorld() != Permission.ALL)
				explodedBlocks.remove(i);
			else
				i++;
		}
	}

	/**
	 * Event called when a piston pushes a block
	 * @param event BlockPistonExtendEvent
	 */
	@EventHandler
	public void onBlockPistonExtend(@NotNull BlockPistonExtendEvent event) {
		Block piston = event.getBlock();
		World world = piston.getWorld();
		List<Block> movedBlocks = event.getBlocks();
		ArrayList<Block> newBlockPositions = new ArrayList<>();
		BlockFace direction = event.getDirection();
		for (Block block: movedBlocks)
			newBlockPositions.add(world.getBlockAt(block.getX() + direction.getModX(), block.getY() + direction.getModY(), block.getZ() + direction.getModZ()));
		ArrayList<Block> allMovedBlocks = new ArrayList<>(movedBlocks);
		allMovedBlocks.addAll(newBlockPositions);
		OutpostCollection pistonOutpost = ClaimManager.getOutpost(piston.getChunk());
		for (Block block: allMovedBlocks) {
			OutpostCollection blockOutpost = ClaimManager.getOutpost(block.getChunk());
			if (block.getType() != Material.AIR && blockOutpost != null && blockOutpost.permsEditWorld() != Permission.ALL && pistonOutpost != blockOutpost && (pistonOutpost == null || pistonOutpost.getOwner() != blockOutpost.getOwner())) {
				event.setCancelled(true);
				return;
			}
		}
	}

	/**
	 * Event called when a piston retracts a block
	 * @param event BlockPistonRetractEvent
	 */
	@EventHandler
	public void onBlockPistonRetract(@NotNull BlockPistonRetractEvent event) {
		Block piston = event.getBlock();
		World world = piston.getWorld();
		List<Block> movedBlocks = event.getBlocks();
		ArrayList<Block> newBlockPositions = new ArrayList<>();
		BlockFace direction = event.getDirection();
		for (Block block: movedBlocks)
			newBlockPositions.add(world.getBlockAt(block.getX() - direction.getModX(), block.getY() - direction.getModY(), block.getZ() - direction.getModZ()));
		ArrayList<Block> allMovedBlocks = new ArrayList<>(movedBlocks);
		allMovedBlocks.addAll(newBlockPositions);
		OutpostCollection pistonOutpost = ClaimManager.getOutpost(piston.getChunk());
		for (Block block: allMovedBlocks) {
			OutpostCollection blockOutpost = ClaimManager.getOutpost(block.getChunk());
			if (block.getType() != Material.AIR && blockOutpost != null && blockOutpost.permsEditWorld() != Permission.ALL && pistonOutpost != blockOutpost && (pistonOutpost == null || pistonOutpost.getOwner() != blockOutpost.getOwner())) {
				event.setCancelled(true);
				return;
			}
		}
	}

	/**
	 * Event called when a block is placed by a player
	 * @param event BlockPlaceEvent
	 */
	@EventHandler
	public void onBlockPlace(@NotNull BlockPlaceEvent event) {
		event.setCancelled(editWorldCheck(event.getBlock().getChunk(), event.getPlayer(), true));
	}

	/**
	 * Event called when a crafter crafts an item
	 * @param event CrafterCraftEvent
	 */
	@EventHandler
	public void onCrafterCraft(@NotNull CrafterCraftEvent event) {
		event.setResult(ItemManager.convertItem(event.getResult()));
	}

	/**
	 * Event called when an entity forms a block in the world
	 * @param event EntityBlockFormEvent
	 */
	@EventHandler
	public void onEntityBlockForm(@NotNull EntityBlockFormEvent event) {
		event.setCancelled(editWorldCheck(event.getBlock().getChunk(), event.getEntity()));
	}

	/**
	 * Event called when an entity changes a block in the world
	 * @param event EntityChangeBlockEvent
	 */
	@EventHandler
	public void onEntityChangeBlock(@NotNull EntityChangeBlockEvent event) {
		event.setCancelled(editWorldCheck(event.getBlock().getChunk(), event.getEntity()));
	}

	/**
	 * Event called when an entity takes damage
	 * @param event EntityDamageEvent
	 */
	@EventHandler
	public void onEntityDamage(@NotNull EntityDamageEvent event) {
		Entity victim = event.getEntity();
		double damage = event.getDamage();
		DamageType damageType = event.getDamageSource().getDamageType();
		if (event instanceof EntityDamageByEntityEvent event1) {
			Entity attacker;
			if (event1.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Entity shooter) {
				attacker = shooter;
				if (projectile instanceof AbstractArrow arrow && !(arrow instanceof Trident) && arrow.getPersistentDataContainer().getOrDefault(isCustom, PersistentDataType.BOOLEAN, false)) {
					if (arrow.isShotFromCrossbow())
						damage = arrow.getDamage();
					else {
						damage = Math.ceil(arrow.getDamage() * arrow.getVelocity().length());
						if (arrow.isCritical())
							damage += 2;
					}
				}
				if (victim instanceof LivingEntity entity)
					new BukkitRunnable() {
						@Override
						public void run() {
							entity.setNoDamageTicks(0);
						}
					}.runTask(SMP_Expansion.getPlugin());
			}
			else
				attacker = event1.getDamager();
			if (attacker instanceof LivingEntity entity) {
				if (!(victim instanceof Enemy) && !(victim instanceof Player)) {
					Chunk chunk = victim.getChunk();
					if (attacker instanceof Player player && editWorldCheck(chunk, player, true)) {
						event.setCancelled(true);
						return;
					}
					else if (editWorldCheck(chunk, entity)) {
						event.setCancelled(true);
						return;
					}
				}
				if (victim instanceof Player player1 && attacker instanceof Player player2 && player1 != player2) {
					PersistentDataContainer player1Data = player1.getPersistentDataContainer();
					PersistentDataContainer player2Data = player2.getPersistentDataContainer();
					if (Config.getEnablePvPCooldown() && player1Data.getOrDefault(pvp, PersistentDataType.BOOLEAN, false) && player2Data.getOrDefault(pvp, PersistentDataType.BOOLEAN, false)) {
						int player1Cooldown = player1Data.getOrDefault(pvpCooldown, PersistentDataType.INTEGER, 0);
						int player2Cooldown = player2Data.getOrDefault(pvpCooldown, PersistentDataType.INTEGER, 0);
						player1Data.set(pvpCooldown, PersistentDataType.INTEGER, 600);
						if (player1Cooldown == 0)
							new BukkitRunnable() {
								@Override
								public void run() {
									int currentCooldown = player1Data.getOrDefault(pvpCooldown, PersistentDataType.INTEGER, 0);
									if (currentCooldown <= 0) {
										player1.sendActionBar(text("You are no longer combat tagged", NamedTextColor.GREEN));
										player1Data.remove(pvpCooldown);
										cancel();
									}
									else {
										if (currentCooldown % 100 == 0)
											player1.sendActionBar(text("Combat Tagged for " + TimeModule.getTimeDisplayTicks(currentCooldown), NamedTextColor.RED));
										player1Data.set(pvpCooldown, PersistentDataType.INTEGER, currentCooldown - 1);
									}
								}
							}.runTaskTimer(SMP_Expansion.getPlugin(), 1, 1);
						player2Data.set(pvpCooldown, PersistentDataType.INTEGER, 600);
						if (player2Cooldown == 0)
							new BukkitRunnable() {
								@Override
								public void run() {
									int currentCooldown = player2Data.getOrDefault(pvpCooldown, PersistentDataType.INTEGER, 0);
									if (currentCooldown <= 0) {
										player2.sendActionBar(Component.empty());
										player2Data.remove(pvpCooldown);
										cancel();
									}
									else {
										if (currentCooldown % 100 == 0)
											player2.sendActionBar(text("Combat Tagged for " + TimeModule.getTimeDisplayTicks(currentCooldown), NamedTextColor.RED));
										player2Data.set(pvpCooldown, PersistentDataType.INTEGER, currentCooldown - 1);
									}
								}
							}.runTaskTimer(SMP_Expansion.getPlugin(), 1, 1);
						if (player1Data.getOrDefault(teleportCountdown, PersistentDataType.INTEGER, 0) != 0)
							player1Data.set(teleportCountdown, PersistentDataType.INTEGER, 0);
						if (player2Data.getOrDefault(teleportCountdown, PersistentDataType.INTEGER, 0) != 0)
							player2Data.set(teleportCountdown, PersistentDataType.INTEGER, 0);
					}
					else {
						event.setCancelled(true);
						return;
					}
				}
				if (damageType == DamageType.MACE_SMASH) {
					ItemStack mace;
					if (entity instanceof Player player)
						mace = player.getInventory().getItemInMainHand();
					else if (entity instanceof Mob mob) {
						EntityEquipment equipment = mob.getEquipment();
						if (equipment instanceof EntityEquipment entityEquipment)
							mace = entityEquipment.getItemInMainHand();
						else
							mace = ItemManager.getMace(0, new ArrayList<>());
					}
					else
						mace = ItemManager.getMace(0, new ArrayList<>());
					if (mace.getType() == Material.MACE) {
						Collection<AttributeModifier> attributeModifiers = mace.getItemMeta().getAttributeModifiers(Attribute.ATTACK_DAMAGE);
						double baseDamage = 1;
						if (attributeModifiers != null)
							for (AttributeModifier attributeModifier: attributeModifiers)
								if (attributeModifier.getOperation() == AttributeModifier.Operation.ADD_NUMBER)
									baseDamage += attributeModifier.getAmount();
						double enchantDamage = 0;
						double densityLvl = 0;
						for (EquipEnchantment enchantment: EquipEnchantment.getEnchantments(mace))
							switch (enchantment.type) {
								case SHARPNESS ->
										enchantDamage += (enchantment.level + 1) / 2.0;
								case SMITE -> {
									enchantDamage += (enchantment.level + 1) / 4.0;
									if (ENTITY_TYPES_UNDEAD.isTagged(victim.getType()))
										enchantDamage += (enchantment.level + 1) * 1.25;
								}
								case BANE_OF_ARTHROPODS -> {
									enchantDamage += (enchantment.level + 1) / 4.0;
									if (ENTITY_TYPES_ARTHROPOD.isTagged(victim.getType()))
										enchantDamage += (enchantment.level + 1) * 1.25;
								}
								case DENSITY ->
										densityLvl += enchantment.level;
							}
						if (entity instanceof Player player) {
							baseDamage *= 0.2 + (0.8 * Math.pow(player.getCooledAttackStrength(0), 2));
							enchantDamage *= player.getCooledAttackStrength(0);
						}
						if (event1.isCritical())
							baseDamage *= 1.5;
						double fallDistance = entity.getFallDistance() * (1 + (0.05 * densityLvl));
						double smashDamage = Math.round(4 * (Math.log(2 + fallDistance) / Math.log(2))) - 4;
						damage = baseDamage + enchantDamage + smashDamage;
					}
				}
				else if (damageType == DamageType.SPEAR && entity.hasActiveItem())
					damage = Math.round(8 * (Math.log(damage) / Math.log(4))) - 4;
			}
			if (victim instanceof Player player && attacker instanceof Mob) {
				switch (player.getPersistentDataContainer().getOrDefault(difficulty, PersistentDataType.STRING, "normal")) {
					case "easy" ->
							damage = Math.round(damage * 70) * 0.01;
					case "hard" ->
							damage = Math.round(damage * 130) * 0.01;
					case "extraHard" ->
							damage = Math.round(damage * 160) * 0.01;
				}
			}
		}
		event.setDamage(damage);
	}

	/**
	 * Event called when an entity dies
	 * @param event EntityDeathEvent
	 */
	@EventHandler
	public void onEntityDeath(@NotNull EntityDeathEvent event) {
		Entity victim = event.getEntity();
		if (event instanceof PlayerDeathEvent event1 && victim instanceof Player player && !event.isCancelled()) {
			PersistentDataContainer playerData = player.getPersistentDataContainer();
			playerData.set(pvpCooldown, PersistentDataType.INTEGER, 0);
			if (!event1.getKeepInventory()) {
				PlayerInventory inventory = player.getInventory();
				event1.setKeepInventory(true);
				List<ItemStack> drops = event1.getDrops();
				drops.clear();
				if (!playerData.getOrDefault(keepInvArmor, PersistentDataType.BOOLEAN, true)) {
					for (int i = 36; i < 40; i++) {
						drops.add(inventory.getItem(i));
						inventory.setItem(i, null);
					}
				}
				if (!playerData.getOrDefault(keepInvHotbar, PersistentDataType.BOOLEAN, true)) {
					for (int i = 0; i < 9; i++) {
						drops.add(inventory.getItem(i));
						inventory.setItem(i, null);
					}
					drops.add(inventory.getItem(40));
					inventory.setItem(40, null);
				}
				if (!playerData.getOrDefault(keepInvInventory, PersistentDataType.BOOLEAN, true)) {
					for (int i = 9; i < 36; i++) {
						drops.add(inventory.getItem(i));
						inventory.setItem(i, null);
					}
				}
				boolean dropExperience = playerData.getOrDefault(keepInvXP, PersistentDataType.BOOLEAN, true);
				event1.setKeepLevel(dropExperience);
				event1.setShouldDropExperience(!dropExperience);
			}
		}
	}

	/**
	 * Event called when an entity explodes
	 * @param event EntityExplodeEvent
	 */
	@EventHandler
	public void onEntityExplode(@NotNull EntityExplodeEvent event) {
		List<Block> explodedBlocks = event.blockList();
		for (int i = 0; i < explodedBlocks.size(); ) {
			Chunk chunk = explodedBlocks.get(i).getChunk();
			if (ClaimManager.getOutpost(chunk) instanceof OutpostCollection outpost && outpost.permsEditWorld() != Permission.ALL)
				explodedBlocks.remove(i);
			else
				i++;
		}
	}

	/**
	 * Event called when an entity picks up an item
	 * @param event EntityPickupItemEvent
	 */
	@EventHandler
	public void onEntityPickupItem(@NotNull EntityPickupItemEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player player) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Inventory inventory = player.getInventory();
					ItemStack[] items = inventory.getContents();
					for (int i = 0; i < items.length; i++)
						items[i] = ItemManager.convertItem(items[i]);
					inventory.setContents(items);
				}
			}.runTask(SMP_Expansion.getPlugin());
		}
	}

	/**
	 * Event called when an entity is placed in the world
	 * @param event EntityPlaceEvent
	 */
	@EventHandler
	public void onEntityPlace(@NotNull EntityPlaceEvent event) {
		event.setCancelled(editWorldCheck(event.getBlock().getChunk(), event.getPlayer(), true));
	}

	/**
	 * Event called when a hanging entity is broken
	 * @param event HangingBreakEvent
	 */
	@EventHandler
	public void onHangingBreak(@NotNull HangingBreakEvent event) {
		event.setCancelled(ClaimManager.getOutpost(event.getEntity().getChunk()) != null);
	}

	/**
	 * Event called when an entity breakes a hanging entity
	 * @param event HangingBreakByEntityEvent
	 */
	@EventHandler
	public void onHangingBreakByEntity(@NotNull HangingBreakByEntityEvent event) {
		if (event.getRemover() instanceof Player player)
			event.setCancelled(editWorldCheck(event.getEntity().getChunk(), player, true));
	}

	/**
	 * Event called when a hanging entity is placed in the world
	 * @param event HangingPlaceEvent
	 */
	@EventHandler
	public void onHangingPlace(@NotNull HangingPlaceEvent event) {
		event.setCancelled(editWorldCheck(event.getEntity().getChunk(), event.getPlayer(), true));
	}

	/**
	 * Event called when an inventory slot is clicked
	 * @param event InventoryClickEvent
	 */
	@EventHandler
	public void onInventoryClick(@NotNull InventoryClickEvent event) {
		InventoryHolder holder = event.getInventory().getHolder();
		if (holder instanceof InventoryMenu menu)
			menu.onClick(event);
		else {
			if (event.getWhoClicked() instanceof Player player) {
				int slot = event.getRawSlot();
				Inventory invOpen = player.getOpenInventory().getTopInventory();
				if (invOpen instanceof CraftingInventory || invOpen instanceof PlayerInventory) {
					// Converts item in crafting output slot
					new BukkitRunnable() {
						@Override
						public void run() {
							if (slot == 0) {
								Inventory playerInv = player.getInventory();
								ItemManager.convertAllItems(playerInv);
							}
							else
								invOpen.setItem(0, ItemManager.convertItem(invOpen.getItem(0)));
						}
					}.runTask(SMP_Expansion.getPlugin());
				}
				else if (invOpen instanceof GrindstoneInventory) {
					// Awards XP for using grindstone, or converts grindstone output
					ItemStack input1 = invOpen.getItem(0);
					ItemStack input2 = invOpen.getItem(1);
					if (slot == 2 && invOpen.getItem(2) != null && event.getAction() != InventoryAction.NOTHING) {
						new BukkitRunnable() {
							@Override
							public void run() {
								PersistentDataContainer playerData = player.getPersistentDataContainer();
								ArrayList<EquipEnchantment> enchantments = new ArrayList<>();
								if (input1 != null)
									enchantments.addAll(EquipEnchantment.getEnchantments(input1));
								if (input2 != null)
									enchantments.addAll(EquipEnchantment.getEnchantments(input2));
								for (EquipEnchantment enchantment: enchantments) {
									switch (enchantment.type.rarity) {
										case COMMON, RARE -> {
											int xp = enchantment.level;
											int playerXP = playerData.getOrDefault(enchantment.type.key, PersistentDataType.INTEGER, 0);
											int currentMaxLevel = EquipEnchantment.getLevelFromXP(enchantment.type, playerXP);
											int newMaxLevel = EquipEnchantment.getLevelFromXP(enchantment.type, playerXP + xp);
											playerData.set(enchantment.type.key, PersistentDataType.INTEGER, xp + playerXP);
											player.sendMessage(build(comp(xp + " XP awarded from enchantment ", NamedTextColor.GREEN), enchantment.getTitleBuilder()));
											if (newMaxLevel > currentMaxLevel)
												player.sendMessage(build(EnchantmentType.getTitleBuilder(enchantment.type, newMaxLevel), comp(" has been unlocked at the enchanting table.", NamedTextColor.GREEN)));
										}
									}
								}
							}
						}.runTask(SMP_Expansion.getPlugin());
					}
					else {
						new BukkitRunnable() {

							@Override
							public void run() {
								ItemStack output = invOpen.getItem(2);
								if (output != null) {
									if (output.getType() == Material.BOOK) {
										Component customName = output.getItemMeta().customName();
										output = ItemStack.of(Material.BOOK, output.getAmount());
										if (customName != null)
											output.editMeta(data -> data.displayName(customName));
										invOpen.setItem(2, output);
									}
									else
										invOpen.setItem(2, EquipEnchantment.removeAllEnchantments(output));
								}
							}
						}.runTask(SMP_Expansion.getPlugin());
					}
				}
				else if (event.getView() instanceof AnvilView view) {
					event.setCancelled(true);
					Component name = view.title();
					if (slot == 2 && invOpen.getItem(2) instanceof ItemStack output && name instanceof TextComponent customName) {
						if (player.getLevel() > 0) {
							String itemName = view.getRenameText();
							if (itemName == null || itemName.isEmpty())
								output.editMeta(data -> data.getPersistentDataContainer().remove(renamed));
							else
								output.editMeta(data -> {
									Map<TextDecoration, TextDecoration.State> decorationMap = customName.decorations();
									ArrayList<TextDecoration> decorations = new ArrayList<>();
									for (TextDecoration decoration: decorationMap.keySet())
										if (decorationMap.get(decoration) == TextDecoration.State.TRUE)
											decorations.add(decoration);
									if (customName.hasDecoration(TextDecoration.ITALIC))
										data.customName(text(itemName, customName.color(), decorations.toArray(TextDecoration[]::new)));
									else
										data.customName(textNI(itemName, customName.color(), decorations.toArray(TextDecoration[]::new)));
									data.getPersistentDataContainer().set(renamed, PersistentDataType.BOOLEAN, true);
								});
							player.give(output);
							invOpen.setItem(0, null);
							player.setLevel(player.getLevel() - 1);
							player.playSound(player, Sound.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1);
							view.close();
						}
					}
				}
			}
		}
	}

	/**
	 * Event called when an inventory is closed
	 * @param event InventoryCloseEvent
	 */
	@EventHandler
	public void onInventoryClose(@NotNull InventoryCloseEvent event) {
		InventoryHolder inventory = event.getInventory().getHolder();
		if (inventory instanceof InventoryMenu menu)
			menu.onClose();
	}

	/**
	 * Event called when items are dragged across inventory slots
	 * @param event InventoryDragEvent
	 */
	@EventHandler
	public void onInventoryDrag(@NotNull InventoryDragEvent event) {
		Inventory invType = event.getInventory();
		if (event.getWhoClicked() instanceof Player player) {
			if (invType.getHolder() instanceof InventoryMenu menu)
				menu.onDrag(event);
			else if (invType instanceof CraftingInventory crafting) {
				new BukkitRunnable() {
					@Override
					public void run() {
						crafting.setItem(0, ItemManager.convertItem(crafting.getItem(0)));
						ItemManager.convertAllItems(player.getInventory());
					}
				}.runTask(SMP_Expansion.getPlugin());
			}
		}
	}

	/**
	 * Event called when an inventory is opened for a player
	 * @param event InventoryOpenEvent
	 */
	@EventHandler
	public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
		Player player = (Player)event.getPlayer();
		Inventory inventory = event.getInventory();
		InventoryHolder holder = inventory.getHolder();
		Chunk chunk = null;
		if (holder instanceof BlockInventoryHolder block) {
			switch (block.getBlock().getType()) {
				case ENDER_CHEST:
				case LECTERN:
					break;
				default:
					chunk = block.getBlock().getChunk();
			}
		}
		else if (holder instanceof Entity entity && entity != player)
			chunk = entity.getChunk();
		if (chunk != null) {
			if (openContainersCheck(chunk, player)) {
				event.setCancelled(true);
				return;
			}
		}
		switch (inventory.getType()) {
			case SMITHING -> {
				event.setCancelled(true);
				player.openInventory(new SmithingTableMenu().getInventory());
			}
			case ENCHANTING -> {
				event.setCancelled(true);
				player.openInventory(new EnchantingTableMenu().getInventory());
			}
			case ANVIL -> {
				if (event.getView().title() instanceof TranslatableComponent) {
					Location block = inventory.getLocation();
					event.setCancelled(true);
					if (block != null)
						player.openInventory(new AnvilMenu(block.getBlock()).getInventory());
				}
			}
		}
	}

	/**
	 * Event called when a hopper or hopper minecart picks up an item
	 * @param event InventoryPickupitemEvent
	 */
	@EventHandler
	public void onInventoryPickupItem(@NotNull InventoryPickupItemEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Inventory inventory = event.getInventory();
				ItemStack[] items = inventory.getContents();
				for (int i = 0; i < items.length; i++)
					items[i] = ItemManager.convertItem(items[i]);
				inventory.setContents(items);
			}
		}.runTask(SMP_Expansion.getPlugin());
	}

	/**
	 * Event called when a container generates loot
	 * @param event LootGenerateEvent
	 */
	@EventHandler
	public void onLootGenerate(@NotNull LootGenerateEvent event) {
		List<ItemStack> loot = event.getLoot();
		for (ItemStack item: loot)
			loot.set(loot.indexOf(item), ItemManager.convertItem(item));
	}

	/**
	 * Event called when a player places a book on a lectern
	 * @param event PlayerInsertLecterBookEvent
	 */
	@EventHandler
	public void onPlayerInsertLecternBook(@NotNull PlayerInsertLecternBookEvent event) {
		event.setCancelled(openContainersCheck(event.getLectern().getChunk(), event.getPlayer()));
	}

	/**
	 * Event called when a player right clicks, potentially on a block
	 * @param event PlayerInteractEvent
	 */
	@EventHandler
	public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (block != null) {
			Chunk chunk = block.getChunk();
			switch (block.getType()) {
				case CAMPFIRE, CAULDRON, CHISELED_BOOKSHELF, COMPOSTER, SOUL_CAMPFIRE -> {
					if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
						event.setCancelled(openContainersCheck(chunk, player));
				}
				case FARMLAND -> {
					if (event.getAction() == Action.PHYSICAL)
						event.setCancelled(editWorldCheck(chunk, player, true));
				}
				default -> {
					switch (block.getBlockData()) {
						case Openable openable -> {
							if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
								event.setCancelled(!(openable instanceof Barrel) && useSwitchesCheck(chunk, player));
						}
						case Switch ignored -> {
							if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
								event.setCancelled(useSwitchesCheck(chunk, player));
						}
						default -> {
						}
					}
				}
			}
		}
	}

	/**
	 * Event called when a player interacts with an entity
	 * @param event PlayerInteractAtEntityEvent
	 */
	@EventHandler
	public void onPlayerInteractAtEntity(@NotNull PlayerInteractAtEntityEvent event) {
		event.setCancelled(editWorldCheck(event.getRightClicked().getChunk(), event.getPlayer(), event.getHand() != EquipmentSlot.OFF_HAND));
	}

	/**
	 * Event called when a player changes an item frame
	 * @param event PlayerItemFrameChangeEvent
	 */
	@EventHandler
	public void onPlayerItemFrameChange(@NotNull PlayerItemFrameChangeEvent event) {
		event.setCancelled(editWorldCheck(event.getItemFrame().getChunk(), event.getPlayer(), false));
	}

	/**
	 * Event called when a player tries to leash an entity
	 * @param event PlayerLeashEntityEvent
	 */
	@EventHandler
	public void onPlayerLeashEntity(@NotNull PlayerLeashEntityEvent event) {
		event.setCancelled(editWorldCheck(event.getEntity().getChunk(), event.getPlayer(), false));
	}

	/**
	 * Event called when a player joins the server
	 * @param event PlayerJoinEvent
	 */
	@EventHandler
	public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PersistentDataContainer playerData = player.getPersistentDataContainer();
		String forcedDifficulty = Config.getForcedDifficulty();
		if (!forcedDifficulty.equals("none"))
			playerData.set(difficulty, PersistentDataType.STRING, forcedDifficulty);
		int forcedPvP = Config.getForcedPvP();
		if (forcedPvP != 0)
			playerData.set(pvp, PersistentDataType.BOOLEAN, forcedPvP == 1);
		if (!Config.getAllowKeepInventory()) {
			playerData.set(keepInvArmor, PersistentDataType.BOOLEAN, false);
			playerData.set(keepInvHotbar, PersistentDataType.BOOLEAN, false);
			playerData.set(keepInvInventory, PersistentDataType.BOOLEAN, false);
			playerData.set(keepInvXP, PersistentDataType.BOOLEAN, false);
		}
		if (!playerData.getOrDefault(completedTutorial, PersistentDataType.BOOLEAN, false)) {
			ClickEvent<?> url = ClickEvent.openUrl("https://github.com/YellowStoneTorch/SMP_Expansion/wiki");
			ClickEvent<?> stop = ClickEvent.callback((audience) -> {
				if (audience instanceof Player player1) {
					player1.getPersistentDataContainer().set(completedTutorial, PersistentDataType.BOOLEAN, true);
					player1.sendMessage(text("This message will stop showing.", NamedTextColor.GREEN));
				}
			});
			player.sendMessage(text("This server is running the SMP Expansion plugin.", NamedTextColor.YELLOW, TextDecoration.BOLD));
			player.sendMessage(build(comp("Learn about the custom features ", NamedTextColor.WHITE), compClick(url, "here", NamedTextColor.YELLOW, TextDecoration.UNDERLINED), comp(".", NamedTextColor.WHITE)));
			player.sendMessage(text("Use the /menu command to open the main menu."));
			player.sendMessage(build(comp("To stop showing this message, click ", NamedTextColor.WHITE), compClick(stop, "here", NamedTextColor.YELLOW, TextDecoration.UNDERLINED), comp(".", NamedTextColor.WHITE)));
		}
	}

	/**
	 * Event called when a player moves
	 * @param event PlayerMoveEvent
	 */
	@EventHandler
	public void onPlayerMove(@NotNull PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PersistentDataContainer playerData = player.getPersistentDataContainer();
		if (playerData.getOrDefault(teleportCountdown, PersistentDataType.INTEGER, 0) > 0)
			playerData.set(teleportCountdown, PersistentDataType.INTEGER, 0);
		Chunk fromChunk = event.getFrom().getChunk();
		Chunk toChunk = event.getTo().getChunk();
		if (!fromChunk.equals(toChunk)) {
			OutpostCollection fromOutpost = ClaimManager.getOutpost(fromChunk);
			OutpostCollection toOutpost = ClaimManager.getOutpost(toChunk);
			if (fromOutpost != toOutpost) {
				if (toOutpost == null)
					player.sendActionBar(text("Unclaimed Territory", NamedTextColor.WHITE));
				else
					player.sendActionBar(build(toOutpost.getNameBuilder(), comp(" - "), TeamManager.getPlayerName(toOutpost.getOwner())));
			}
		}
	}

	/**
	 * Event called when a player leaves the server
	 * @param event PlayerQuitEvent
	 */
	@EventHandler
	public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (player.getOpenInventory().getTopInventory().getHolder() instanceof InventoryMenu)
			player.closeInventory(InventoryCloseEvent.Reason.DISCONNECT);
		PersistentDataContainer playerData = player.getPersistentDataContainer();
		if (playerData.getOrDefault(pvpCooldown, PersistentDataType.INTEGER, 0) > 0) {
			if (SMP_Expansion.isShuttingDown())
				playerData.set(pvpCooldown, PersistentDataType.INTEGER, 0);
			else
				player.setHealth(0);
		}
	}

	/**
	 * Event called when a player clicks on a recipe in the recipe book
	 * @param event PlayerRecipeBookClickEvent
	 */
	@EventHandler
	public void onPlayerRecipeBookClick(@NotNull PlayerRecipeBookClickEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Player player = event.getPlayer();
				Inventory inventory = player.getOpenInventory().getTopInventory();
				inventory.setItem(0, ItemManager.convertItem(inventory.getItem(0)));
			}
		}.runTask(SMP_Expansion.getPlugin());
	}

	/**
	 * Event called when a player takes a book from a lectern
	 * @param event PlayerTakeLecternBookEvent
	 */
	@EventHandler
	public void onPlayerTakeLecternBook(@NotNull PlayerTakeLecternBookEvent event) {
		event.setCancelled(openContainersCheck(event.getLectern().getChunk(), event.getPlayer()));
	}

	/**
	 * Event called when a player teleports
	 * @param event PlayerTeleportEvent
	 */
	@EventHandler
	public void onPlayerTeleport(@NotNull PlayerTeleportEvent event) {
		switch (event.getCause()) {
			case ENDER_PEARL, CONSUMABLE_EFFECT ->
					event.setCancelled(teleportCheck(event.getFrom().getChunk(), event.getTo().getChunk(), event.getPlayer()));
		}
	}

	/**
	 * Event called when a projectile is fired
	 * @param event ProjectileLaunchEvent
	 */
	@EventHandler
	public void onProjectileLaunch(@NotNull ProjectileLaunchEvent event) {
		Projectile projectile = event.getEntity();
		ProjectileSource entity = event.getEntity().getShooter();
		if (projectile instanceof Arrow arrow && entity instanceof Player player) {
			PersistentDataContainer dataP = arrow.getPersistentDataContainer();
			PlayerInventory pInventory = player.getInventory();
			ItemStack weapon;
			if (arrow.isShotFromCrossbow()) // Check which weapon arrow was fired from
			{
				if (pInventory.getItemInMainHand().getType() == Material.CROSSBOW)
					weapon = pInventory.getItemInMainHand();
				else
					weapon = pInventory.getItemInOffHand();
			}
			else {
				if (pInventory.getItemInMainHand().getType() == Material.BOW)
					weapon = pInventory.getItemInMainHand();
				else
					weapon = pInventory.getItemInOffHand();
			}
			ItemMeta weaponMeta = weapon.getItemMeta();
			PersistentDataContainer dataW = weaponMeta.getPersistentDataContainer();
			if (dataW.has(isCustom)) {
				double damage;
				if (weapon.getType() == Material.CROSSBOW) {
					if (weaponMeta.hasEnchant(Enchantment.POWER))
						damage = 9.0 + (0.5 * (weaponMeta.getEnchantLevel(Enchantment.POWER) + 1));
					else
						damage = 9.0;
				}
				else {
					if (weaponMeta.hasEnchant(Enchantment.POWER))
						damage = 2.0 + (0.5 * weaponMeta.getEnchantLevel(Enchantment.POWER));
					else
						damage = 2.0;
				}
				arrow.setDamage(damage);
				dataP.set(isCustom, PersistentDataType.BOOLEAN, true);
			}
		}
	}

	/**
	 * Event called when a vehicle collides with an entity
	 * @param event VehicleEntityCollisionEvent
	 */
	@EventHandler
	public void onVehicleEntityCollision(@NotNull VehicleEntityCollisionEvent event) {
		if (event.getEntity() instanceof Player player)
			event.setCancelled(editWorldCheck(event.getVehicle().getChunk(), player, false));
	}

	/**
	 * Event called when a vehicle is damaged by another entity
	 * @param event VehicleDamageEvent
	 */
	@EventHandler
	public void onVehicleDamage(@NotNull VehicleDamageEvent event) {
		if (event.getAttacker() instanceof Projectile projectile) {
			if (projectile.getShooter() instanceof Player player)
				event.setCancelled(editWorldCheck(event.getVehicle().getChunk(), player, true));
			else if (projectile.getShooter() instanceof Entity entity)
				event.setCancelled(editWorldCheck(event.getVehicle().getChunk(), entity));
		}
		else if (event.getAttacker() instanceof Player player)
			event.setCancelled(editWorldCheck(event.getVehicle().getChunk(), player, true));
		else
			event.setCancelled(editWorldCheck(event.getVehicle().getChunk(), event.getAttacker()));
	}

	/**
	 * Event called when an entity enters a vehicle
	 * @param event VehicleEnterEvent
	 */
	@EventHandler
	public void onVehicleEnter(@NotNull VehicleEnterEvent event) {
		if (event.getEntered() instanceof Player player)
			event.setCancelled(editWorldCheck(event.getVehicle().getChunk(), player, false));
	}

	/**
	 * Event called when a villager acquires a new trade
	 * @param event VillagerAcquireTradeEvent
	 */
	@EventHandler
	public void onVillagerAcquireTrade(@NotNull VillagerAcquireTradeEvent event) {
		AbstractVillager abstractVillager = event.getEntity();
		PersistentDataContainer villagerData = abstractVillager.getPersistentDataContainer();
		int type = villagerData.getOrDefault(villagerType, PersistentDataType.INTEGER, 0);
		if (type == 0)
			return;
		if (abstractVillager instanceof Villager villager) {
			if (villager.getProfession() == Villager.Profession.CARTOGRAPHER) {
				int level = villager.getVillagerLevel();
				MerchantRecipe tradeToAdd = event.getRecipe();
				switch (level) {
					case 2 -> {
						if ((villager.getRecipeCount() == 3 && tradeToAdd.getResult().getType() == Material.EMERALD)) {
							event.setCancelled(true);
							VillagerData.setTrades(villager, villager.getProfession(), villager.getVillagerType(), type, 2);
							villagerData.set(villagerHasTradeLevel, PersistentDataType.INTEGER, 2);
						}
						else if (villager.getRecipeCount() == 4 && villagerData.getOrDefault(villagerHasTradeLevel, PersistentDataType.INTEGER, 0) == 1) {
							event.setCancelled(true);
							villagerData.set(villagerHasTradeLevel, PersistentDataType.INTEGER, 2);
							List<MerchantRecipe> trades = new ArrayList<>(villager.getRecipes());
							MerchantRecipe prevTrade = trades.get(3);
							MerchantRecipe newTrade = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 16, true, 10, 0.05f, false);
							newTrade.addIngredient(ItemStack.of(Material.GLASS_PANE, 8));
							trades.set(3, newTrade);
							trades.add(4, prevTrade);
							villager.setRecipes(trades);
						}
					}
					case 3 -> {
						if ((villager.getRecipeCount() == 5 && tradeToAdd.getResult().getType() == Material.EMERALD)) {
							event.setCancelled(true);
							VillagerData.setTrades(villager, villager.getProfession(), villager.getVillagerType(), type, 3);
							villagerData.set(villagerHasTradeLevel, PersistentDataType.INTEGER, 3);
						}
						else if (villager.getRecipeCount() == 6 && villagerData.getOrDefault(villagerHasTradeLevel, PersistentDataType.INTEGER, 0) == 2) {
							event.setCancelled(true);
							villagerData.set(villagerHasTradeLevel, PersistentDataType.INTEGER, 2);
							List<MerchantRecipe> trades = new ArrayList<>(villager.getRecipes());
							MerchantRecipe prevTrade = trades.get(5);
							MerchantRecipe newTrade = new MerchantRecipe(ItemStack.of(Material.EMERALD, 1), 0, 12, true, 10, 0.05f, false);
							newTrade.addIngredient(ItemStack.of(Material.COMPASS, 1));
							trades.set(5, newTrade);
							trades.add(6, prevTrade);
							villager.setRecipes(trades);

						}
					}
					case 5 -> {
						if ((villager.getRecipeCount() == 9 && tradeToAdd.getResult().getType() == Material.EMERALD)) {
							event.setCancelled(true);
							VillagerData.setTrades(villager, villager.getProfession(), villager.getVillagerType(), type, 5);
							villagerData.set(villagerHasTradeLevel, PersistentDataType.INTEGER, 5);
						}
						else if (villager.getRecipeCount() == 10 && villagerData.getOrDefault(villagerHasTradeLevel, PersistentDataType.INTEGER, 0) == 4) {
							event.setCancelled(true);
							villagerData.set(villagerHasTradeLevel, PersistentDataType.INTEGER, 2);
							List<MerchantRecipe> trades = new ArrayList<>(villager.getRecipes());
							MerchantRecipe prevTrade = trades.get(9);
							MerchantRecipe newTrade = new MerchantRecipe(ItemStack.of(Material.GLOBE_BANNER_PATTERN, 1), 0, 12, true, 30, 0.05f, false);
							newTrade.addIngredient(ItemStack.of(Material.EMERALD, 8));
							trades.set(9, newTrade);
							trades.add(10, prevTrade);
							villager.setRecipes(trades);
						}
					}
					default -> {
						event.setCancelled(true);
						int villagerLevel = villager.getVillagerLevel();
						int currentVillagerTradeLevel = villagerData.getOrDefault(villagerHasTradeLevel, PersistentDataType.INTEGER, 0);
						if (currentVillagerTradeLevel != villagerLevel) {
							VillagerData.setTrades(villager, villager.getProfession(), villager.getVillagerType(), type, villagerLevel);
							villagerData.set(villagerHasTradeLevel, PersistentDataType.INTEGER, villagerLevel);
						}
					}
				}
			}
			else {
				event.setCancelled(true);
				int villagerLevel = villager.getVillagerLevel();
				int currentVillagerTradeLevel = villagerData.getOrDefault(villagerHasTradeLevel, PersistentDataType.INTEGER, 0);
				if (currentVillagerTradeLevel != villagerLevel) {
					VillagerData.setTrades(villager, villager.getProfession(), villager.getVillagerType(), type, villagerLevel);
					villagerData.set(villagerHasTradeLevel, PersistentDataType.INTEGER, villagerLevel);
				}
			}
		}
		else if (abstractVillager instanceof WanderingTrader) {
			MerchantRecipe recipe = event.getRecipe();
			ItemStack result = recipe.getResult();
			if (result.getType() == Material.IRON_PICKAXE) {
				MerchantRecipe newRecipe = new MerchantRecipe(ItemManager.convertItem(result), 0, recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier(), recipe.getDemand(), recipe.getSpecialPrice(), recipe.shouldIgnoreDiscounts());
				for (ItemStack ingredient: recipe.getIngredients())
					newRecipe.addIngredient(ingredient);
				event.setRecipe(newRecipe);
			}
		}
	}

	/**
	 * Event called when a villager changes its profession
	 * @param event VillagerCareerChangeEvent
	 */
	@EventHandler
	public void onVillagerCareerChange(@NotNull VillagerCareerChangeEvent event) {
		Villager villager = event.getEntity();
		PersistentDataContainer villagerData = villager.getPersistentDataContainer();
		Villager.Profession futureProfession = event.getProfession();
		if (futureProfession == NONE) {
			villagerData.remove(villagerType);
			villagerData.remove(villagerHasTradeLevel);
		}
		else if (futureProfession != NITWIT) {
			villagerData.set(villagerType, PersistentDataType.INTEGER, VillagerData.generateVillagerType(futureProfession, villager.getVillagerType()));
			villagerData.set(villagerHasTradeLevel, PersistentDataType.INTEGER, 0);
		}
	}

	/**
	 * Checks whether this player is allowed to teleport using ender pearls or chorus fruit
	 * @param fromChunk chunk where the player tried to teleport
	 * @param toChunk chunk where the player tried to teleport to
	 * @param player player who tried to teleport
	 * @return whether player is blocked from doing this
	 */
	private boolean teleportCheck(Chunk fromChunk, Chunk toChunk, Player player) {
		if (ClaimManager.getOutpost(fromChunk) instanceof OutpostCollection outpost && !outpost.getOwner().equals(player) && player.getGameMode() != GameMode.CREATIVE) {
			Permission permission = outpost.permsTeleport();
			if (permission == Permission.NONE || (permission == Permission.TEAMMATES && TeamManager.getPlayerTeam(player) != TeamManager.getPlayerTeam(outpost.getOwner()))) {
				player.sendMessage(text("You cannot do that in this claim.", NamedTextColor.RED));
				return true;
			}
		}
		if (ClaimManager.getOutpost(toChunk) instanceof OutpostCollection outpost && !outpost.getOwner().equals(player) && player.getGameMode() != GameMode.CREATIVE) {
			Permission permission = outpost.permsTeleport();
			if (permission == Permission.NONE || (permission == Permission.TEAMMATES && TeamManager.getPlayerTeam(player) != TeamManager.getPlayerTeam(outpost.getOwner()))) {
				player.sendMessage(text("You cannot do that in this claim.", NamedTextColor.RED));
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether this player is allowed to use switches in this chunk
	 * @param chunk chunk where the player tried to use a switch
	 * @param player player who tried to use a switch
	 * @return whether player is blocked from doing this
	 */
	private boolean useSwitchesCheck(Chunk chunk, Player player) {
		if (ClaimManager.getOutpost(chunk) instanceof OutpostCollection outpost && !outpost.getOwner().equals(player) && player.getGameMode() != GameMode.CREATIVE) {
			Permission permission = outpost.permsUseSwitches();
			if (permission == Permission.NONE || (permission == Permission.TEAMMATES && TeamManager.getPlayerTeam(player) != TeamManager.getPlayerTeam(outpost.getOwner()))) {
				player.sendMessage(text("You cannot do that in this claim.", NamedTextColor.RED));
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether this player is allowed to open containers in this chunk
	 * @param chunk chunk where the player tried to open a container
	 * @param player player who tried to open a container
	 * @return whether player is blocked from doing this
	 */
	private boolean openContainersCheck(Chunk chunk, Player player) {
		if (ClaimManager.getOutpost(chunk) instanceof OutpostCollection outpost && !outpost.getOwner().equals(player) && player.getGameMode() != GameMode.CREATIVE) {
			Permission permission = outpost.permsOpenContainers();
			if (permission == Permission.NONE || (permission == Permission.TEAMMATES && TeamManager.getPlayerTeam(player) != TeamManager.getPlayerTeam(outpost.getOwner()))) {
				player.sendMessage(text("You cannot do that in this claim.", NamedTextColor.RED));
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether this player is allowed to edit the world in this chunk
	 * @param chunk chunk where the player tried to edit the world
	 * @param player player who tried to edit the world
	 * @param broadcast whether to send message to player or not
	 * @return whether player is blocked from doing this
	 */
	private boolean editWorldCheck(Chunk chunk, Player player, boolean broadcast) {
		if (ClaimManager.getOutpost(chunk) instanceof OutpostCollection outpost && !outpost.getOwner().equals(player) && player.getGameMode() != GameMode.CREATIVE) {
			Permission permission = outpost.permsEditWorld();
			if (permission == Permission.NONE || (permission == Permission.TEAMMATES && TeamManager.getPlayerTeam(player) != TeamManager.getPlayerTeam(outpost.getOwner()))) {
				if (broadcast)
					player.sendMessage(text("You cannot do that in this claim.", NamedTextColor.RED));
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether this entity is allowed to edit the world in this chunk
	 * @param chunk chunk where the entity tried to edit the world
	 * @param entity entity who tried to edit the world
	 * @return whether entity is blocked from doing this
	 */
	private boolean editWorldCheck(Chunk chunk, Entity entity) {
		return entity instanceof Enemy && ClaimManager.getOutpost(chunk) != null;
	}


}
package io.github.YellowStoneTorch.SMP_Expansion.Commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.YellowStoneTorch.SMP_Expansion.Claims.ClaimCollection;
import io.github.YellowStoneTorch.SMP_Expansion.Claims.ClaimManager;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.Enchantments.EnchantmentType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.pvpCooldown;
import static net.kyori.adventure.text.Component.text;

//@formatter:off
/**
 * Command for testing purposes only
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Test {
	/**
	 * Get command logic to register command
	 * @return command logic
	 */
	public static LiteralCommandNode<CommandSourceStack> createCommand() {
		return Commands.literal("test")
				.requires(source -> source.getSender().isOp())
				.then(Commands.literal("xp")
						.then(Commands.argument("amount", IntegerArgumentType.integer())
								.executes(context -> {
									if (context.getSource().getSender() instanceof Player player) {
										int xp = context.getArgument("amount", Integer.class);
										PersistentDataContainer data = player.getPersistentDataContainer();
										for (EnchantmentType type: EnchantmentType.values())
											data.set(type.key, PersistentDataType.INTEGER, xp);
										data.set(pvpCooldown, PersistentDataType.INTEGER, 0);
									}
									return 1;
								})))
				.then(Commands.literal("claims")
						.then(Commands.argument("amount", IntegerArgumentType.integer())
								.then(Commands.literal("outposts")
										.executes(context -> {
											if (context.getSource().getSender() instanceof Player player) {
												int amount = context.getArgument("amount", Integer.class);
												ClaimCollection collection = ClaimManager.getPlayerClaims(player);
												assert collection != null;
												collection.setOutposts(amount);
											}
											return 1;
										}))
								.then(Commands.literal("claims")
										.executes(context -> {
											if (context.getSource().getSender() instanceof Player player) {
												int amount = context.getArgument("amount", Integer.class);
												ClaimCollection collection = ClaimManager.getPlayerClaims(player);
												assert collection != null;
												collection.setClaims(amount);
											}
											return 1;
										}))))
				.then(Commands.literal("durability")
						.executes(context -> {
							if (context.getSource().getSender() instanceof Player player) {
								ItemStack item = player.getInventory().getItemInMainHand();
								Bukkit.getServer().broadcast(text(((Damageable)item.getItemMeta()).hasMaxDamage()));
								Bukkit.getServer().broadcast(text(item.getItemMeta().toString()));
							}
							return 1;
						}))
				.build();
	}
}

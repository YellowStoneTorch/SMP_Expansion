package io.github.YellowStoneTorch.SMP_Expansion.Commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.YellowStoneTorch.SMP_Expansion.Equipment.ItemManager;
import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import static io.github.YellowStoneTorch.SMP_Expansion.CustomData.money;
import static net.kyori.adventure.text.Component.text;

//@formatter:off
/**
 * Command for withdrawing banknotes from the player's account
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Withdraw {
	/**
	 * Get command logic to register command
	 * @return command logic
	 */
	public static LiteralCommandNode<CommandSourceStack> createCommand() {
		return Commands.literal("withdraw")
				.then(Commands.argument("amount", IntegerArgumentType.integer(1))
						.executes(context -> {
							if (context.getSource().getSender() instanceof Player player) {
								int amount = context.getArgument("amount", Integer.class);
								PersistentDataContainer playerData = player.getPersistentDataContainer();
								int balance = playerData.getOrDefault(money, PersistentDataType.INTEGER, 0);
								if (balance >= amount) {
									playerData.set(money, PersistentDataType.INTEGER, balance - amount);
									player.give(ItemManager.getBanknote(amount));
									new BukkitRunnable()
									{
										@Override
										public void run()
										{
											player.updateInventory();
										}
									}.runTask(SMP_Expansion.getPlugin());
								}
								else
									player.sendMessage(text("Insufficient Balance", NamedTextColor.RED));
							}
							return 1;
						}))
				.build();
	}
}

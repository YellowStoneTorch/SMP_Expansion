package io.github.YellowStoneTorch.SMP_Expansion.Commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.YellowStoneTorch.SMP_Expansion.InventoryMenus.MainMenu;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

//@formatter:off
/**
 * Command for opening the main menu
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class Menu {
	/**
	 * Get command logic to register command
	 * @return command logic
	 */
	public static LiteralCommandNode<CommandSourceStack> createCommand()
	{
		return Commands.literal("menu")
				.executes(context -> {
					if (context.getSource().getSender() instanceof Player player)
						player.openInventory(new MainMenu().getInventory());
					return 1;
				})
				.build();
	}
}

package io.github.YellowStoneTorch.SMP_Expansion.Commands;

import io.github.YellowStoneTorch.SMP_Expansion.SMP_Expansion;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jetbrains.annotations.NotNull;

/**
 * Registers all custom commands for server
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
public class CommandManager {

	/**
	 * Initializes and registers each command
	 * @param plugin the plugin
	 */
	public static void registerCommands(@NotNull SMP_Expansion plugin) {
		plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
			Commands registrar = commands.registrar();
			registrar.register(Menu.createCommand(), "Opens the main menu");
			registrar.register(Withdraw.createCommand(), "Withdraws banknotes from account");
			registrar.register(Home.createCommand(), "Teleports player to their spawn point");
			registrar.register(Team.createCommand(), "Manages player teams");
			registrar.register(Claim.createCommand(), "Managers player claims");
			//registrar.register(Test.createCommand(), "Testing purposes only");
		});
	}
}

package io.github.YellowStoneTorch.SMP_Expansion;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

/**
 * Bootstrapper for plugin, to load data pack
 * @author YellowStoneTorch
 * @version 0.1.0-ALPHA
 */
@SuppressWarnings({"UnstableApiUsage", "unused"})
public class Bootstrapper implements PluginBootstrap {

	@Override
	@ApiStatus.Experimental
	public void bootstrap(@NotNull BootstrapContext context) {
		context.getLifecycleManager().registerEventHandler(LifecycleEvents.DATAPACK_DISCOVERY.newHandler(event -> {
			try {
				//noinspection DataFlowIssue
				URI uri = this.getClass().getResource("/SMP_Expansion_Datapack").toURI();
				event.registrar().discoverPack(uri, "provided");
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}

		}));
	}
}

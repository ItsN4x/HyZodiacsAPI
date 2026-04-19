package com.hyzodiac.api.impl.fabric;

import com.hyzodiac.api.HyZodiacAPI;
import com.hyzodiac.api.events.LifecycleEvents;
import com.hyzodiac.api.util.HyZodiacLog;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

/** Main entrypoint on Fabric. Bridges Fabric API events to {@link LifecycleEvents}. */
public final class FabricCommonBootstrap implements ModInitializer {

    @Override
    public void onInitialize() {
        HyZodiacLog.info(HyZodiacAPI.MOD_NAME + " v" + HyZodiacAPI.VERSION + " loading on Fabric 1.21+");

        // Trigger platform discovery early so misconfiguration surfaces during init.
        HyZodiacAPI.platform();

        ServerTickEvents.END_SERVER_TICK.register(server -> LifecycleEvents.SERVER_TICK_END.invoker().onTick());
        ServerLifecycleEvents.SERVER_STARTED.register(server -> LifecycleEvents.SERVER_STARTED.invoker().onStart());
    }
}

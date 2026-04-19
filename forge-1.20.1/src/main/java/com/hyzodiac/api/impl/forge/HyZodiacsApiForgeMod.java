package com.hyzodiac.api.impl.forge;

import com.hyzodiac.api.HyZodiacAPI;
import com.hyzodiac.api.events.LifecycleEvents;
import com.hyzodiac.api.hud.HudOverlays;
import com.hyzodiac.api.hud.HudRenderContext;
import com.hyzodiac.api.util.HyZodiacLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/** Forge mod entrypoint. */
@Mod(HyZodiacAPI.MOD_ID)
public final class HyZodiacsApiForgeMod {

    public HyZodiacsApiForgeMod() {
        HyZodiacLog.info(HyZodiacAPI.MOD_NAME + " v" + HyZodiacAPI.VERSION + " loading on Forge 1.20.1");
        HyZodiacAPI.platform();

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onClientSetup);
        modBus.addListener(this::onRegisterOverlays);

        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(this::onServerTick);
        MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
    }

    private void onServerStarted(ServerStartedEvent e) {
        LifecycleEvents.SERVER_STARTED.invoker().onStart();
    }

    private void onServerTick(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            LifecycleEvents.SERVER_TICK_END.invoker().onTick();
        }
    }

    private void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            LifecycleEvents.CLIENT_TICK_END.invoker().onTick();
        }
    }

    private void onClientSetup(FMLClientSetupEvent e) {
        LifecycleEvents.CLIENT_STARTED.invoker().onStart();
    }

    private void onRegisterOverlays(RegisterGuiOverlaysEvent e) {
        e.registerAboveAll("hud_overlays", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            HudOverlays.RENDER.invoker().render(new Context(guiGraphics, screenWidth, screenHeight, partialTick));
        });
    }

    private record Context(GuiGraphics gui, int w, int h, float delta) implements HudRenderContext {
        @Override public Object graphics() { return gui; }
        @Override public int screenWidth() { return w; }
        @Override public int screenHeight() { return h; }
        @Override public float tickDelta() { return delta; }
    }
}

package com.hyzodiac.api.impl.fabric;

import com.hyzodiac.api.events.LifecycleEvents;
import com.hyzodiac.api.hud.HudOverlays;
import com.hyzodiac.api.hud.HudRenderContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/** Client entrypoint on Fabric 1.20.1. Uses the older HudRenderCallback API. */
@Environment(EnvType.CLIENT)
public final class FabricClientBootstrap implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> LifecycleEvents.CLIENT_TICK_END.invoker().onTick());

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            int width = mc.getWindow().getScaledWidth();
            int height = mc.getWindow().getScaledHeight();
            HudOverlays.RENDER.invoker().render(new Context(drawContext, width, height, tickDelta));
        });

        ClientTickEvents.END_CLIENT_TICK.register(new ClientTickEvents.EndTick() {
            private boolean fired = false;

            @Override
            public void onEndTick(MinecraftClient client) {
                if (!fired && client.world != null) {
                    fired = true;
                    LifecycleEvents.CLIENT_STARTED.invoker().onStart();
                }
            }
        });
    }

    private record Context(DrawContext ctx, int w, int h, float delta) implements HudRenderContext {
        @Override public Object graphics() { return ctx; }
        @Override public int screenWidth() { return w; }
        @Override public int screenHeight() { return h; }
        @Override public float tickDelta() { return delta; }
    }
}

package com.hyzodiac.api.hud;

/**
 * A draw callback consumed by each platform's HUD integration. Drawing itself uses the
 * platform's native graphics context (e.g. {@code DrawContext} on Fabric or
 * {@code GuiGraphics} on Forge); implementations forward that context to the callback
 * via {@link HudRenderContext}.
 */
@FunctionalInterface
public interface HudOverlay {
    void render(HudRenderContext ctx);
}

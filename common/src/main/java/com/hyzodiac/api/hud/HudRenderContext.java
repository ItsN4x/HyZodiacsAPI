package com.hyzodiac.api.hud;

/**
 * Platform-neutral view of a HUD render tick. The raw graphics handle is exposed as
 * {@code Object} because its concrete type differs between Minecraft versions
 * (e.g. {@code DrawContext} 1.20.1 vs 1.21). Consumers that need to draw typically
 * cast it in their platform-specific code, or use the drawing helpers shipped by
 * their loader module.
 */
public interface HudRenderContext {

    Object graphics();

    int screenWidth();

    int screenHeight();

    float tickDelta();
}

package com.hyzodiac.api.hud;

import com.hyzodiac.api.events.Event;

/**
 * Registry of HUD overlays that the active platform should render every frame after
 * vanilla HUD elements.
 */
public final class HudOverlays {

    public static final Event<HudOverlay> RENDER = Event.create(
            HudOverlay.class,
            listeners -> ctx -> {
                for (HudOverlay l : listeners) l.render(ctx);
            });

    private HudOverlays() {
    }
}

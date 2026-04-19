package com.hyzodiac.api.events;

import java.util.UUID;

/**
 * Platform-neutral combat events. Payloads intentionally use {@link UUID} and primitive
 * types instead of Minecraft classes so that mods targeting different Minecraft versions
 * can share subscriber code through the common jar.
 *
 * <p>Fired by the platform impls when the underlying Fabric/Forge hook is triggered.</p>
 */
public final class CombatEvents {

    @FunctionalInterface
    public interface PlayerAttacked {
        EventResult onAttack(AttackContext ctx);
    }

    @FunctionalInterface
    public interface PlayerKilled {
        void onKill(UUID attacker, UUID victim);
    }

    public record AttackContext(UUID attacker, UUID victim, float damage, boolean isCritical) {
    }

    public static final Event<PlayerAttacked> PLAYER_ATTACKED = Event.create(
            PlayerAttacked.class,
            listeners -> ctx -> {
                for (PlayerAttacked l : listeners) {
                    EventResult r = l.onAttack(ctx);
                    if (r.interruptsFurtherEvaluation()) return r;
                }
                return EventResult.PASS;
            });

    public static final Event<PlayerKilled> PLAYER_KILLED = Event.create(
            PlayerKilled.class,
            listeners -> (attacker, victim) -> {
                for (PlayerKilled l : listeners) {
                    l.onKill(attacker, victim);
                }
            });

    private CombatEvents() {
    }
}

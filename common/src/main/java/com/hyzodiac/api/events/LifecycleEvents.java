package com.hyzodiac.api.events;

/**
 * Client- and server-side lifecycle hooks that fire once per logical tick or when the
 * corresponding Minecraft loop transitions. Platform impls are responsible for wiring
 * these through Fabric API tick callbacks / Forge's TickEvent.
 */
public final class LifecycleEvents {

    @FunctionalInterface
    public interface ClientTick {
        void onTick();
    }

    @FunctionalInterface
    public interface ServerTick {
        void onTick();
    }

    @FunctionalInterface
    public interface ClientStarted {
        void onStart();
    }

    @FunctionalInterface
    public interface ServerStarted {
        void onStart();
    }

    public static final Event<ClientTick> CLIENT_TICK_END = Event.create(
            ClientTick.class,
            listeners -> () -> {
                for (ClientTick l : listeners) l.onTick();
            });

    public static final Event<ServerTick> SERVER_TICK_END = Event.create(
            ServerTick.class,
            listeners -> () -> {
                for (ServerTick l : listeners) l.onTick();
            });

    public static final Event<ClientStarted> CLIENT_STARTED = Event.create(
            ClientStarted.class,
            listeners -> () -> {
                for (ClientStarted l : listeners) l.onStart();
            });

    public static final Event<ServerStarted> SERVER_STARTED = Event.create(
            ServerStarted.class,
            listeners -> () -> {
                for (ServerStarted l : listeners) l.onStart();
            });

    private LifecycleEvents() {
    }
}

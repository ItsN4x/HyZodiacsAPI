package com.hyzodiac.api;

import com.hyzodiac.api.platform.Platform;
import com.hyzodiac.api.platform.Services;
import com.hyzodiac.api.util.HyZodiacLog;

/**
 * Entry point for HyZodiac's API. Provides access to platform services and shared
 * state used by hyZodiac's mods (Kexosyn) and partner mods (PvpSkill).
 *
 * <p>Third-party mods should depend on the `common` module at compile time and
 * rely on the loader-specific jar at runtime.</p>
 */
public final class HyZodiacAPI {

    public static final String MOD_ID = "hyzodiacs_api";
    public static final String MOD_NAME = "HyZodiac's API";
    public static final String VERSION = "1.0.0";

    private static volatile Platform platform;

    private HyZodiacAPI() {
    }

    /**
     * Returns the active platform implementation (Fabric 1.21, Fabric 1.20.1, or Forge 1.20.1).
     * The platform is discovered via {@link java.util.ServiceLoader} the first time this method
     * is called.
     */
    public static Platform platform() {
        Platform p = platform;
        if (p == null) {
            synchronized (HyZodiacAPI.class) {
                p = platform;
                if (p == null) {
                    p = Services.load(Platform.class);
                    platform = p;
                    HyZodiacLog.info("Initialized on " + p.loaderName()
                            + " for Minecraft " + p.minecraftVersion()
                            + " (client=" + p.isClient() + ")");
                }
            }
        }
        return p;
    }

    /**
     * Test-only hook that lets unit tests inject a fake platform.
     */
    public static void setPlatformForTesting(Platform p) {
        platform = p;
    }
}

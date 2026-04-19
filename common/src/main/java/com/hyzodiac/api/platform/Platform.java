package com.hyzodiac.api.platform;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Abstracts loader- and version-specific behavior so code in the `common` module
 * can stay free of Minecraft types. Implementations are registered via
 * {@link java.util.ServiceLoader} in each platform jar.
 */
public interface Platform {

    enum Loader { FABRIC, FORGE, NEOFORGE }

    Loader loader();

    default String loaderName() {
        return loader().name().toLowerCase();
    }

    String minecraftVersion();

    boolean isClient();

    boolean isDevelopmentEnvironment();

    Path configDir();

    Optional<String> modVersion(String modId);

    boolean isModLoaded(String modId);
}

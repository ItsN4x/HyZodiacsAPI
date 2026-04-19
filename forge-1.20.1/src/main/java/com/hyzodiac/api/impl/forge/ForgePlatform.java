package com.hyzodiac.api.impl.forge;

import com.hyzodiac.api.platform.Platform;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.Optional;

/** Service provider for {@link Platform} on Forge 1.20.1. */
public final class ForgePlatform implements Platform {

    @Override
    public Loader loader() {
        return Loader.FORGE;
    }

    @Override
    public String minecraftVersion() {
        return ModList.get() == null ? "unknown"
                : ModList.get().getModContainerById("minecraft")
                .map(c -> c.getModInfo().getVersion().toString())
                .orElse("unknown");
    }

    @Override
    public boolean isClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.production;
    }

    @Override
    public Path configDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Optional<String> modVersion(String modId) {
        if (ModList.get() == null) return Optional.empty();
        return ModList.get().getModContainerById(modId)
                .map(c -> c.getModInfo().getVersion().toString());
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get() != null && ModList.get().isLoaded(modId);
    }
}

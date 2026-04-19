package com.hyzodiac.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.hyzodiac.api.HyZodiacApi;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Base class for HyZodiac mod configs. Subclasses declare public fields with default values;
 * call {@link #load(Class, String)} on startup and {@link #save()} after mutating fields.
 *
 * <p>Override {@link #fillDefaults()} to backfill nullable fields after deserialisation
 * (handy when adding new settings between releases) and {@link #clamp()} to enforce ranges.
 *
 * <p>Storage path is {@code config/<fileName>.json}. Errors during load fall back to defaults
 * with a warning logged via the API logger; errors during save are logged but not thrown so
 * a corrupted save never crashes the game.
 */
public abstract class AbstractJsonConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	transient String fileName;

	/** Override to migrate / backfill defaults for fields added in newer versions. */
	public void fillDefaults() {}

	/** Override to enforce field ranges after load and before save. */
	public void clamp() {}

	/** Resolved on-disk path; only valid after a successful {@link #load(Class, String)}. */
	public final Path path() {
		return FabricLoader.getInstance().getConfigDir().resolve(fileName + ".json");
	}

	public final void save() {
		clamp();
		try {
			Path p = path();
			Files.createDirectories(p.getParent());
			Files.writeString(p, GSON.toJson(this));
		} catch (IOException e) {
			HyZodiacApi.LOGGER.warn("Failed to save config {}: {}", fileName, e.getMessage());
		}
	}

	/**
	 * Loads the config of {@code type} from {@code config/<fileName>.json}, or writes out
	 * a fresh default if the file is missing or corrupt.
	 */
	public static <T extends AbstractJsonConfig> T load(Class<T> type, String fileName) {
		Path p = FabricLoader.getInstance().getConfigDir().resolve(fileName + ".json");
		T cfg;
		if (!Files.exists(p)) {
			cfg = newInstance(type);
		} else {
			try {
				cfg = GSON.fromJson(Files.readString(p), type);
				if (cfg == null) cfg = newInstance(type);
			} catch (IOException | JsonSyntaxException e) {
				HyZodiacApi.LOGGER.warn("Failed to load {}, using defaults: {}", p, e.getMessage());
				cfg = newInstance(type);
			}
		}
		cfg.fileName = fileName;
		cfg.fillDefaults();
		cfg.clamp();
		cfg.save();
		return cfg;
	}

	private static <T> T newInstance(Class<T> type) {
		try {
			return type.getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Config class " + type.getName() + " needs a public no-arg ctor", e);
		}
	}
}

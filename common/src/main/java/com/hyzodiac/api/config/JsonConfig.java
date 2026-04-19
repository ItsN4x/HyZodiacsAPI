package com.hyzodiac.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyzodiac.api.HyZodiacAPI;
import com.hyzodiac.api.util.HyZodiacLog;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * Tiny JSON-backed config helper. Not a full config framework — by design — just enough
 * to save and load POJOs without each consumer mod re-implementing the same
 * {@link Gson} boilerplate.
 *
 * <p>Files live under {@link com.hyzodiac.api.platform.Platform#configDir()} by default.</p>
 *
 * @param <T> POJO type containing only {@code public} fields with default values
 */
public final class JsonConfig<T> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    private final Class<T> type;
    private final Path file;
    private final Supplier<T> defaults;
    private T current;

    private JsonConfig(Class<T> type, Path file, Supplier<T> defaults) {
        this.type = type;
        this.file = file;
        this.defaults = defaults;
    }

    public static <T> JsonConfig<T> of(Class<T> type, String fileName, Supplier<T> defaults) {
        Path dir = HyZodiacAPI.platform().configDir();
        return new JsonConfig<>(type, dir.resolve(fileName), defaults);
    }

    public T load() {
        if (current != null) return current;
        try {
            if (Files.exists(file)) {
                try (Reader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    T parsed = GSON.fromJson(r, type);
                    current = parsed != null ? parsed : defaults.get();
                }
            } else {
                current = defaults.get();
                save();
            }
        } catch (IOException | RuntimeException e) {
            HyZodiacLog.warn("Failed to load config " + file + "; using defaults", e);
            current = defaults.get();
        }
        return current;
    }

    public void save() {
        if (current == null) current = defaults.get();
        try {
            Files.createDirectories(file.getParent());
            try (Writer w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                GSON.toJson(current, type, w);
            }
        } catch (IOException e) {
            HyZodiacLog.warn("Failed to save config " + file, e);
        }
    }

    public T get() {
        return load();
    }

    public void set(T value) {
        this.current = value;
        save();
    }
}

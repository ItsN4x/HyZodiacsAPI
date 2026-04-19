package com.hyzodiac.api.branding;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Process-wide registry of mod {@link Branding} entries. Thread-safe; re-registering the
 * same mod id replaces the previous entry without warning.
 */
public final class BrandingRegistry {
	private static final Map<String, Branding> BY_ID = new ConcurrentHashMap<>();

	private BrandingRegistry() {}

	public static void register(Branding branding) {
		BY_ID.put(branding.modId(), branding);
	}

	public static Optional<Branding> get(String modId) {
		return Optional.ofNullable(BY_ID.get(modId));
	}

	public static Collection<Branding> all() {
		return BY_ID.values();
	}
}

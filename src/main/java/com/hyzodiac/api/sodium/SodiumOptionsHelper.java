package com.hyzodiac.api.sodium;

import com.hyzodiac.api.HyZodiacApi;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Helper for adding custom tabs to the Sodium options screen via {@code sodium-options-api}.
 *
 * <p>This API does <b>not</b> link against {@code sodium-options-api} at compile time — it
 * detects the integration mod at runtime and uses reflection to register pages, so HyZodiac
 * mods continue to load on Sodium-less installs without ClassNotFound crashes.
 *
 * <p>Pending registrations are queued until the API discovers the integration class. If
 * {@code sodium-options-api} is never loaded, registrations are silently dropped (and a
 * single {@code DEBUG} log line is emitted per mod).
 */
public final class SodiumOptionsHelper {
	private static final List<PageRegistration> QUEUE = new ArrayList<>();
	private static volatile boolean drained;

	private SodiumOptionsHelper() {}

	/**
	 * Register a Sodium options page. The {@code pageFactory} should produce a value of the
	 * type expected by your installed {@code sodium-options-api} version (usually a
	 * {@code OptionPage}). The factory is only invoked if the integration mod is present.
	 *
	 * @param ownerModId  Fabric mod id of the registering mod (used for logging)
	 * @param pageFactory factory that produces the page object lazily
	 */
	public static synchronized void registerPage(String ownerModId, Supplier<Object> pageFactory) {
		QUEUE.add(new PageRegistration(ownerModId, pageFactory));
		drain();
	}

	/**
	 * Force-drain the queue. Called automatically by {@link #registerPage}; consumer mods
	 * usually don't need to invoke it themselves. Returns the number of pages successfully
	 * registered (0 if the integration mod isn't loaded).
	 */
	public static synchronized int drain() {
		if (drained || QUEUE.isEmpty()) return 0;
		if (!FabricLoader.getInstance().isModLoaded("sodium-options-api")) {
			HyZodiacApi.LOGGER.debug("sodium-options-api not present — queued {} page(s) will be dropped", QUEUE.size());
			QUEUE.clear();
			drained = true;
			return 0;
		}

		int ok = 0;
		for (PageRegistration r : QUEUE) {
			try {
				Object page = r.pageFactory.get();
				registerViaReflection(page);
				HyZodiacApi.LOGGER.info("Registered Sodium options page for {}", r.ownerModId);
				ok++;
			} catch (Throwable t) {
				HyZodiacApi.LOGGER.warn("Failed to register Sodium page for {}: {}", r.ownerModId, t.toString());
			}
		}
		QUEUE.clear();
		drained = true;
		return ok;
	}

	private static void registerViaReflection(Object page) throws ReflectiveOperationException {
		// sodium-options-api 1.x exposes a static SodiumOptionsAPI.registerOptionPage(OptionPage)
		Class<?> apiClass = Class.forName("me.flashyreese.mods.sodiumoptionsapi.SodiumOptionsAPI");
		apiClass.getMethod("registerOptionPage", page.getClass().getSuperclass() != null ? page.getClass().getSuperclass() : page.getClass())
			.invoke(null, page);
	}

	private record PageRegistration(String ownerModId, Supplier<Object> pageFactory) {}
}

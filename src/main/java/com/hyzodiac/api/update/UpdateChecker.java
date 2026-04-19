package com.hyzodiac.api.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hyzodiac.api.HyZodiacApi;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Async update checker that pings the GitHub Releases API for a repo and reports whether the
 * installed Fabric mod is older than the latest published tag.
 *
 * <p>Uses simple {@link String#compareTo(String)} on the normalised tag (leading "v" stripped),
 * which works for the {@code MAJOR.MINOR.PATCH} scheme HyZodiac mods follow but is not a real
 * semver comparator. Pre-release tags like {@code 1.2.0-rc1} will sort lexically.
 */
public final class UpdateChecker {
	private static final HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

	private UpdateChecker() {}

	/**
	 * @param modId       Fabric mod id of the installed mod (used to read the local version)
	 * @param ownerRepo   GitHub {@code owner/repo} to query for releases
	 * @return Future resolving to {@link Result#upToDate()} or {@link Result#newer(String, String)}.
	 *         Failures resolve to {@link Result#unknown(String)} with the error message.
	 */
	public static CompletableFuture<Result> check(String modId, String ownerRepo) {
		Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer(modId);
		if (mod.isEmpty()) {
			return CompletableFuture.completedFuture(Result.unknown("mod " + modId + " not loaded"));
		}
		String installed = mod.get().getMetadata().getVersion().getFriendlyString();

		HttpRequest req = HttpRequest.newBuilder(URI.create("https://api.github.com/repos/" + ownerRepo + "/releases/latest"))
			.header("Accept", "application/vnd.github+json")
			.header("User-Agent", "HyZodiacAPI-update-checker")
			.timeout(Duration.ofSeconds(10))
			.GET()
			.build();

		return HTTP.sendAsync(req, HttpResponse.BodyHandlers.ofString())
			.thenApply(resp -> {
				if (resp.statusCode() == 404) return Result.unknown("no releases published");
				if (resp.statusCode() / 100 != 2) return Result.unknown("HTTP " + resp.statusCode());
				try {
					JsonObject body = JsonParser.parseString(resp.body()).getAsJsonObject();
					String tag = body.get("tag_name").getAsString();
					String latest = tag.startsWith("v") ? tag.substring(1) : tag;
					return latest.compareTo(installed) > 0 ? Result.newer(installed, latest) : Result.upToDate();
				} catch (Exception e) {
					return Result.unknown("parse: " + e.getMessage());
				}
			})
			.exceptionally(t -> {
				HyZodiacApi.LOGGER.debug("update check failed for {}: {}", modId, t.getMessage());
				return Result.unknown(t.getMessage());
			});
	}

	public sealed interface Result {
		static Result upToDate()                              { return new UpToDate(); }
		static Result newer(String installed, String latest)  { return new Newer(installed, latest); }
		static Result unknown(String reason)                  { return new Unknown(reason); }

		record UpToDate() implements Result {}
		record Newer(String installed, String latest) implements Result {}
		record Unknown(String reason) implements Result {}
	}
}

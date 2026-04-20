package com.hyzodiac.api.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thin wrapper around SLF4J that prefixes every line with the owning mod's display name.
 * Use {@link #of(String)} to obtain an instance — passing the human-readable mod name,
 * not the Fabric mod id.
 */
public final class HyZodiacLogger {
	private final Logger delegate;
	private final String prefix;

	private HyZodiacLogger(Logger delegate, String displayName) {
		this.delegate = delegate;
		this.prefix = "[" + displayName + "] ";
	}

	public static HyZodiacLogger of(String displayName) {
		return new HyZodiacLogger(LoggerFactory.getLogger("HyZodiac/" + displayName), displayName);
	}

	public void info(String msg, Object... args)  { delegate.info(prefix + msg, args); }
	public void warn(String msg, Object... args)  { delegate.warn(prefix + msg, args); }
	public void error(String msg, Object... args) { delegate.error(prefix + msg, args); }
	public void debug(String msg, Object... args) { delegate.debug(prefix + msg, args); }

	public Logger raw() { return delegate; }
}

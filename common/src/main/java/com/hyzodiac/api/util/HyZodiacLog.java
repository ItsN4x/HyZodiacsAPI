package com.hyzodiac.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tiny shim around SLF4J so consumers don't need to keep passing the logger instance.
 * Every log line is prefixed with the mod name so it's easy to grep in a shared log.
 */
public final class HyZodiacLog {

    private static final Logger LOG = LoggerFactory.getLogger("HyZodiacsAPI");

    private HyZodiacLog() {
    }

    public static void info(String msg) {
        LOG.info(msg);
    }

    public static void warn(String msg) {
        LOG.warn(msg);
    }

    public static void warn(String msg, Throwable t) {
        LOG.warn(msg, t);
    }

    public static void error(String msg, Throwable t) {
        LOG.error(msg, t);
    }

    public static void debug(String msg) {
        LOG.debug(msg);
    }
}

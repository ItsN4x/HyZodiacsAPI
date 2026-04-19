package com.hyzodiac.api.platform;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Utility for locating a single service implementation via {@link ServiceLoader}.
 * Throws early with a descriptive message when zero or multiple implementations are
 * found — common symptom of a broken multi-loader build.
 */
public final class Services {

    private Services() {
    }

    public static <T> T load(Class<T> serviceClass) {
        ServiceLoader<T> loader = ServiceLoader.load(serviceClass);
        Iterator<T> it = loader.iterator();
        if (!it.hasNext()) {
            throw new IllegalStateException(
                    "No HyZodiac's API platform implementation found for " + serviceClass.getName()
                            + ". Make sure the loader-specific jar is present on the classpath.");
        }
        T impl = it.next();
        if (it.hasNext()) {
            throw new IllegalStateException(
                    "Multiple HyZodiac's API platform implementations found for "
                            + serviceClass.getName() + ". Only one platform jar may be loaded.");
        }
        return impl;
    }
}

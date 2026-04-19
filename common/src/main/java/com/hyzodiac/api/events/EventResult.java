package com.hyzodiac.api.events;

/**
 * Return value for cancellable events.
 *
 * <ul>
 *   <li>{@link #PASS} — the listener did not care; continue with normal behavior.</li>
 *   <li>{@link #ALLOW} — explicitly allow the action, short-circuiting further listeners.</li>
 *   <li>{@link #DENY}  — cancel the action and skip further listeners.</li>
 * </ul>
 */
public enum EventResult {
    PASS,
    ALLOW,
    DENY;

    public boolean interruptsFurtherEvaluation() {
        return this != PASS;
    }
}

package com.mattunderscore.yaef;

/**
 * A listener for events.
 * @author Matt Champion on 04/02/14.
 */
public interface EventListener {
    /**
     * The callback for events.
     * @param event The event.
     */
    void onEvent(Event event);
}

package com.mattunderscore.yaef;

/**
 * A filter for events.
 * @author Matt Champion on 04/02/14.
 */
public interface EventFilter {
    /**
     * Filters events.
     * @param event The event to filter.
     * @return {@code true} iff the filter allows the event to be passed on.
     */
    boolean accept(Event event);
}

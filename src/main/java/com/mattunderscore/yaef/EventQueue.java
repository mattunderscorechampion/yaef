package com.mattunderscore.yaef;

/**
 * A queue for events.
 * @author Matt Champion on 04/02/14.
 */
public interface EventQueue {
    /**
     * Add an event to the queue.
     * @param event An event to add.
     */
    void add(Event event);

    /**
     * Remove an event from the queue.
     * @return An event from the queue or null.
     */
    Event poll();
}

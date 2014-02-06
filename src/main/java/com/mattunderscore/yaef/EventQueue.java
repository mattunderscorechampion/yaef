package com.mattunderscore.yaef;

/**
 * A queue for events.
 * @author Matt Champion on 04/02/14.
 */
public interface EventQueue {
    /**
     * Add an event to the queue if it can be done so immediately.
     * @param event An event to add.
     * @return {@code true} iff the event was added.
     */
    boolean offer(Event event);

    /**
     * Remove and return an event from the queue.
     * @return An event from the queue or null.
     */
    Event poll();
}

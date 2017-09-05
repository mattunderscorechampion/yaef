package com.mattunderscore.yaef.pipeline;

import java.util.function.Consumer;

/**
 * A pipeline of processed events.
 *
 * @author Matt Champion on 05/09/17
 */
public interface Pipeline<T> {
    /**
     * Attach a consumer for events from the pipeline.
     * @param consumer The consumer.
     */
    void attach(Consumer<T> consumer);
}

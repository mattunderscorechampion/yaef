package com.mattunderscore.yaef.pipeline;

/**
 * @author Matt Champion on 26/04/16
 */
@FunctionalInterface
public interface Transformer<T, R, E extends Exception> {
    R apply(T value) throws E;
}

/* Copyright © 2014 Matthew Champion
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of mattunderscore.com nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL MATTHEW CHAMPION BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.mattunderscore.yaef;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An event mapper from the type of an event to an object. This ensures that an event which is a subtype of the mapping
 * key will be included in the listeners.
 * @author matt on 06/02/14.
 */
public final class EventTypeMapper<T> implements MutableEventMapper<Class<? extends Event>, T> {
    private final ConcurrentHashMap<Class<?>,Collection<T>> map = new ConcurrentHashMap<Class<?>, Collection<T>>();

    @Override
    public void addMapping(final Class<? extends Event> key, final T value) {
        final Collection<T> collection = Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());
        collection.add(value);
        final Collection<T> existingCollection = map.putIfAbsent(key, collection);
        if (existingCollection != null) {
            existingCollection.add(value);
        }
    }

    @Override
    public Collection<T> objectsForEvent(final Event event) {
        final Class<? extends Event> eventClass = event.getClass();
        final Set<T> objects = new HashSet<>();
        final List<Class<?>> possibleKeys = getSuperTypesThatImplementEvent(eventClass);
        for (final Class<?> key : possibleKeys) {
            final Collection<T> typeObjects = map.get(key);
            if (typeObjects != null) {
                objects.addAll(typeObjects);
            }
        }
        return objects;
    }

    /**
     * Get a list of classes that are between this class and the {@link Event} class in the hierarchy.
     * @param klass The class to search from.
     * @return The list of classes.
     */
    static List<Class<?>> getSuperTypesThatImplementEvent(final Class<? extends Event> klass) {
        final List<Class<?>> possibleKeys = new ArrayList<>();
        if (isEventType(klass)) {
            addTypesToEvent(possibleKeys, klass);
        }
        return possibleKeys;
    }

    private static void addTypesToEvent(final List<Class<?>> list, final Class<?> klass) {
        list.add(klass);
        final Class<?> superClass = klass.getSuperclass();
        if (superClass != null && isEventType(superClass)) {
            addTypesToEvent(list, superClass);
        }
        final Class<?>[] interfaces = klass.getInterfaces();
        for (final Class<?> intFace : interfaces) {
            if (isEventType(intFace)) {
                addTypesToEvent(list, intFace);
            }
        }
    }

    /**
     * Checks the class is a subtype of {@link Event}.
     * @param klass The class to check.
     * @return {@@code true} iff the class is a subtype of {@link Event}.
     */
    private static boolean isEventType(final Class<?> klass) {
        return Event.class.isAssignableFrom(klass);
    }
}
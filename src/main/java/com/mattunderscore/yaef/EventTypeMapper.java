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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An event mapper from the type of an event to an object. This ensures that an event which is a subtype of the mapping
 * key will be included in the listeners.
 * @author matt on 06/02/14.
 */
public final class EventTypeMapper implements EventMapper<EventListener<Event>> {
    /**
     * This is a map of classes that extend Event to collections of event listeners.
     */
    private final ConcurrentHashMap<Class<? extends Event>,Collection<EventListener<Event>>> map =
            new ConcurrentHashMap<>();

    /**
     * Add an event type mapping for a listener. The mapper will dispatch any events that match the key or a subtype of
     * the key to the listener. The listener must accept a super type of the key class.
     * @param key The type or super type of events that will be dispatched to the listener.
     * @param value The listener to dispatch events to. It must accept a super type of the key.
     * @param <T> The class of the key, must extend Event. The event listener must be invokable with a super type of
     *           the key.
     */
    @SuppressWarnings("unchecked")
    public <T extends Event> void addMapping(final Class<T> key, final EventListener<? super T> value) {
        final Collection<EventListener<Event>> collection =
                Collections.newSetFromMap(new ConcurrentHashMap<EventListener<Event>, Boolean>());
        // An event listener must accept subtypes of Event, they can always be added to a collection of
        // EventListener<Event>
        collection.add((EventListener<Event>)value);
        final Collection<EventListener<Event>> existingCollection = map.putIfAbsent(key, collection);
        if (existingCollection != null) {
            // An event listener must accept subtypes of Event, they can always be added to a collection of
            // EventListener<Event>
            existingCollection.add((EventListener<Event>)value);
        }
    }

    @Override
    public Collection<EventListener<Event>> objectsForEvent(final Event event) {
        final Class<? extends Event> eventClass = event.getClass();
        final Set<EventListener<Event>> objects = new HashSet<>();
        final List<Class<? extends Event>> possibleKeys = getSuperTypesThatImplementEvent(eventClass);
        for (final Class<? extends Event> key : possibleKeys) {
            final Collection<EventListener<Event>> typeObjects = map.get(key);
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
    static List<Class<? extends Event>> getSuperTypesThatImplementEvent(final Class<? extends Event> klass) {
        final List<Class<? extends Event>> possibleKeys = new ArrayList<>();
        if (isEventType(klass)) {
            addTypesToEvent(possibleKeys, klass);
        }
        return possibleKeys;
    }

    @SuppressWarnings("unchecked")
    private static void addTypesToEvent(final List<Class<? extends Event>> list, final Class<? extends Event> klass) {
        list.add(klass);
        final Class<?> superClass = klass.getSuperclass();
        if (superClass != null && isEventType(superClass)) {
            // Cast must be valid if isEventType returns true
            addTypesToEvent(list, (Class<? extends Event>)superClass);
        }
        final Class<?>[] interfaces = klass.getInterfaces();
        for (final Class<?> intFace : interfaces) {
            if (isEventType(intFace)) {
                // Cast must be valid if isEventType returns true
                addTypesToEvent(list, (Class<? extends Event>)intFace);
            }
        }
    }

    /**
     * Checks the class is a subtype of {@link Event}.
     * @param klass The class to check.
     * @return {@code true} iff the class is a subtype of {@link Event}.
     */
    private static boolean isEventType(final Class<?> klass) {
        return Event.class.isAssignableFrom(klass);
    }
}

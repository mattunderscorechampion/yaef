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

import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EventTypeMapper}.
 * @author matt on 06/02/14.
 */
public final class EventTypeMapperTest {
    @Test
    public void checkSuperTypesToEventForEventA() {
        final List<Class<?>> classes = EventTypeMapper.getSuperTypesThatImplementEvent(EventA.class);
        assertEquals(2, classes.size());
        assertTrue(classes.contains(EventA.class));
        assertTrue(classes.contains(Event.class));
    }

    @Test
    public void checkSuperTypesToEventForEventB() {
        final List<Class<?>> classes = EventTypeMapper.getSuperTypesThatImplementEvent(EventB.class);
        assertEquals(2, classes.size());
        assertTrue(classes.contains(EventB.class));
        assertTrue(classes.contains(Event.class));
    }

    @Test
    public void checkSuperTypesToEventForEventASubclass() {
        final List<Class<?>> classes = EventTypeMapper.getSuperTypesThatImplementEvent(EventASubclass.class);
        assertEquals(3, classes.size());
        assertTrue(classes.contains(EventASubclass.class));
        assertTrue(classes.contains(EventA.class));
        assertTrue(classes.contains(Event.class));
    }

    @Test
    public void checkSuperTypesToEventForInterestingEventA() {
        final List<Class<?>> classes = EventTypeMapper.getSuperTypesThatImplementEvent(InterestingEventA.class);
        assertEquals(3, classes.size());
        assertTrue(classes.contains(InterestingEventA.class));
        assertTrue(classes.contains(InterestingEvent.class));
        assertTrue(classes.contains(Event.class));
    }

    @Test
    public void correctTypeMapped0() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final Object target = new Object();
        mapper.addMapping(EventA.class, target);
        final Event event = new EventA();
        final Collection<Object> objects = mapper.objectsForEvent(event);
        assertEquals(1, objects.size());
        assertTrue(objects.contains(target));
    }

    @Test
    public void correctTypeMapped1() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final Object target = new Object();
        mapper.addMapping(EventB.class, target);
        final Event event = new EventB();
        final Collection<Object> objects = mapper.objectsForEvent(event);
        assertEquals(1, objects.size());
        assertTrue(objects.contains(target));
    }

    @Test
    public void incorrectTypeNotMapped0() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final Object target = new Object();
        mapper.addMapping(EventA.class, target);
        final Event event = new EventB();
        final Collection<Object> objects = mapper.objectsForEvent(event);
        assertEquals(0, objects.size());
    }

    @Test
    public void incorrectTypeNotMapped1() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final Object target = new Object();
        mapper.addMapping(EventB.class, target);
        final Event event = new EventA();
        final Collection<Object> objects = mapper.objectsForEvent(event);
        assertEquals(0, objects.size());
    }

    @Test
    public void subTypeOfCorrectMapped() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final Object target = new Object();
        mapper.addMapping(EventA.class, target);
        final Event event = new EventASubclass();
        final Collection<Object> objects = mapper.objectsForEvent(event);
        assertEquals(1, objects.size());
        assertTrue(objects.contains(target));
    }

    @Test
    public void multipleCorrectMappings() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final Object target0 = new Object();
        final Object target1 = new Object();
        mapper.addMapping(EventA.class, target0);
        mapper.addMapping(Event.class, target1);
        final Event event = new EventASubclass();
        final Collection<Object> objects = mapper.objectsForEvent(event);
        assertEquals(2, objects.size());
        assertTrue(objects.contains(target0));
        assertTrue(objects.contains(target1));
    }

    @Test
    public void mixedCorrectAndIncorrectMappings() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final Object target0 = new Object();
        final Object target1 = new Object();
        final Object target2 = new Object();
        mapper.addMapping(EventB.class, target2);
        mapper.addMapping(EventA.class, target1);
        mapper.addMapping(EventASubclass.class, target0);
        final Event event0 = new EventASubclass();
        final Event event1 = new EventB();

        final Collection<Object> objects0 = mapper.objectsForEvent(event0);
        assertEquals(2, objects0.size());
        assertTrue(objects0.contains(target0));
        assertTrue(objects0.contains(target1));

        final Collection<Object> objects1 = mapper.objectsForEvent(event1);
        assertEquals(1, objects1.size());
        assertTrue(objects1.contains(target2));
    }
}

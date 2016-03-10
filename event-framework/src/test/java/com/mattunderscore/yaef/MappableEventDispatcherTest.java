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

import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.EmptyListener;
import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.EventA;
import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.EventASubclass;
import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.EventB;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author matt on 10/02/14.
 */
public class MappableEventDispatcherTest {

    @Test
    public void exactEventTypeMapping0() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final EmptyListener<EventA> target = new EmptyListener<>();
        mapper.addMapping(EventA.class, target);
        final MappableEventDispatcher dispatcher = new MappableEventDispatcher();
        dispatcher.addMapper(mapper);
        final EventA event = new EventA();
        dispatcher.dispatch(event);
        assertEquals(1, target.events.size());
        assertSame(event, target.events.get(0));
    }

    @Test
    public void exactEventTypeMapping1() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final EmptyListener<EventB> target = new EmptyListener<>();
        mapper.addMapping(EventB.class, target);
        final MappableEventDispatcher dispatcher = new MappableEventDispatcher();
        dispatcher.addMapper(mapper);
        final EventB event = new EventB();
        dispatcher.dispatch(event);
        assertEquals(1, target.events.size());
        assertSame(event, target.events.get(0));
    }

    @Test
    public void nonexactEventTypeMapping0() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final EmptyListener<Event> target = new EmptyListener<>();
        mapper.addMapping(EventB.class, target);
        final MappableEventDispatcher dispatcher = new MappableEventDispatcher();
        dispatcher.addMapper(mapper);
        final EventB event = new EventB();
        dispatcher.dispatch(event);
        assertEquals(1, target.events.size());
        assertSame(event, target.events.get(0));
    }

    @Test
    public void nonexactEventTypeMapping1() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final EmptyListener<Event> target = new EmptyListener<>();
        mapper.addMapping(Event.class, target);
        final MappableEventDispatcher dispatcher = new MappableEventDispatcher();
        dispatcher.addMapper(mapper);
        final EventB event0 = new EventB();
        final EventA event1 = new EventA();
        dispatcher.dispatch(event0);
        dispatcher.dispatch(event1);
        assertEquals(2, target.events.size());
        assertSame(event0, target.events.get(0));
        assertSame(event1, target.events.get(1));
    }

    @Test
    public void nonexactEventTypeMapping2() {
        final EventTypeMapper mapper = new EventTypeMapper();
        final EmptyListener<Event> target = new EmptyListener<>();
        mapper.addMapping(EventA.class, target);
        final MappableEventDispatcher dispatcher = new MappableEventDispatcher();
        dispatcher.addMapper(mapper);
        final EventB event0 = new EventB();
        final EventA event1 = new EventA();
        final EventASubclass event2 = new EventASubclass();
        dispatcher.dispatch(event0);
        dispatcher.dispatch(event1);
        dispatcher.dispatch(event2);
        assertEquals(2, target.events.size());
        assertSame(event1, target.events.get(0));
        assertSame(event2, target.events.get(1));
    }
}

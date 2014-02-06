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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.EventA;
import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.EventASubclass;
import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.EventB;
import org.junit.Test;

/**
 * Tests for type filter.
 * @author Matt Champion on 04/02/14.
 */
public final class EventTypeFilterTest {
    @Test
    public void correctTypeAccepted0() {
        final EventTypeFilter filter = new EventTypeFilter(EventA.class);
        final Event event = new EventA();
        assertTrue("Filter must accept events of the type passed in", filter.accept(event));
    }

    @Test
    public void correctTypeAccepted1() {
        final EventTypeFilter filter = new EventTypeFilter(EventB.class);
        final Event event = new EventB();
        assertTrue("Filter must accept events of the type passed in", filter.accept(event));
    }

    @Test
    public void incorrectTypeNotAccepted0() {
        final EventTypeFilter filter = new EventTypeFilter(EventA.class);
        final Event event = new EventB();
        assertFalse("Filter must not accept events different to the type passed in", filter.accept(event));
    }

    @Test
    public void incorrectTypeNotAccepted1() {
        final EventTypeFilter filter = new EventTypeFilter(EventB.class);
        final Event event = new EventA();
        assertFalse("Filter must not accept events different to the type passed in", filter.accept(event));
    }

    @Test
    public void subTypeOfCorrectAccepted() {
        final EventTypeFilter filter = new EventTypeFilter(EventA.class);
        final Event event = new EventASubclass();
        assertTrue("Filter must accept events of a subclass of the type passed in", filter.accept(event));
    }
}

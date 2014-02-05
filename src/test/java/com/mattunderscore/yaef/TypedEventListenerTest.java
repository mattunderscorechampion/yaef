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

import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.EventA;
import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.EventASubclass;
import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.EventB;
import org.junit.Before;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests for type filter.
 * @author Matt Champion on 05/02/14.
 */
public final class TypedEventListenerTest {
    private EventListener mockedListener;

    @Before
    public void setUp() {
        mockedListener = mock(EventListener.class);
    }

    public void correctTypePassedOn0() {
        final TypedEventListener listener = new TypedEventListener(EventA.class, mockedListener);
        final Event event = new EventA();
        listener.onEvent(event);
        verify(mockedListener).onEvent(event);
    }

    public void correctTypePassedOn1() {
        final TypedEventListener listener = new TypedEventListener(EventB.class, mockedListener);
        final Event event = new EventB();
        listener.onEvent(event);
        verify(mockedListener).onEvent(event);
    }

    public void incorrectTypeDropped0() {
        final TypedEventListener listener = new TypedEventListener(EventA.class, mockedListener);
        final Event event = new EventB();
        listener.onEvent(event);
        verify(mockedListener, never()).onEvent(any(Event.class));
    }

    public void incorrectTypeDropped1() {
        final TypedEventListener listener = new TypedEventListener(EventB.class, mockedListener);
        final Event event = new EventA();
        listener.onEvent(event);
        verify(mockedListener, never()).onEvent(any(Event.class));
    }

    public void subTypeOfCorrectPassedOn() {
        final TypedEventListener listener = new TypedEventListener(EventA.class, mockedListener);
        final Event event = new EventASubclass();
        listener.onEvent(event);
        verify(mockedListener).onEvent(event);
    }
}

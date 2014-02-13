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
import java.util.HashSet;
import java.util.Set;

/**
 * @author matt on 07/02/14.
 */
public class MappableEventDispatcher implements EventDispatcher {
    private final Set<EventMapper> mappers;

    public MappableEventDispatcher() {
        mappers = new HashSet<>();
    }

    /**
     * Add a mapper to the dispatcher.
     * @param mapper The mapper to add.
     */
    public void addMapper(final EventMapper mapper) {
        mappers.add(mapper);
    }

    @Override
    public void dispatch(final Event event) {
        dispatchInternal(event);
    }

    private <T extends Event> void dispatchInternal(final T event) {
        for (final EventMapper mapper : mappers) {
            final Collection<EventListener<? super T>> listeners = mapper.listenersForEvent(event);
            for (final EventListener<? super T> listener : listeners) {
                final EventListener<? super T> l = listener;
                l.onEvent(event);
            }
        }
    }
}

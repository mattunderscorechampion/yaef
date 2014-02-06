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
import com.mattunderscore.yaef.com.mattunderscore.yaef.stubs.EventB;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for TypedEventMapper.
 * @author matt on 05/02/14.
 */
public final class ExactEventTypeMapperTest {
    @Test
    public void correctTypeMapsToObject() {
        final ExactEventTypeMapper<Object> mapper = new ExactEventTypeMapper<>();
        final Object object0 = new Object();
        mapper.addMapping(EventA.class, object0);

        final Collection<Object> objects = mapper.objectsForEvent(new EventA());
        assertEquals(1, objects.size());
        assertTrue(objects.contains(object0));
    }

    @Test
    public void correctTypeMapsToObjects() {
        final ExactEventTypeMapper<Object> mapper = new ExactEventTypeMapper<>();
        final Object object0 = new Object();
        final Object object1 = new Object();
        mapper.addMapping(EventA.class, object0);
        mapper.addMapping(EventA.class, object1);

        final Collection<Object> objects = mapper.objectsForEvent(new EventA());
        assertEquals(2, objects.size());
        assertTrue(objects.contains(object0));
        assertTrue(objects.contains(object1));
    }

    @Test
    public void incorrectTypeMapsToEmptyCollection() {
        final ExactEventTypeMapper<Object> mapper = new ExactEventTypeMapper<>();
        final Object object0 = new Object();
        mapper.addMapping(EventA.class, object0);

        final Collection<Object> objects = mapper.objectsForEvent(new EventB());
        assertEquals(0, objects.size());
    }

    @Test
    public void typeMapsToSubsetOfMappings() {
        final ExactEventTypeMapper<Object> mapper = new ExactEventTypeMapper<>();
        final Object object0 = new Object();
        final Object object1 = new Object();
        mapper.addMapping(EventA.class, object0);
        mapper.addMapping(EventB.class, object1);

        final Collection<Object> objects = mapper.objectsForEvent(new EventA());
        assertEquals(1, objects.size());
        assertTrue(objects.contains(object0));
        assertFalse(objects.contains(object1));
    }
}

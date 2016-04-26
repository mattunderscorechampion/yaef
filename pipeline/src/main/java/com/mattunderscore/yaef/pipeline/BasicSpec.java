/* Copyright © 2016 Matthew Champion
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

package com.mattunderscore.yaef.pipeline;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A basic {@link PipelineSpec}.
 * @author Matt Champion on 18/03/16
 */
/*package*/ final class BasicSpec<S, T> implements PipelineSpec<S, T> {
    private final Function<S, Optional<T>> pipe;

    public BasicSpec(Function<S, Optional<T>> pipe) {
        this.pipe = pipe;
    }

    @Override
    public <R> PipelineSpec<S, R> transform(Function<T, R> function) {
        return new BasicSpec<>(pipe.andThen(ot -> ot.map(function)));
    }

    @Override
    public <R, E extends Error> PipelineSpec<S, OrError<R, E>> transform(Transformer<T, R, E> function) {
        return new BasicSpec<>(pipe.andThen(ot -> {
            if (ot.isPresent()) {
                return Optional.ofNullable(function.apply(ot.get()));
            }
            else {
                return Optional.empty();
            }
        }));
    }

    @Override
    public PipelineSpec<S, T> filter(Predicate<T> predicate) {
        return new BasicSpec<>(pipe.andThen(ot -> ot.filter(predicate)));
    }

    @Override
    public <R> PipelineSpec<S, R> transformOrFilter(Function<T, Optional<R>> function) {
        return new BasicSpec<>(pipe.andThen(ot -> ot.flatMap(function)));
    }
}

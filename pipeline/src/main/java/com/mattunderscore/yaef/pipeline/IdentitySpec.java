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
 * Identity {@link PipelineSpec}. Does no processing.
 * @author Matt Champion on 18/03/16
 */
/*package*/ final class IdentitySpec<S> implements PipelineSpec<S, S> {
    private static final IdentitySpec RAW_INSTANCE = new IdentitySpec();

    private IdentitySpec() {
    }

    @Override
    public <R> PipelineSpec<S, R> transform(Function<S, R> function) {
        return new BasicSpec<>(function.andThen(Optional::ofNullable));
    }

    @Override
    public <R, E extends Exception> PipelineSpec<S, OrError<R, E>> transform(Transformer<S, R, E> function) {
        return new BasicSpec<>(v -> {
            try {
                final R value = function.apply(v);
                if (value == null) {
                    return Optional.empty();
                }
                else {
                    return Optional.of(new OrErrorOk<>(value));
                }
            } catch (Exception e) {
                return Optional.of(new OrErrorException<>((E) e));
            }
        });
    }

    @Override
    public PipelineSpec<S, S> filter(Predicate<S> predicate) {
        return new BasicSpec<>(s -> Optional.ofNullable(s).filter(predicate));
    }

    @Override
    public <R> PipelineSpec<S, R> transformOrFilter(Function<S, Optional<R>> function) {
        return new BasicSpec<>(function);
    }

    /**
     * @param <S>
     * @return An identity {@link PipelineSpec}.
     */
    public static <S> PipelineSpec<S, S> get() {
        return RAW_INSTANCE;
    }
}

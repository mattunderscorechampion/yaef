package com.mattunderscore.yaef.pipeline;/* Copyright © 2016 Matthew Champion
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

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The specification for a pipeline.
 * @author Matt Champion on 10/03/16
 */
public interface PipelineSpec<S, T> {
    /**
     * Transform the values passed through the pipeline.
     * @param function The function performing the transformation
     * @param <R> The type of value passed on through the pipeline
     * @return The new specification for a pipeline
     */
    <R> PipelineSpec<S, R> transform(Function<T, R> function);

    /**
     * Filter the values passed through the pipeline.
     * @param predicate The predicate filtering the pipeline
     * @return The new specification for a pipeline
     */
    PipelineSpec<S, T> filter(Predicate<T> predicate);

    /**
     * Transform or filter the values passed through the pipeline. If the {@link Optional} does not have a value the
     * value is filtered, otherwise it is transformed.
     * @param function The function performing the transformation
     * @param <R> The type of value passed on through the pipeline
     * @return The new specification for a pipeline
     */
    <R> PipelineSpec<S, R> transformOrFilter(Function<T, Optional<R>> function);
}

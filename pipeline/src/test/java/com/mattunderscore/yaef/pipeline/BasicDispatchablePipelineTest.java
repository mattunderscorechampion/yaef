package com.mattunderscore.yaef.pipeline;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit tests for {@link BasicDispatchablePipeline}.
 *
 * @author Matt Champion on 05/09/17
 */
public class BasicDispatchablePipelineTest {
    @Mock
    private Consumer<String> consumer0;
    @Mock
    private Consumer<String> consumer1;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void postConditions() {
        verifyNoMoreInteractions(consumer0, consumer1);
    }

    @Test
    public void dispatchedValueDeliveredToSingleConsumer() throws Exception {
        final DispatchablePipeline<String, String> pipeline = new BasicDispatchablePipeline<>(Optional::ofNullable);

        pipeline.attach(consumer0);

        pipeline.dispatch("hello");

        verify(consumer0).accept("hello");
    }

    @Test
    public void dispatchedValueDeliveredToMultipleConsumers() throws Exception {
        final DispatchablePipeline<String, String> pipeline = new BasicDispatchablePipeline<>(Optional::ofNullable);

        pipeline.attach(consumer0);
        pipeline.attach(consumer1);

        pipeline.dispatch("hello");

        verify(consumer0).accept("hello");
        verify(consumer1).accept("hello");
    }

    @Test
    public void filteredValueNotDeliveredToConsumer() throws Exception {
        final DispatchablePipeline<String, String> pipeline = new BasicDispatchablePipeline<>(Optional::ofNullable);

        pipeline.attach(consumer0);

        pipeline.dispatch(null);
    }
}

package common.port.impl;

import lombok.Data;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

public class AbstractInMemoryAdapterTest {

    @Value
    private static class TestObject {
        private final int id;
        private final String text;
    }

    private AbstractInMemoryAdapter<Integer,TestObject> adapter;

    @Before
    public void setUp() throws Exception {
        this.adapter = new AbstractInMemoryAdapter<>() {
            @Override
            protected Integer extractId(final TestObject obj) {
                return obj.getId();
            }
        };
    }

    @Test
    public void save(){
        // given
        final int id = 1;
        final String text = "test";
        // when
        this.adapter.save(new TestObject(1, text));
        // then
        final Optional<TestObject> opt = this.adapter.findById(id);
        assertTrue(opt.isPresent());
        assertEquals(text, opt.get().getText());
    }

}
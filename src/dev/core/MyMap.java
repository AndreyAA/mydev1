package dev.core;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class MyMap {

    @Test
    public void test() {

        Value v1 = new Value(1, "1");
        Value v12 = new Value(1, "2");
        Map<Value, Integer> map = new HashMap<>();
        map.put(v1, 1);
        map.put(v12, 2);

        // key is NOT changed, value is changed
        assertEquals(1, map.keySet().iterator().next().id);
        assertEquals(2, map.values().iterator().next().intValue());
    }

    public static class Value {
        private final int id;
        private final String name;

        public Value(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Value value = (Value) o;
            return id == value.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "Value{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

}

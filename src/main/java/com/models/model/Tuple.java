package com.models.model;

import java.util.Objects;

public record Tuple(String path, String method) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Objects.equals(path, tuple.path) && Objects.equals(method, tuple.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}

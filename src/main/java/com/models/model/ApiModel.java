package com.models.model;

import java.util.Objects;

public record ApiModel(
        String path,
        String method,
        AttributeDescriptor[]  query_params,
        AttributeDescriptor[]  headers,
        AttributeDescriptor[]   body
        ) {

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                ApiModel apiModel = (ApiModel) o;
                return path.equals(apiModel.path) && method.equals(apiModel.method);
        }

        @Override
        public int hashCode() {
                return Objects.hash(path, method);
        }
}

package com.models.model;

public record ApiRequest(String path,
                         String method,
                         QueryParamsInput[] query_params,
                         QueryParamsInput[] headers,
                         QueryParamsInput[] body) {
}

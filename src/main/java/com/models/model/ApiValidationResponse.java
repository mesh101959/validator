package com.models.model;

public record ApiValidationResponse(String status,
                                    AbnormalFields[] failedFields) {
}

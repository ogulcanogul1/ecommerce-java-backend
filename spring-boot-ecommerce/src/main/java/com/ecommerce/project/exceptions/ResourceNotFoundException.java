package com.ecommerce.project.exceptions;

import lombok.Data;

@Data
public class ResourceNotFoundException  extends  RuntimeException{
    private String resourceName;
    private String fieldName;
    private Long fieldId;

    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldId) {
        super(String.format("%s Resource not found with %s : %s", resourceName ,fieldName, fieldId));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldId = fieldId;
    }
}

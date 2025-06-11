package com.ecommerce.project.exceptions;

import lombok.Getter;
import lombok.Setter;

public class AlreadyTakenException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    @Getter @Setter
    private String fieldName;

    public AlreadyTakenException(String fieldName) {
        super(fieldName + " is already taken");
        this.fieldName = fieldName;
    }
}

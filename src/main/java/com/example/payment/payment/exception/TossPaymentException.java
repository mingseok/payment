package com.example.payment.payment.exception;

import lombok.Getter;

@Getter
public class TossPaymentException extends RuntimeException {

    private final String errorCode;

    public TossPaymentException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

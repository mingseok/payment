package com.example.payment.payment.exception;

import lombok.Getter;

@Getter
public class TossPaymentException extends RuntimeException {

    private final String errorCode;
    private final TossCallOutcome outcome;

    public TossPaymentException(String errorCode, String message) {
        this(errorCode, message, TossCallOutcome.UNKNOWN);
    }

    public TossPaymentException(String errorCode, String message, TossCallOutcome outcome) {
        super(message);
        this.errorCode = errorCode;
        this.outcome = outcome;
    }

    public boolean isRejected() {
        return outcome == TossCallOutcome.REJECTED;
    }
}

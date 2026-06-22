package com.example.payment.payment.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "toss.payments")
public class TossPaymentsProperties {

    private final String secretKey;
    private final String baseUrl;
}

package com.example.payment.payment.client;

import com.example.payment.payment.client.dto.TossPaymentCancelRequest;
import com.example.payment.payment.client.dto.TossPaymentConfirmRequest;
import com.example.payment.payment.client.dto.TossPaymentResponse;
import com.example.payment.payment.config.TossPaymentsProperties;
import com.example.payment.payment.exception.TossPaymentException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

@Component
@EnableConfigurationProperties(TossPaymentsProperties.class)
public class TossPaymentsClient {

    private final RestClient restClient;

    public TossPaymentsClient(TossPaymentsProperties properties) {
        String credentials = Base64.getEncoder()
                .encodeToString((properties.getSecretKey() + ":").getBytes(StandardCharsets.UTF_8));

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(15));

        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + credentials)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .requestFactory(factory)
                .build();
    }

    public TossPaymentResponse confirmPayment(TossPaymentConfirmRequest request) {
        return post("/v1/payments/confirm", request);
    }

    public TossPaymentResponse cancelPayment(String paymentKey, TossPaymentCancelRequest request) {
        return post("/v1/payments/" + paymentKey + "/cancel", request);
    }

    private TossPaymentResponse post(String uri, Object request) {
        try {
            return restClient.post()
                    .uri(uri)
                    .body(request)
                    .retrieve()
                    .body(TossPaymentResponse.class);
        } catch (RestClientResponseException e) {
            throw handleError(e);
        } catch (Exception e) {
            throw new TossPaymentException("UNKNOWN", "토스 API 호출 중 오류가 발생했습니다.");
        }
    }

    private TossPaymentException handleError(RestClientResponseException e) {
        return new TossPaymentException(
                e.getStatusCode().toString(),
                "토스 API 호출 실패: " + e.getStatusText());
    }
}

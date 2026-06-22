package com.example.payment.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId;

    @NotEmpty(message = "상품을 1개 이상 선택해야 합니다.")
    private List<Long> productIds;
}

package com.example.payment.member.dto.response;

import com.example.payment.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private final Long memberId;
    private final String name;

    public static LoginResponse from(Member member) {
        return LoginResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .build();
    }
}

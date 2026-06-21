package com.example.payment.member.dto.response;

import com.example.payment.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponse {

    private final Long memberId;
    private final String email;
    private final String name;

    public static SignupResponse from(Member member) {
        return SignupResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}

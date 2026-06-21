package com.example.payment.member.service;

import com.example.payment.member.dto.request.LoginRequest;
import com.example.payment.member.dto.request.SignupRequest;
import com.example.payment.member.dto.response.LoginResponse;
import com.example.payment.member.dto.response.SignupResponse;
import com.example.payment.member.entity.Member;
import com.example.payment.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        validateEmailNotDuplicated(request.getEmail());
        Member saved = memberRepository.save(request.toMember());
        return SignupResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Member member = getByEmail(request.getEmail());
        member.verifyPassword(request.getPassword());
        return LoginResponse.from(member);
    }

    private void validateEmailNotDuplicated(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
    }

    private Member getByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));
    }
}

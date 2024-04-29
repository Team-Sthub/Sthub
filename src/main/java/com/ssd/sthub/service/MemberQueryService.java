package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberQueryService {
    public final MemberRepository memberRepository;

    // 아이디 중복 확인
    public String checkNickname(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        if (member != null) {
            return "이미 존재하는 아이디입니다.";
        } else {
            return "사용가능한 아이디입니다.";
        }
    }

    // 프로필 조회 (추후에 dto를 사용하여 일부만 반환할 것)
    public Optional<Member> getMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member == null) {
            // exception 반환
        }
        return member;
    }

    // 프로필 상세 조회
    public Optional<Member> getMemberDetail(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member == null) {
            // exception 반환
        }
        return member;
    }
}

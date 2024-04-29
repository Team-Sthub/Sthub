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
public class MemberService {

    public final MemberRepository memberRepository;

    // 회원가입
    public Member register(Member member) {
        Optional<Member> isMember = memberRepository.findById(member.getId());
        if(isMember != null) {
            // exception 반환
        }
        return memberRepository.save(member);
    }

    // 로그인
    public Optional<Member> login(String nickname, String password) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        if(member == null) {
            // exception 반환
        }
        if(!member.get().getPassword().equals(password)) {
            // exception 반환
        }
        return member;
    }

    // 마이페이지 사용자 정보 수정
    public Member updateMember(Member member) {
        Optional<Member> isMember = memberRepository.findById(member.getId());
        if(isMember == null) {
            // exception 반환
        }
        return memberRepository.save(member);
    }
}

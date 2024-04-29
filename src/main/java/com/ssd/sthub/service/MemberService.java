package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.dto.member.MemberDTO;
import com.ssd.sthub.dto.member.RegisterDTO;
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
    public Member register(RegisterDTO registerDTO) {
        Optional<Member> isMember = memberRepository.findByNickname(registerDTO.getNickname());
        if(isMember != null) {
            // exception 반환
        }

        Member newMember = new Member(registerDTO);
        return memberRepository.save(newMember);
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
    public Member updateMember(Long memberId, MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NullPointerException()
        );

        member.updateInfo(memberDTO);
        return memberRepository.save(member);
    }
}

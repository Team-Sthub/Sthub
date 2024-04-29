package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.dto.member.MemberDTO;
import com.ssd.sthub.dto.member.UserViewDTO;
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

    // 프로필 조회
    public UserViewDTO getMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member == null) {
            // exception 반환
        }
        return new UserViewDTO(memberId, member.get().getNickname(), member.get().getProfile(), member.get().getMannerGrade(), member.get().getAddress());
    }

    // 프로필 상세 조회
    public MemberDTO.MemberResDTO getMemberDetail(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member == null) {
            // exception 반환
        }
        return new MemberDTO.MemberResDTO(memberId, member.get().getNickname(), member.get().getPassword(), member.get().getPhone(), member.get().getBank(), member.get().getAccount(),
                member.get().getAddress(), member.get().getLatitude(), member.get().getLongitude(), member.get().getEmail(), member.get().getProfile(), member.get().getMannerGrade());
    }
}

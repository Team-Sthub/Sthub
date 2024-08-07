package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.dto.member.LoginDTO;
import com.ssd.sthub.dto.member.MemberDTO;
import com.ssd.sthub.dto.member.RegisterDTO;
import com.ssd.sthub.dto.member.UserViewDTO;
import com.ssd.sthub.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public Member register(String imgUrl, RegisterDTO registerDTO) {
        Optional<Member> isMember = memberRepository.findByNickname(registerDTO.getNickname());
        if(isMember.isPresent()) {
            throw new EntityNotFoundException("이미 있는 사용자입니다.");
        }
        Member newMember = new Member(registerDTO);
        if (imgUrl != null) {
            newMember.setProfile(imgUrl);
        }
        return memberRepository.save(newMember);
    }

    // 로그인
    public Member login(LoginDTO loginDTO) {
        Optional<Member> member = memberRepository.findByNickname(loginDTO.getNickname());
        if(member.isEmpty()) {
            throw new EntityNotFoundException("회원 조회에 실패했습니다.");
        }
        if(!member.get().getPassword().equals(loginDTO.getPassword())) {
            throw new EntityNotFoundException("비밀번호가 일치하지 않습니다.");
        }
        return member.get();
    }

    // 아이디 중복 확인
    public boolean checkNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isEmpty();
    }

    // 프로필 조회
    public UserViewDTO getMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new EntityNotFoundException("회원 조회에 실패했습니다.");
        }
        Member foundMember = member.get();
        return new UserViewDTO(memberId, foundMember.getNickname(), foundMember.getProfile(), foundMember.getMannerGrade(), foundMember.getAddress());
    }

    // 프로필 상세 조회
    public MemberDTO.MemberResDTO getMemberDetail(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new EntityNotFoundException("회원 조회에 실패했습니다.");
        }
        return new MemberDTO.MemberResDTO(memberId, member.get().getNickname(), member.get().getPassword(), member.get().getPhone(), member.get().getBank(), member.get().getAccount(),
                member.get().getAddress(), member.get().getLatitude(), member.get().getLongitude(), member.get().getEmail(), member.get().getProfile(), member.get().getMannerGrade());
    }

    // 마이페이지 사용자 정보 수정
    public Member updateMember(Member existingMember) {
        // 이미 변경된 필드를 가지고 있는 existingMember 객체를 활용하여 업데이트
        return memberRepository.save(existingMember);
    }

    // 닉네임으로 사용자 조회
    public Member getMemberByNickname(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        if (member.isEmpty()) {
            throw new EntityNotFoundException("회원 조회에 실패했습니다.");
        }
        return member.get();
    }
}

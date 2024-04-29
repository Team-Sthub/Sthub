package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member save(Member member);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByNickname(String nickname);

}

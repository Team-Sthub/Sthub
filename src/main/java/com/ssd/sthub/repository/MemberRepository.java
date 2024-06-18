package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickname(String nickname);

    Member save(Optional<Member> member);
}

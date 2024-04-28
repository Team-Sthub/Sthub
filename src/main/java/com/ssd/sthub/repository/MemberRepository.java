package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {


}

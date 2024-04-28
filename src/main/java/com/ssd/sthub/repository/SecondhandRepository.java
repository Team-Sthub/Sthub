package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Secondhand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecondhandRepository extends JpaRepository<Secondhand, Long> {
    public List<Secondhand> findAllByMember(Member member);
}

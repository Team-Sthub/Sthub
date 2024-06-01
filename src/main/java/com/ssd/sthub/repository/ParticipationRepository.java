package com.ssd.sthub.repository;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Participation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Participation save(Participation participation);
    Optional<Participation> findById(Long participationId);
    Page<Participation> findAllByGroupBuyingId(Long groupBuyingId, PageRequest pageRequest);
    Page<Participation> findAllByMember(Member member, PageRequest pageRequest);
    List<Participation> findAllByMember(Member member);
}

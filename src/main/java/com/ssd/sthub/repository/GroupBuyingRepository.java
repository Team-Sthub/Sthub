package com.ssd.sthub.repository;

import com.ssd.sthub.domain.GroupBuying;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface GroupBuyingRepository extends JpaRepository<GroupBuying, Long> {

    GroupBuying save(GroupBuying groupBuying);
    Optional<GroupBuying> findById(Long groupBuyingId);
    Page<GroupBuying> findAll(PageRequest pageRequest);
    Page<GroupBuying> findAllByMemberId(Long memberId, PageRequest pageRequest);
    void deleteById(Long groupById);
}

package com.ssd.sthub.repository;

import com.ssd.sthub.domain.GImage;
import com.ssd.sthub.domain.GroupBuying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GImageRepository extends JpaRepository<GImage, Long> {

    List<GImage> findAllByGroupBuying(GroupBuying groupBuying);
    List<GImage> findAllByGroupBuyingId(Long groupBuyingId);
    void deleteAllByGroupBuying(GroupBuying groupBuying);
}

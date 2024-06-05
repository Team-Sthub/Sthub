package com.ssd.sthub.repository;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.enumerate.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupBuyingRepository extends JpaRepository<GroupBuying, Long> {

    Optional<GroupBuying> findById(Long groupBuyingId);
    Page<GroupBuying> findAllByMemberId(Long memberId, PageRequest pageRequest);
    Page<GroupBuying> findAllByCategoryOrderByCreatedAtDesc(Category category, PageRequest pageRequest);
    Page<GroupBuying> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);

    List<GroupBuying> findTop4ByMemberIdOrderByCreatedAtDesc(Long memberId);

    // 공동구매 키워드 검색
    List<GroupBuying> findAllByTitleContainingOrderByCreatedAtDesc(String title);

    // 내 근처 조회
    @Query("SELECT gb " +
            "FROM GroupBuying gb " +
            "WHERE " +
            "   6371 * " +
            "   ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(gb.latitude)) * " +
            "       COS(RADIANS(gb.longitude) - RADIANS(:longitude)) + " +
            "       SIN(RADIANS(:latitude)) * SIN(RADIANS(gb.latitude))) <= 5")
    Page<GroupBuying> findByLocationWithin5kmOrderByCreatedAtDesc(@Param("latitude") double latitude, @Param("longitude") double longitude, PageRequest pageRequest);

    @Query("SELECT gb " +
            "FROM GroupBuying gb " +
            "WHERE " +
            "   6371 * " +
            "   ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(gb.latitude)) * " +
            "       COS(RADIANS(gb.longitude) - RADIANS(:longitude)) + " +
            "       SIN(RADIANS(:latitude)) * SIN(RADIANS(gb.latitude))) <= 5 " +
            "AND gb.category = :category " +
            "ORDER BY gb.createdAt DESC")
    Page<GroupBuying> findByLocationWithin5kmAndCategoryOrderByCreatedAtDesc(@Param("latitude") double latitude,
                                                                             @Param("longitude") double longitude,
                                                                             @Param("category") Category category, PageRequest pageRequest);

}

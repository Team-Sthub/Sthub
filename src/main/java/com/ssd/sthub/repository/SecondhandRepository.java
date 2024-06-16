package com.ssd.sthub.repository;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Secondhand;
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
public interface SecondhandRepository extends JpaRepository<Secondhand, Long> {
    Secondhand save(Secondhand secondhand);

    void deleteById(Long secondhandId);

    Optional<Secondhand> findById(Long secondhandId);
    List<Secondhand> findAllByMemberId(Long memberId);
    List<Secondhand> findTop4ByMemberIdOrderByCreatedAtDesc(Long memberId);

    Page<Secondhand> findAllByMemberId(Long memberId, PageRequest pageRequest);
    Page<Secondhand> findAllByMemberIdAndStatus(Long memberId, String status, PageRequest pageRequest);

    Page<Secondhand> findAllByCategoryOrderByCreatedAtDesc(Category category, PageRequest pageRequest);
    Page<Secondhand> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);

    // 중고거래 키워드 검색
    List<Secondhand> findAllByTitleContainingOrderByCreatedAtDesc(String title);

    void deleteAllByStatus(String status);
}

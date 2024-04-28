package com.ssd.sthub.repository;

import com.ssd.sthub.domain.GComment;
import com.ssd.sthub.domain.GroupBuying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GCommentRepository extends JpaRepository<GComment, Long> {

    List<GComment> findAllByGroupBuying(GroupBuying groupBuying);
}

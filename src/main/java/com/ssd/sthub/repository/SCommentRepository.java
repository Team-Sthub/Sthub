package com.ssd.sthub.repository;

import com.ssd.sthub.domain.SComment;
import com.ssd.sthub.domain.Secondhand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SCommentRepository extends JpaRepository<SComment, Long> {
    public List<SComment> findAllBySecondhand(Secondhand secondhand);
}

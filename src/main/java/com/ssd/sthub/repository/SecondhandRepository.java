package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.domain.enumerate.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecondhandRepository extends JpaRepository<Secondhand, Long> {
    Secondhand save(Secondhand secondhand);

    void deleteById(Long secondhandId);

    Optional<Secondhand> findById(Long secondhandId);

    Page<Secondhand> findAllByMember(Member member, PageRequest pageRequest);

    Page<Secondhand> findAllByCategoryOrderByCreatedAtDesc(Category category, PageRequest pageRequest);
    Page<Secondhand> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);
}

package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unchecked")
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Complaint save(Complaint complaint);

    /*
    신고 횟수 카운트
     Long countByComplaint(Long id);
     */

    //memberId 찾기?
}

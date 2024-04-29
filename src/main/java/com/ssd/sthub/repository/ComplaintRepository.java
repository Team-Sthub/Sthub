package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Complaint save(Complaint complaint);
    Long countByComplaint(long id);
    //memberId 찾기?

}

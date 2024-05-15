package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Complaint;
import com.ssd.sthub.dto.complaint.ComplaintRepoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Complaint save(Complaint complaint);

    /*
    신고 횟수 카운트
     Long countByComplaint(Long id);
     */

    //memberId 찾기?

    // 신고 내역 조회 (키워드 조회)
    List<Integer>  findComplaintRepoDTOByMemberId(Long memberId);
}

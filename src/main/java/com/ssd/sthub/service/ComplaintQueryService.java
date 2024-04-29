package com.ssd.sthub.service;

import com.ssd.sthub.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintQueryService {
    private final ComplaintRepository complaintRepository;

    // 신고 내역 조회
    

    // 신고 내역 카운트
    public Long countComplaints (Long id) {
        return complaintRepository.countByComplaint(id);
    }
}

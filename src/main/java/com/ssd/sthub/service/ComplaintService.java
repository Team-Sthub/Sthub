package com.ssd.sthub.service;

import com.ssd.sthub.domain.Complaint;
import com.ssd.sthub.repository.ComplaintRepository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplaintService {
    private final ComplaintRepository complaintRepository;

    // 신고 처리
    public Complaint complaint (Complaint complaint) {
        return complaintRepository.save(complaint);
    }

}

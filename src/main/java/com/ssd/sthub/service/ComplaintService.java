package com.ssd.sthub.service;

import com.ssd.sthub.domain.Complaint;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.complaint.ComplaintDTO;
import com.ssd.sthub.repository.ComplaintRepository;
import com.ssd.sthub.repository.GroupBuyingRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final SecondhandRepository secondhandRepository;
    private final GroupBuyingRepository groupBuyingRepository;

    // 중고거래 신고 처리
    public Complaint complaintSecondhand (Long secondhandId, ComplaintDTO request) throws NullPointerException {
        Secondhand secondhand = secondhandRepository.findById(secondhandId).orElseThrow(() -> new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다."));
        Complaint complaint = new Complaint(request, secondhand);
        return complaintRepository.save(complaint);
    }

    // 공동구매 신고 처리
    public Complaint complaintGroupBuying (Long groupBuyingId, ComplaintDTO request) throws NullPointerException {
        GroupBuying groupBuying = groupBuyingRepository.findById(groupBuyingId).orElseThrow(() -> new EntityNotFoundException("해당 공동구매 게시글을 찾을 수 없습니다."));
        Complaint complaint = new Complaint(request, groupBuying);
        return complaintRepository.save(complaint);
    }

    // 신고 내역 조회


    // 신고 내역 카운트
    /*
    public Long countComplaints (Long id) {
        return complaintRepository.countByComplaint(id);
    }
    */

}

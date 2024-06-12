package com.ssd.sthub.service;

import com.ssd.sthub.domain.Complaint;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.complaint.ComplaintDTO;
import com.ssd.sthub.repository.ComplaintRepository;
import com.ssd.sthub.repository.GroupBuyingRepository;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final SecondhandRepository secondhandRepository;
    private final GroupBuyingRepository groupBuyingRepository;
    public final MemberRepository memberRepository;

    // 중고거래 신고 처리
    public Complaint complaintSecondhand (Long secondhandId, ComplaintDTO.Request request) throws NullPointerException {
        Optional<Secondhand> secondhandOptional = secondhandRepository.findById(secondhandId);
        if (secondhandOptional.isPresent()) {
            Secondhand secondhand = secondhandOptional.get();
            Complaint complaint = new Complaint(request, secondhandOptional, Optional.empty());
            return complaintRepository.save(complaint);
        } else {
            throw new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다.");
        }
    }

    // 공동구매 신고 처리
    public Complaint complaintGroupBuying(Long groupBuyingId, ComplaintDTO.Request request) throws EntityNotFoundException {
        Optional<GroupBuying> groupBuyingOptional = groupBuyingRepository.findById(groupBuyingId);
        if (groupBuyingOptional.isPresent()) {
            GroupBuying groupBuying = groupBuyingOptional.get();
            Complaint complaint = new Complaint(request, Optional.empty(), groupBuyingOptional);
            return complaintRepository.save(complaint);
        } else {
            throw new EntityNotFoundException("해당 공동구매 게시글을 찾을 수 없습니다.");
        }
    }


    // 신고 내역 조회
    public List<Integer> getTags(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        List<Integer> trueIndexes = complaintRepository.findComplaintRepoDTOByMemberId(memberId);
        return trueIndexes;
    }


    // 신고 내역 카운트
    /*
    public Long countComplaints (Long id) {
        return complaintRepository.countByComplaint(id);
    }
    */

}

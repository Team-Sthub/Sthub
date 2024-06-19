package com.ssd.sthub.service;

import com.ssd.sthub.domain.*;
import com.ssd.sthub.dto.complaint.ComplaintDTO;
import com.ssd.sthub.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintRepositoryImpl complaintRepositoryImpl;
    private final SecondhandRepository secondhandRepository;
    private final GroupBuyingRepository groupBuyingRepository;
    public final MemberRepository memberRepository;

    // 중고거래 신고 처리
    public Complaint complaintSecondhand (Long secondhandId, ComplaintDTO.Request request) throws NullPointerException {
        Optional<Secondhand> secondhandOptional = secondhandRepository.findById(secondhandId);

        if(secondhandOptional.isEmpty())
            throw new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다.");

        Secondhand secondhand = secondhandOptional.get();
        Complaint complaint = new Complaint(request, secondhandOptional, Optional.empty());
        complaint = complaintRepository.save(complaint);

        secondhand.getComplaintList().add(complaint);
        secondhand.updateStatus("신고 누적"); // 신고 횟수 체크 및 상태 업데이트

        secondhandRepository.save(secondhand);
        return complaint;
    }

    // 공동구매 신고 처리
    public Complaint complaintGroupBuying(Long groupBuyingId, ComplaintDTO.Request request) throws EntityNotFoundException {
        Optional<GroupBuying> groupBuyingOptional = groupBuyingRepository.findById(groupBuyingId);

        if(groupBuyingOptional.isEmpty())
            throw new EntityNotFoundException("해당 공동구매 게시글을 찾을 수 없습니다.");

        GroupBuying groupBuying = groupBuyingOptional.get();
        Complaint complaint = new Complaint(request, Optional.empty(), groupBuyingOptional);
        complaint = complaintRepository.save(complaint);

        groupBuying.getComplaintList().add(complaint);
        groupBuying.updateStatus();

        groupBuyingRepository.save(groupBuying);
        return complaint;
    }

    // 신고 내역 조회
    public List<String> getTags(Long memberId) {
        List<Integer> tagIndexes = complaintRepositoryImpl.findComplaintRepoDTOByMemberId(memberId);
        Set<String> uniqueTags = new HashSet<>();

        for (Integer index : tagIndexes) {
            uniqueTags.add(convertIndexToTag(index));
        }

        return new ArrayList<>(uniqueTags);
    }


    private String convertIndexToTag(int index) {
        switch (index) {
            case 0:
                return "# 광고성 콘텐츠예요 ❌";
            case 1:
                return "# 상품 정보 부정확 🔗";
            case 2:
                return "# 상품 설명과 일치하지 않아요 💣";
            case 3:
                return "# 안전거래를 거부해요 🚫";
            case 4:
                return "# 사기가 의심돼요 🤑";
            case 5:
                return "# 거래금지 품목으로 판단 돼요 🚬";
            case 6:
                return "# 전문업자 같아요 🫵";
            case 7:
                return "# 거래 중 분쟁이 발생했어요 😡";
            case 8:
                return "# 연락이 잘 안돼요 🙅";
            // 필요한 경우 더 많은 태그를 추가
            default:
                return "#알 수 없는 태그";
        }
    }

    // 신고 내역 카운트
    /*
    public Long countComplaints (Long id) {
        return complaintRepository.countByComplaint(id);
    }
    */

}

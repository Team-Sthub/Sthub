package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SecondhandService {
    private final SecondhandRepository secondhandRepository;
    private final MemberRepository memberRepository;

    // 중고거래 게시글 작성
    public Secondhand createSecondhand(Long memberId, SecondhandDTO.PostRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        Secondhand secondhand = Secondhand.builder()
                .member(member)
                .title(request.getTitle())
                .category(request.getCategory())
                .product(request.getProduct())
                .price(request.getPrice())
                .type(request.getType())
                .place(request.getPlace())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .build();

        return secondhandRepository.save(secondhand);
    }

    // 중고거래 게시글 수정
    public Secondhand updateSecondhand(Long memberId, SecondhandDTO.PatchRequest request) throws BadRequestException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        Secondhand secondhand = secondhandRepository.findById(request.getSecondhandId())
                .orElseThrow(() -> new EntityNotFoundException("중고거래 게시글 조회에 실패했습니다."));

        if(!secondhand.getMember().getId().equals(memberId))
            throw new BadRequestException("작성자만 삭제할 수 있습니다.");

        secondhand.update(request);
        return secondhandRepository.save(secondhand);
    }

    // 중고거래 게시글 삭제
    public String deleteSecondhand(Long memberId, Long secondhandId) throws BadRequestException {
        Secondhand secondhand = secondhandRepository.findById(secondhandId)
                .orElseThrow(() -> new EntityNotFoundException("중고거래 게시글 조회에 실패했습니다."));

        if(!secondhand.getMember().getId().equals(memberId))
            throw new BadRequestException("작성자만 삭제할 수 있습니다.");
        secondhandRepository.deleteById(secondhandId);
        return "delete success";
    }

    // 중고거래 게시글 상세 조회
    public Secondhand getSecondhand(Long secondhandId) {
        return secondhandRepository.findById(secondhandId)
                .orElseThrow(() -> new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다."));
    }

    // 중고거래 게시글 전체 조회
    public Page<Secondhand> getSecondhands(Category category, int pageNum) throws BadRequestException {
        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        Page<Secondhand> secondhands;

        if (category == Category.ALL)
            secondhands = secondhandRepository.findAllByOrderByCreatedAtDesc(pageRequest);
        else {
            secondhands = secondhandRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageRequest);
        }

        if(secondhands == null || secondhands.isEmpty())
            throw new BadRequestException("작성된 게시글이 없습니다.");
        return secondhands;
    }

    // 중고거래 판매내역 조회
    public Page<Secondhand> getSellingSecondhands(Long memberId, int pageNum) throws BadRequestException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        Page<Secondhand> secondhands = secondhandRepository.findAllByMember(member, pageRequest);

        if(secondhands == null || secondhands.isEmpty())
            throw new BadRequestException("작성된 게시글이 없습니다.");
        return secondhands;
    }


}

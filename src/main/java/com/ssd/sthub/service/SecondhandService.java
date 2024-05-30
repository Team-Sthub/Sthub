package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.SImage;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.SCommentRepository;
import com.ssd.sthub.repository.SImageRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SecondhandService {
    private final SecondhandRepository secondhandRepository;
    private final MemberRepository memberRepository;
    private final SImageRepository sImageRepository;
    private final SCommentRepository sCommentRepository;
    private final AWSS3SService awss3SService;

    // 중고거래 게시글 작성
    public SecondhandDTO.DetailResponse createSecondhand(Long memberId, List<String> imgUrls, SecondhandDTO.PostRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        Secondhand secondhand = Secondhand.builder()
                .member(member)
                .request(request)
                .build();
        Secondhand newSecondhand = secondhandRepository.save(secondhand);

        if(imgUrls != null && !imgUrls.isEmpty()) {
            for (String imgUrl : imgUrls) {
                SImage sImage = SImage.builder()
                        .path(imgUrl)
                        .secondhand(secondhand)
                        .build();
                sImageRepository.save(sImage);
            }
        }

        List<SImage> sImages = sImageRepository.findAllBySecondhand(secondhand);
        return new SecondhandDTO.DetailResponse(newSecondhand, sImages, null);
    }

    // 중고거래 게시글 수정
    public SecondhandDTO.DetailResponse updateSecondhand(Long memberId, List<String> imgUrls, SecondhandDTO.PatchRequest request) throws BadRequestException {
        Secondhand secondhand = secondhandRepository.findById(request.getSecondhandId())
                .orElseThrow(() -> new EntityNotFoundException("중고거래 게시글 조회에 실패했습니다."));

        if(!secondhand.getMember().getId().equals(memberId))
            throw new BadRequestException("작성자만 삭제할 수 있습니다.");

        if(imgUrls != null && !imgUrls.isEmpty()) {
            for (String imgUrl : imgUrls) {
                SImage sImage = SImage.builder()
                        .path(imgUrl)
                        .secondhand(secondhand)
                        .build();
                sImageRepository.save(sImage);
            }
        }

        secondhand.update(request);
        secondhand = secondhandRepository.save(secondhand);
        return new SecondhandDTO.DetailResponse(secondhand, secondhand.getImageList(), secondhand.getCommentList());
    }

    // 중고거래 게시글 삭제
    public String deleteSecondhand(Long memberId, Long secondhandId) throws BadRequestException {
        Secondhand secondhand = secondhandRepository.findById(secondhandId)
                .orElseThrow(() -> new EntityNotFoundException("중고거래 게시글 조회에 실패했습니다."));

        if(!secondhand.getMember().getId().equals(memberId))
            throw new BadRequestException("작성자만 삭제할 수 있습니다.");

        awss3SService.deleteImages(
                sImageRepository.findAllBySecondhand(secondhand)
                        .stream().map(SImage::getPath)
                        .collect(Collectors.toList())
        );
//        sImageRepository.deleteAllBySecondhand(secondhand);
//        sCommentRepository.deleteAllBySecondhand(secondhand);
        secondhandRepository.deleteById(secondhandId);
        return "delete success";
    }

    // 중고거래 게시글 상세 조회
    public SecondhandDTO.DetailResponse getSecondhand(Long secondhandId) {
        Secondhand secondhand = secondhandRepository.findById(secondhandId)
                .orElseThrow(() -> new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다."));
        return new SecondhandDTO.DetailResponse(secondhand, secondhand.getImageList(), secondhand.getCommentList());
    }

    // 중고거래 게시글 전체 조회
    public List<SecondhandDTO.ListResponse> getSecondhands(Category category, int pageNum) throws BadRequestException {
        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        Page<Secondhand> secondhands;

        if (category == Category.ALL)
            secondhands = secondhandRepository.findAllByOrderByCreatedAtDesc(pageRequest);
        else {
            secondhands = secondhandRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageRequest);
        }

        if(secondhands == null || secondhands.isEmpty())
            throw new BadRequestException("작성된 게시글이 없습니다.");

        return secondhands.stream()
                .map(s -> new SecondhandDTO.ListResponse(s, s.getImageList(), category, secondhands.getTotalPages(), pageNum + 1))
                .collect(Collectors.toList());
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

    // 중고거래 글 이미지 조회
    public List<String> getImageUrls(Long secondhandId) {
        return sImageRepository.findAllBySecondhandId(secondhandId)
                .stream()
                .map(SImage::getPath)
                .collect(Collectors.toList());
    }

    // 중고거래 글 이미지 삭제
    public void deleteImages(List<String> imageUrls) {
        for(String image : imageUrls) {
            sImageRepository.deleteByPath(image);
        }
    }
}

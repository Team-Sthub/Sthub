package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.SImage;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.SImageRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SecondhandService {
    private final SecondhandRepository secondhandRepository;
    private final MemberRepository memberRepository;
    private final SImageRepository sImageRepository;
    private final AWSS3SService awss3SService;

    // 중고거래 게시글 작성
    public SecondhandDTO.Response createSecondhand(Long memberId, List<String> imgUrls, SecondhandDTO.PostRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        Secondhand secondhand = Secondhand.builder()
                .member(member)
                .request(request)
                .build();
        Secondhand newSecondhand = secondhandRepository.save(secondhand);

        for(String imgUrl : imgUrls) {
            SImage sImage = SImage.builder()
                    .path(imgUrl)
                    .secondhand(secondhand)
                    .build();
            sImageRepository.save(sImage);
        }

        return new SecondhandDTO.Response(newSecondhand, sImageRepository.findAllBySecondhand(secondhand));
    }

    // 중고거래 게시글 수정
    public SecondhandDTO.Response updateSecondhand(Long memberId, List<String> imgUrls, SecondhandDTO.PatchRequest request) throws BadRequestException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        Secondhand secondhand = secondhandRepository.findById(request.getSecondhandId())
                .orElseThrow(() -> new EntityNotFoundException("중고거래 게시글 조회에 실패했습니다."));

        if(!secondhand.getMember().getId().equals(memberId))
            throw new BadRequestException("작성자만 삭제할 수 있습니다.");

        awss3SService.deleteImages(
                sImageRepository.findAllBySecondhand(secondhand)
                        .stream().map(SImage::getPath)
                        .collect(Collectors.toList())
        );
        sImageRepository.deleteAllBySecondhand(secondhand);

        secondhand.update(request);
        secondhand = secondhandRepository.save(secondhand);

        for(String imgUrl : imgUrls) {
            SImage sImage = SImage.builder()
                    .path(imgUrl)
                    .secondhand(secondhand)
                    .build();
            sImageRepository.save(sImage);
        }

        return new SecondhandDTO.Response(secondhand, sImageRepository.findAllBySecondhand(secondhand));
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
        sImageRepository.deleteAllBySecondhand(secondhand);
        secondhandRepository.deleteById(secondhandId);
        return "delete success";
    }

    // 중고거래 게시글 상세 조회
    public SecondhandDTO.Response getSecondhand(Long secondhandId) {
        Secondhand secondhand = secondhandRepository.findById(secondhandId)
                .orElseThrow(() -> new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다."));
        List<SImage> sImages = sImageRepository.findAllBySecondhand(secondhand);
        return new SecondhandDTO.Response(secondhand, sImages);
    }

    // 중고거래 게시글 전체 조회 (이미지도 가져오도록 수정 필요)
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

    // 중고거래 글 이미지 조회
    public List<String> getImageUrls(Long secondhandId) {
        return sImageRepository.findAllBySecondhandId(secondhandId)
                .stream()
                .map(SImage::getPath)
                .collect(Collectors.toList());
    }
}

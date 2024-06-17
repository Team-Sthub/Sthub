package com.ssd.sthub.service;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.SImage;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.SCommentRepository;
import com.ssd.sthub.repository.SImageRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import com.ssd.sthub.util.GoogleMapUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final GoogleMapUtil googleMapUtil;

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
            throw new BadRequestException("작성자만 수정할 수 있습니다.");

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

    // 중고거래 거래 최종 방식 선택
    public SecondhandDTO.DetailResponse checkSecondhand(Long memberId, SecondhandDTO.CheckRequest request) throws BadRequestException {
        Secondhand secondhand = secondhandRepository.findById(request.getSecondhandId())
                .orElseThrow(() -> new EntityNotFoundException("중고거래 게시글 조회에 실패했습니다."));


        if(!secondhand.getMember().getId().equals(memberId))
            throw new BadRequestException("작성자만 수정할 수 있습니다.");

        secondhand.checkTransaction(request);
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
    public List<SecondhandDTO.ListResponse> getSellingSecondhands(String status, Long memberId, int pageNum) throws BadRequestException {
        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        Page<Secondhand> secondhands;

        if(status.equals("ALL")) {
            secondhands = secondhandRepository.findAllByMemberId(memberId, pageRequest);
        } else {
            status = (status.equals("COMPLETE")) ? "거래완료" : "예약중";
            secondhands = secondhandRepository.findAllByMemberIdAndStatus(memberId, status, pageRequest);
        }

        return secondhands.stream()
                .map(s -> new SecondhandDTO.ListResponse(s, s.getImageList(), s.getCategory(), secondhands.getTotalPages(), pageNum + 1))
                .collect(Collectors.toList());
    }

    // 중고거래 상위4개 조회
    public List<SecondhandDTO.Top4ListResponse> getTop4Items(Long memberId) throws BadRequestException {
        List<Secondhand> allItems = secondhandRepository.findTop4ByMemberIdOrderByCreatedAtDesc(memberId);
        return allItems.stream()
                .map(s -> new SecondhandDTO.Top4ListResponse(s, s.getImageList()))
                .collect(Collectors.toList());
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

    public void deleteSecondhandByStatus() {
        secondhandRepository.deleteAllByStatus("신고 누적");
    }

    // 내 근처 중고거래 조회
    public List<SecondhandDTO.ListResponse> getAllAroundSecondhand(Long memberId, Category category, int pageNum) throws BadRequestException {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("회원 조회에 실패했습니다.")
        );

        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        Page<Secondhand> secondhands;

        // 지오코딩
        List<Secondhand> secondhandList = secondhandRepository.findByPlaceIsNotNull();      // 장소 필드에 값이 있는 것만
        for (Secondhand secondhand : secondhandList) {
            Map<String, String> address = googleMapUtil.getGeoDataByAddress(secondhand.getPlace());
            if(address != null) {
                Double latitude = Double.valueOf(address.get("lat"));
                Double longitude = Double.valueOf(address.get("lng"));

                if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
                    throw new IllegalArgumentException("Invalid latitude or longitude values.");
                }
                secondhand.setLatitude(latitude);
                secondhand.setLongitude(longitude);
                secondhandRepository.save(secondhand);
                log.info("Secondhand: " + secondhand);
            }
        }

        // 2. 로그인한 회원의 위치를 기준으로 반경 5km 내에 있는 중고거래 데이터 검색
        if (category == Category.ALL){
            secondhands = secondhandRepository.findByLocationWithin5kmOrderByCreatedAtDesc(member.getLatitude(), member.getLongitude(), pageRequest);
            log.info("secondhands: " + secondhands);
        } else {
            secondhands = secondhandRepository.findByLocationWithin5kmAndCategoryOrderByCreatedAtDesc(member.getLatitude(), member.getLongitude(), category, pageRequest);
            log.info("secondhands: " + secondhands);
        }

        if(secondhands == null || secondhands.isEmpty())
            throw new BadRequestException("작성된 게시글이 없습니다.");

        return secondhands.stream()
                .map(g -> new SecondhandDTO.ListResponse(g, g.getImageList(), category, secondhands.getTotalPages(), pageNum + 1))
                .collect(Collectors.toList());
    }
}

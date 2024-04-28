package com.ssd.sthub.service;

import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecondhandQueryService {
    private final SecondhandRepository secondhandRepository;

    // 중고거래 게시글 상세 조회
    public Secondhand getSecondhand(Long secondhandId) {
        return secondhandRepository.findById(secondhandId)
                .orElseThrow(() -> new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다."));
    }

    // 중고거래 게시글 전체 조회
    public Page<Secondhand> getSecondhands(int pageNum, Category category) throws BadRequestException {
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
}

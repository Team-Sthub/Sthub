package com.ssd.sthub.service;

import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SecondhandService {
    private final SecondhandRepository secondhandRepository;

    // 중고거래 게시글 작성
    public Secondhand createSecondhand(Secondhand secondhand) {
        return secondhandRepository.save(secondhand);
    }

    // 중고거래 게시글 삭제
    public void deleteSecondhand(Long secondhandId) {
        secondhandRepository.deleteById(secondhandId);
    }
}

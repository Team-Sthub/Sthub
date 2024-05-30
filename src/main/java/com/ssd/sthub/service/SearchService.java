package com.ssd.sthub.service;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.search.SearchDTO;
import com.ssd.sthub.repository.GroupBuyingRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SecondhandRepository secondhandRepository;
    private final GroupBuyingRepository groupBuyingRepository;

    // 중고거래 + 공동구매 동시 검색
//    public List<SearchDTO> getSearchList(String title) {
//        List<Secondhand> secondhands = secondhandRepository.findAllByTitleOrderByCreatedAt(title); //중고거래 검색 결과
//        List<GroupBuying> groupBuyings = groupBuyingRepository.findAllByTitleOrderByCreatedAt(title); // 공동구매 검색 결과
//
//        List<SearchDTO> mergedLists = null;
//        secondhands.forEach(result -> mergedLists.add(
//                new SearchDTO(result.getId(),
//                        result.getCreatedAt(),
//                        result.getImageList().get(0).getPath(),
//                        result.getTitle(),
//                        result.getMember().getNickname(),
//                        result.getPrice())));
//
//        groupBuyings.forEach(result -> mergedLists.add(
//                new SearchDTO(result.getId(),
//                        result.getCreatedAt(),
//                        result.getImageList().get(0).getPath(),
//                        result.getTitle(),
//                        result.getMember().getNickname(),
//                        result.getPrice())));
//
//        return mergedLists.stream()
//                .sorted(Comparator.comparing(SearchDTO::getCreateAt))
//                .collect(Collectors.toList());
//    }
}

package com.ssd.sthub.service;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.search.SearchDTO;
import com.ssd.sthub.repository.GroupBuyingRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final SecondhandRepository secondhandRepository;
    private final GroupBuyingRepository groupBuyingRepository;

    // 중고거래 + 공동구매 동시 검색
    public SearchDTO.ListResponse getSearchList(String keyword, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 10);

        List<Secondhand> secondhands = secondhandRepository.findAllByTitleContainingOrderByCreatedAtDesc(keyword);
        List<GroupBuying> groupBuyings = groupBuyingRepository.findAllByTitleContainingOrderByCreatedAtDesc(keyword);

        List<SearchDTO.DetailResponse> mergedLists = new ArrayList<>();

        secondhands.forEach(result -> mergedLists.add(
                new SearchDTO.DetailResponse(
                        result.getId(),
                        result.getCreatedAt(),
                        result.getImageList() == null ? null : result.getImageList().get(0).getPath(),
                        result.getTitle(),
                        result.getMember().getNickname(),
                        result.getPrice(),
                        "secondhand"
                )));

        groupBuyings.forEach(result -> mergedLists.add(
                new SearchDTO.DetailResponse(
                        result.getId(),
                        result.getCreatedAt(),
                        result.getImageList() == null || result.getImageList().isEmpty() ? "" : result.getImageList().get(0).getPath(),
                        result.getTitle(),
                        result.getMember().getNickname(),
                        result.getPrice(),
                        "groupBuying"
                )));

        List<SearchDTO.DetailResponse> sortedList = mergedLists.stream()
                .sorted(Comparator.comparing(SearchDTO.DetailResponse::getCreatedAt).reversed())
                .collect(Collectors.toList());

        int totalElements = sortedList.size();
        int totalPages = (int) Math.ceil((double) totalElements / 10);

        // 페이지 번호가 유효한 범위 내에 있는지 확인
        if (pageNum < 0 || pageNum >= totalPages) {
            return new SearchDTO.ListResponse(Collections.emptyList(), keyword, totalPages, pageNum + 1);
        }

        int start = Math.min(pageNum * 10, totalElements);
        int end = Math.min((pageNum + 1) * 10, totalElements);

        List<SearchDTO.DetailResponse> pagedList = sortedList.subList(start, end);

        return new SearchDTO.ListResponse(pagedList, keyword, totalPages, pageNum + 1);
    }

}

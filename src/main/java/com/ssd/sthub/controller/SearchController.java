package com.ssd.sthub.controller;

import com.ssd.sthub.dto.search.SearchDTO;
import com.ssd.sthub.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
@Slf4j
public class SearchController {
    private final SearchService searchService;

    // 중고거래, 공동구매 동시 검색
    @GetMapping("")
    public ModelAndView getSearchList(@RequestParam("searchValue") String keyword, @RequestParam int pageNum) {
        SearchDTO.ListResponse search = searchService.getSearchList(keyword, pageNum - 1);
        log.info(search.toString());
        return new ModelAndView("thyme/search/list", "searchList", search);
    }
}

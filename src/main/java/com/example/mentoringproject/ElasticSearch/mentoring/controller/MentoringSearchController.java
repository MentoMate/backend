package com.example.mentoringproject.ElasticSearch.mentoring.controller;

import com.example.mentoringproject.ElasticSearch.mentoring.model.MentoringSearchDto;
import com.example.mentoringproject.ElasticSearch.mentoring.service.MentoringSearchService;
import com.example.mentoringproject.ElasticSearch.util.SearchResult;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "멘토링 - 검색", description = "멘토링 - 검색 API")
@RestController
@RequestMapping("/mentoring")
@RequiredArgsConstructor
public class MentoringSearchController {

  private final MentoringSearchService mentoringSearchService;

  @Operation(summary = "멘토링 - 검색 api", description = "멘토링 - 검색 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 - 검색 성공", content =
      @Content(schema = @Schema(implementation = MentoringSearchDto.class)))
  })
  @GetMapping("search")
  public ResponseEntity<SearchResult<MentoringSearchDto>> searchMentoring(
      @RequestParam(required = false) String searchType,
      @RequestParam(required = false) String searchText,
      @RequestParam(required = true) String sortBy,
      @RequestParam(required = false) String searchCategory,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "8") int pageSize) {

    List<MentoringSearchDto> mentoringSearchDtoList = new ArrayList<>();

    String email = SpringSecurityUtil.getLoginEmail();

    if (searchType != null) {
      if ("title".equals(searchType)) {
        mentoringSearchDtoList = mentoringSearchService.searchTitleAndCategory(searchText,
            searchCategory, email);
      } else if ("writer".equals(searchType)) {
        mentoringSearchDtoList = mentoringSearchService.searchWriterAndCategory(searchText,
            searchCategory,email);
      } else {
        return ResponseEntity.badRequest().build();
      }
    } else {
      mentoringSearchDtoList = mentoringSearchService.searchAll(email);
    }

    // 평점순, 최신순, 금액순 정렬
    if ("rating".equals(sortBy)) {
      mentoringSearchDtoList.sort(
          Comparator.comparing(MentoringSearchDto::getRating).reversed());
    } else if ("latest".equals(sortBy)) {
      mentoringSearchDtoList.sort(Comparator.comparing(MentoringSearchDto::getId).reversed());
    } else if ("cost".equals(sortBy)) {
      mentoringSearchDtoList.sort(Comparator.comparing(MentoringSearchDto::getAmount));
    }
    // 전체 페이지 수 계산
    int totalItems = mentoringSearchDtoList.size();
    int totalPages = (int) Math.ceil((double) totalItems / pageSize);

    // 페이징 처리
    int start = (page - 1) * pageSize;
    int end = Math.min(start + pageSize, totalItems);

    List<MentoringSearchDto> pagedResult = mentoringSearchDtoList.subList(start, end);

    SearchResult<MentoringSearchDto> searchResult = new SearchResult<>(totalPages, pagedResult);

    return ResponseEntity.ok(searchResult);
  }
}

package com.example.mentoringproject.ElasticSearch.mentor.controller;


import com.example.mentoringproject.ElasticSearch.mentor.model.MentorSearchDto;
import com.example.mentoringproject.ElasticSearch.mentor.service.MentorSearchService;
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

@Tag(name = "멘토 - 검색", description = "멘토 - 검색 API")
@RestController
@RequestMapping("/mentor")
@RequiredArgsConstructor
public class MentorSearchController {

  private final MentorSearchService mentorSearchService;

  @Operation(summary = "멘토 - 검색 api", description = "멘토 - 검색 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토 - 검색 성공", content =
      @Content(schema = @Schema(implementation = MentorSearchDto.class)))
  })
  @GetMapping("search")
  public ResponseEntity<SearchResult<MentorSearchDto>> searchMentor(
      @RequestParam(required = false) String searchCategory,
      @RequestParam(required = false) String searchText,
      @RequestParam(required = true) String sortBy,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "8") int pageSize) {

    List<MentorSearchDto> mentorSearchDtoList = new ArrayList<>();
    String email = SpringSecurityUtil.getLoginEmail();

    if (searchText != null) {
      mentorSearchDtoList = mentorSearchService.searchTextAndCategory(searchText, searchCategory, email);
    } else {
      if (searchCategory != null) {
        mentorSearchDtoList = mentorSearchService.searchCategory(searchCategory, email);
      } else {
        mentorSearchDtoList = mentorSearchService.searchAll(email);
      }
    }

    // 평점순, 최신순, 팔로우순 정렬
    if ("rating".equals(sortBy)) {
      mentorSearchDtoList.sort(
          Comparator.comparing(MentorSearchDto::getRating).reversed());
    } else if ("latest".equals(sortBy)) {
      mentorSearchDtoList.sort(Comparator.comparing(MentorSearchDto::getId).reversed());
    } else if ("follower".equals(sortBy)) {
      mentorSearchDtoList.sort(Comparator.comparing(MentorSearchDto::getFollowers).reversed());
    }
    // 전체 페이지 수 계산
    int totalItems = mentorSearchDtoList.size();
    int totalPages = (int) Math.ceil((double) totalItems / pageSize);

    // 페이징 처리
    int start = (page - 1) * pageSize;
    int end = Math.min(start + pageSize, mentorSearchDtoList.size());

    List<MentorSearchDto> pagedResult = mentorSearchDtoList.subList(start, end);

    SearchResult<MentorSearchDto> searchResult = new SearchResult<>(totalPages, pagedResult);

    return ResponseEntity.ok(searchResult);
  }
}
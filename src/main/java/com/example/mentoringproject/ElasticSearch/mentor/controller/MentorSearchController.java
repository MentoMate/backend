package com.example.mentoringproject.ElasticSearch.mentor.controller;


import com.example.mentoringproject.ElasticSearch.mentor.model.MentorSearchDto;
import com.example.mentoringproject.ElasticSearch.mentor.service.MentorSearchService;
import com.example.mentoringproject.ElasticSearch.mentoring.model.MentoringSearchDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
  public ResponseEntity<List<MentorSearchDto>> searchMentor(
      @RequestParam(required = false) String searchCategory,
      @RequestParam(required = false) String searchText,
      @RequestParam(required = true) String sortBy,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "8") int pageSize) {

    List<MentorSearchDto> mentorSearchDtoList = new ArrayList<>();

    if (searchCategory != null) {
      mentorSearchDtoList = mentorSearchService.searchCategory(searchText,
          searchCategory);
    } else {
      mentorSearchDtoList = mentorSearchService.searchAll();
    }

    // 평점순, 최신순 정렬
    if ("rating".equals(sortBy)) {
      mentorSearchDtoList.sort(
          Comparator.comparing(MentorSearchDto::getRating).reversed());
    } else if ("latest".equals(sortBy)) {
      mentorSearchDtoList.sort(Comparator.comparing(MentorSearchDto::getId).reversed());
    }
    // 페이징 처리
    int start = (page - 1) * pageSize;
    int end = Math.min(start + pageSize, mentorSearchDtoList.size());

    List<MentorSearchDto> pagedResult = mentorSearchDtoList.subList(start, end);

    return ResponseEntity.ok(pagedResult);
  }
}
package com.example.mentoringproject.ElasticSearch.mentor.controller;


import com.example.mentoringproject.ElasticSearch.mentor.model.MentorSearchDto;
import com.example.mentoringproject.ElasticSearch.mentor.service.MentorSearchService;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mentor")
@RequiredArgsConstructor
public class MentorSearchController {

  private final MentorSearchService mentorSearchService;

  @PostMapping("search")
  public ResponseEntity<List<MentorSearchDto>> searchMentor(
      @RequestParam(required = true) String searchCategory,
      @RequestParam(required = true) String searchText,
      @RequestParam(required = true) String sortBy) {

    List<MentorSearchDto> mentorSearchDtoList = mentorSearchService.searchCategory(searchText,
        searchCategory);

    // 평점순, 최신순 정렬
    if ("rating".equals(sortBy)) {
      mentorSearchDtoList.sort(
          Comparator.comparing(MentorSearchDto::getRating).reversed());
    } else if ("latest".equals(sortBy)) {
      mentorSearchDtoList.sort(Comparator.comparing(MentorSearchDto::getId).reversed());
    }

    return ResponseEntity.ok(mentorSearchDtoList);
  }
}
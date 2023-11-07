package com.example.mentoringproject.ElasticSearch.post.controller;

import com.example.mentoringproject.ElasticSearch.post.model.PostSearchDto;
import com.example.mentoringproject.ElasticSearch.post.service.PostSearchService;
import com.example.mentoringproject.post.post.service.PostService;
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

@Tag(name = "게시판 - 검색", description = "게시판 - 검색 API")
@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostSearchController {

  private final PostService postService;
  private final PostSearchService postSearchService;


  @Operation(summary = "게시판 - 검색 api", description = "게시판 - 검색 api", responses = {
      @ApiResponse(responseCode = "200", description = "게시판 - 검색 성공", content =
      @Content(schema = @Schema(implementation = PostSearchDto.class)))
  })
  @GetMapping("/search")
  public ResponseEntity<List<PostSearchDto>> searchPost(
      @RequestParam(required = false) String searchType,
      @RequestParam(required = false) String searchText,
      @RequestParam(required = true) String searchCategory,
      @RequestParam(required = true) String sortBy,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "8") int pageSize) {

    List<PostSearchDto> postSearchDtoList = new ArrayList<>();

    if (searchType != null) {
      if ("title".equals(searchType)) {
        postSearchDtoList = postSearchService.searchTitleAndCategory(searchText,
            searchCategory);
      } else if ("writer".equals(searchType)) {
        postSearchDtoList = postSearchService.searchWriterAndCategory(searchText,
            searchCategory);
      } else {
        return ResponseEntity.badRequest().build();
      }
    } else {
      postSearchDtoList = postSearchService.searchCategory(searchCategory);
    }

    // 인기순, 최신순 정렬
    if ("likes".equals(sortBy)) {
      postSearchDtoList.sort(
          Comparator.comparing(PostSearchDto::getPostLikesCount).reversed());
    } else if ("latest".equals(sortBy)) {
      postSearchDtoList.sort(Comparator.comparing(PostSearchDto::getId).reversed());
    }

    // 페이징 처리
    int start = (page - 1) * pageSize;
    int end = Math.min(start + pageSize, postSearchDtoList.size());

    List<PostSearchDto> pagedResult = postSearchDtoList.subList(start, end);

    return ResponseEntity.ok(pagedResult);

  }

}

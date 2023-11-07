package com.example.mentoringproject.common.s3.Controller;

import com.example.mentoringproject.ElasticSearch.mentor.model.MentorSearchDto;
import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지 저장", description = "이미지 저장 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class S3Controller {
  private final S3Service s3Service;
  private static final String UPLOAD_FILETYPE = "img";


  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> upload(
      @RequestParam("key") String key,
      @RequestPart(name = "img", required = false) List<MultipartFile> multipartFiles
  ){
    return ResponseEntity.ok(s3Service.upload(multipartFiles, key,UPLOAD_FILETYPE).get(0).getUploadUrl());
  }


  @GetMapping
  public ResponseEntity<String> fileNameChk(
      @RequestParam("key") String key
  ){
    s3Service.folderChk(key);
    return ResponseEntity.ok().build();
  }


}

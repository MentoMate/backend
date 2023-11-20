package com.example.mentoringproject.common.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.model.S3FileDto;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  private static final Set<String> fileExtensions = new HashSet<>(Arrays.asList(".jpg", ".jpeg", ".png"));
  private static final String VALIDATION_CHECK_EXTENSION = "img";

  public List<S3FileDto> upload(List<MultipartFile> multipartFile, String folderName, String fileType) {
    List<S3FileDto> s3FileDto = new ArrayList<>();

    String filePath = folderName + "/" ;
    String fileName;
    String originalFileName;

    for (MultipartFile file : multipartFile) {
      originalFileName = file.getOriginalFilename();
      if(fileType.equals(VALIDATION_CHECK_EXTENSION)) imageFileExtensionChk(fileNameChk(originalFileName));
      fileName = createFileName(fileNameChk(originalFileName));

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(file.getSize());
      objectMetadata.setContentType(file.getContentType());

      try (InputStream inputStream = file.getInputStream()) {
        amazonS3.putObject(new PutObjectRequest(bucket, filePath+fileName, inputStream, objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
      }
      catch (IOException e) {
        throw new AppException(HttpStatus.BAD_REQUEST,"UPLOAD_ERROR");
      }

      s3FileDto.add(
          S3FileDto.builder()
              .fileName(originalFileName)
              .uploadFolder(filePath)
              .uploadUrl(amazonS3.getUrl(bucket, filePath+fileName).toString())
              .build());
    }

    return s3FileDto;
  }


  public void deleteFile(List<S3FileDto> s3FileDtoList) {
    String keyName;
    for (S3FileDto s3FileDto : s3FileDtoList) {
      keyName = s3FileDto.getUploadFolder() + decode(extractFileName(s3FileDto.getUploadUrl()));
      try {
        if (amazonS3.doesObjectExist(bucket, keyName)) {
          amazonS3.deleteObject(bucket, keyName);
        }
      }
      catch (AmazonS3Exception e) {
        throw new AppException(HttpStatus.BAD_REQUEST, "DELETE_ERROR");
      }
    }
  }

  public void fileClear(String folder, List<String> imgUrl){
    String keyName;

    ObjectListing objectListing = amazonS3.listObjects(bucket,folder);

    List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
    for (S3ObjectSummary objectSummary : objectSummaries) {
      keyName = objectSummary.getKey();
      if (!imgUrl.contains(encode(extractFileName(keyName)))){
        if (amazonS3.doesObjectExist(bucket, keyName)) {
          amazonS3.deleteObject(bucket, keyName);
        }
      }
    }
  }

  public void folderChk(String folder){
    ObjectListing objectListing = amazonS3.listObjects(bucket,folder);
    List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
    if(objectSummaries.size() != 0){
      throw new AppException(HttpStatus.BAD_REQUEST, "해당 폴더는 존재합니다.");
    }
  }

  private void imageFileExtensionChk(String fileName) {
    if (!fileExtensions.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
      throw new AppException(HttpStatus.BAD_REQUEST, "WRONG_IMAGE_FORMAT");
    }
  }
  private String fileNameChk(String fileName){
    if (fileName == null || fileName.length() == 0) throw new AppException(HttpStatus.BAD_REQUEST,"WRONG_FILE_NAME");
    return encode(fileName);
  }
  public String extractFileName(String s3Url) {
    String[] parts = s3Url.split("/");
    return parts[parts.length - 1];
  }
  private String createFileName(String fileName) {
    return  UUID.randomUUID().toString().concat(fileName);
  }
  private String encode(String fileName) {
    try {
      return URLEncoder.encode(fileName, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      return  fileName;
    }
  }

  private String decode(String fileName) {
    try {
      return URLDecoder.decode(fileName, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      return  fileName;
    }
  }
}
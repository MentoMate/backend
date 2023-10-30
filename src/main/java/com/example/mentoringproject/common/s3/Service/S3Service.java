package com.example.mentoringproject.common.s3.Service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.mentoringproject.common.s3.Model.S3FileDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    List<S3FileDto> s3FileDtoList = new ArrayList<>();

    String filePath = folderName + "/" ;
    String fileName;

    for (MultipartFile file : multipartFile) {

      String originalFileName = fileNameChk(file.getOriginalFilename());
      if(fileType.equals(VALIDATION_CHECK_EXTENSION)) imageFileExtensionChk(originalFileName);
      fileName = createFileName(originalFileName);

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(file.getSize());
      objectMetadata.setContentType(file.getContentType());

      try (InputStream inputStream = file.getInputStream()) {
        amazonS3.putObject(new PutObjectRequest(bucket, filePath+fileName, inputStream, objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
      }
      catch (IOException e) {
        throw new RuntimeException(fileType +"_UPLOAD_ERROR");
      }

      s3FileDtoList.add(
          S3FileDto.builder()
              .uploadPath(filePath)
              .uploadName(fileName)
              .uploadUrl(amazonS3.getUrl(bucket, filePath+fileName).toString())
              .build());
    }

    return s3FileDtoList;
  }

  public void deleteFile(List<S3FileDto> s3FileDtoList) {
    String keyName;
    for (S3FileDto s3FileDto : s3FileDtoList) {
      keyName = s3FileDto.getUploadPath() + s3FileDto.getUploadName();
      try {
        if (amazonS3.doesObjectExist(bucket, keyName)) {
          amazonS3.deleteObject(bucket, keyName);
        }
      }
      catch (AmazonS3Exception e) {
        throw new RuntimeException("S3 파일 삭제 중 오류 발생: " + e.getMessage(), e);
      }
      catch (SdkClientException e) {
        throw new RuntimeException("S3 클라이언트 오류: " + e.getMessage(), e);
      }
    }
  }

  private String createFileName(String fileName) {
    return UUID.randomUUID().toString().concat(fileName);
  }

  private void imageFileExtensionChk(String fileName) {
    if (!fileExtensions.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
      throw new RuntimeException("WRONG_IMAGE_FORMAT");
    }
  }
  private String fileNameChk(String fileName){
    if (fileName == null || fileName.length() == 0) throw new RuntimeException("WRONG_INPUT_IMAGE");
    return fileName;
  }
}
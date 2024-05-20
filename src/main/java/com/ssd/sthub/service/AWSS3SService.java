package com.ssd.sthub.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AWSS3SService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    // MultipartFile을 전달받아 File로 전환 후 S3 서버에 파일 업로드
    public List<String> uploadFile(List<MultipartFile> multipartFile) {
        List<String> fileNameList = new ArrayList<>();

        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 fileNameList에 추가
        multipartFile.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }

            fileNameList.add(amazonS3.getUrl(bucket, fileName).toString());
        });

        return fileNameList;
    }

    // S3 서버에서 파일 삭제 (단일)
    public boolean deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        return true;
    }

    // S3 서버에서 파일 삭제 (다중)
    public boolean deleteImages(List<String> fileNames) {
        for(String fileName : fileNames) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        }
        return true;
    }

    // 기존 확장자명을 유지하면서, 식별되는 파일명을 생성
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 확장자 알아내기
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
}

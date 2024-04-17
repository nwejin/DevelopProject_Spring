package com.weatherable.weatherable.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Upload {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveImageFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename().replace(" ", "_");

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        // putObject() : 파일 저장 메소드
        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        // getUrl() : 파일이 저장된 url 반환 (url 로 이동시 해당 파일 오픈)
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

}
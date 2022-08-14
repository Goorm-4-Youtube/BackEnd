package com.goorm.team4.youtubeclone.Service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service implements FileService{

    @Value("${cloud.aws.bucketname}")
    public String BUCKET_NAME;
    private final AmazonS3Client awsS3Client;

    @Override
    public String uploadFile(MultipartFile file){

        var filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());


        var key = UUID.randomUUID().toString() + "." + filenameExtension;

        var metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());


        try {
            awsS3Client.putObject(BUCKET_NAME,key,file.getInputStream(),metadata);

        } catch (IOException ioException) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An Exception occured while uploading");

        }
        awsS3Client.setObjectAcl(BUCKET_NAME,key, CannedAccessControlList.PublicRead);
        return awsS3Client.getResourceUrl(BUCKET_NAME,key);
    }

    public void deleteFile(String url) {
        if (url==null)
        {
            return;
        }
        System.out.println(url);
        AmazonS3URI as3uri = new AmazonS3URI(url);
        System.out.println(as3uri);
        System.out.println(as3uri.getBucket());
        System.out.println(as3uri.getKey());
        awsS3Client.deleteObject(as3uri.getBucket(), as3uri.getKey());
    }
}

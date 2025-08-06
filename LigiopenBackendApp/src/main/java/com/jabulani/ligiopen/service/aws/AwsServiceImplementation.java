package com.jabulani.ligiopen.service.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AwsServiceImplementation implements AwsService {

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public String uploadFile(String bucketName, MultipartFile file) throws AmazonClientException, IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        String contentType = file.getContentType();
        long fileSize = file.getSize();
        InputStream inputStream = file.getInputStream();

        metadata.setContentLength(fileSize);
        metadata.setContentType(contentType);

        // Create a PutObjectRequest with PublicRead ACL
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        // Upload the file
        s3Client.putObject(putObjectRequest);

        log.info("File uploaded to bucket({}): {}", bucketName, fileName);

        return fileName;
    }


    // Method to upload a file to an S3 bucket
    @Override
    public String uploadFiles(
            final String bucketName,
            final MultipartFile[] files
    ) throws AmazonClientException, IOException {
        ObjectMetadata metadata = new ObjectMetadata();

        for(MultipartFile file : files) {
            String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            String contentType = file.getContentType();
            long fileSize = file.getSize();
            InputStream inputStream = file.getInputStream();

            metadata.setContentLength(fileSize);
            metadata.setContentType(contentType);

            s3Client.putObject(bucketName, fileName, inputStream, metadata);
            log.info("File uploaded to bucket({}): {}", bucketName, fileName);
        }

        return "Upload successful";

    }

    @Override
    public String getFileUrl(String bucketName, String keyName) {
        // Construct the public URL for the file
        String fileUrl = "https://" + bucketName + ".fra1.digitaloceanspaces.com/" + keyName;

        log.info("Public URL for file in bucket({}): {}", bucketName, keyName);

        return fileUrl; // This URL will not expire
    }

    // Method to download a file from an S3 bucket
    @Override
    public ByteArrayOutputStream downloadFile(
            final String bucketName,
            final String keyName
    ) throws IOException, AmazonClientException {
        S3Object s3Object = s3Client.getObject(bucketName, keyName);
        InputStream inputStream = s3Object.getObjectContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int len;
        byte[] buffer = new byte[4096];
        while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        log.info("File downloaded from bucket({}): {}", bucketName, keyName);
        return outputStream;
    }

    // Method to list files in an S3 bucket
    @Override
    public List<String> listFiles(final String bucketName) throws AmazonClientException {
        List<String> keys = new ArrayList<>();
        ObjectListing objectListing = s3Client.listObjects(bucketName);

        while (true) {
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            if (objectSummaries.isEmpty()) {
                break;
            }

            objectSummaries.stream()
                    .filter(item -> !item.getKey().endsWith("/"))
                    .map(S3ObjectSummary::getKey)
                    .forEach(keys::add);

            objectListing = s3Client.listNextBatchOfObjects(objectListing);
        }

        log.info("Files found in bucket({}): {}", bucketName, keys);
        return keys;
    }

    // Method to delete a file from an S3 bucket
    @Override
    public void deleteFile(
            final String bucketName,
            final String keyName
    ) throws AmazonClientException {
        s3Client.deleteObject(bucketName, keyName);
        log.info("File deleted from bucket({}): {}", bucketName, keyName);
    }
}
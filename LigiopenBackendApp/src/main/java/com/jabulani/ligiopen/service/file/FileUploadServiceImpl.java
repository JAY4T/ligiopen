package com.jabulani.ligiopen.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jabulani.ligiopen.dao.FileDao;
import com.jabulani.ligiopen.entity.file.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);
    
    private final AmazonS3 s3Client;
    private final FileDao fileDao;
    
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    
    @Value("${cloud.aws.s3.endpoint}")
    private String s3Endpoint;
    
    @Autowired
    public FileUploadServiceImpl(AmazonS3 s3Client, FileDao fileDao) {
        this.s3Client = s3Client;
        this.fileDao = fileDao;
    }
    
    @Override
    @Transactional
    public File uploadFile(MultipartFile file, Long uploadedBy, String description, boolean isPublic) {
        try {
            logger.info("Starting file upload for user: {}, filename: {}", uploadedBy, file.getOriginalFilename());
            
            // Validate file
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            
            if (file.getSize() > 50 * 1024 * 1024) { // 50MB limit
                throw new RuntimeException("File size exceeds 50MB limit");
            }
            
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String storedName = UUID.randomUUID().toString() + fileExtension;
            
            // Determine file type
            File.FileType fileType = determineFileType(file.getContentType());
            
            // Create folder structure: files/YYYY/MM/
            LocalDateTime now = LocalDateTime.now();
            String folderPath = String.format("files/%d/%02d/", now.getYear(), now.getMonthValue());
            String s3Key = folderPath + storedName;
            
            // Upload to S3/Digital Ocean Spaces
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            metadata.addUserMetadata("original-filename", originalFilename);
            
            PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3Key, file.getInputStream(), metadata);
            
            // Set public read access if public file
            if (isPublic) {
                putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            
            s3Client.putObject(putRequest);
            logger.info("File uploaded to S3/Spaces with key: {}", s3Key);
            
            // Save file metadata to database
            File fileEntity = File.builder()
                    .originalName(originalFilename)
                    .storedName(storedName)
                    .filePath(s3Key)
                    .fileSize(file.getSize())
                    .mimeType(file.getContentType())
                    .fileType(fileType)
                    .uploadedBy(uploadedBy)
                    .description(description)
                    .isPublic(isPublic)
                    .createdAt(LocalDateTime.now())
                    .build();
            
            File savedFile = fileDao.saveFile(fileEntity);
            logger.info("File metadata saved with ID: {}", savedFile.getId());
            
            return savedFile;
            
        } catch (IOException e) {
            logger.error("Failed to upload file for user: {}", uploadedBy, e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error uploading file for user: {}", uploadedBy, e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public File uploadProfilePicture(MultipartFile file, Long uploadedBy) {
        // Validate that it's an image
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Profile picture must be an image file");
        }
        
        // Validate image size (max 10MB for profile pictures)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("Profile picture must be less than 10MB");
        }
        
        return uploadFile(file, uploadedBy, "Profile picture", true);
    }
    
    @Override
    @Transactional
    public String deleteFile(Integer fileId) {
        try {
            logger.info("Deleting file with ID: {}", fileId);
            
            // Get file metadata
            File file = fileDao.getFileById(fileId);
            
            // Delete from S3/Spaces
            s3Client.deleteObject(bucketName, file.getFilePath());
            logger.info("File deleted from S3/Spaces: {}", file.getFilePath());
            
            // Delete from database
            String result = fileDao.deleteFile(fileId);
            logger.info("File metadata deleted from database: {}", result);
            
            return result;
            
        } catch (Exception e) {
            logger.error("Failed to delete file with ID: {}", fileId, e);
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }
    
    @Override
    public String getFileUrl(Integer fileId) {
        try {
            File file = fileDao.getFileById(fileId);
            
            if (file.getIsPublic()) {
                // Return public URL for public files
                return String.format("%s/%s/%s", s3Endpoint, bucketName, file.getFilePath());
            } else {
                // Generate pre-signed URL for private files (valid for 1 hour)
                java.util.Date expiration = new java.util.Date();
                long expTimeMillis = expiration.getTime();
                expTimeMillis += 1000 * 60 * 60; // 1 hour
                expiration.setTime(expTimeMillis);
                
                return s3Client.generatePresignedUrl(bucketName, file.getFilePath(), expiration).toString();
            }
        } catch (Exception e) {
            logger.error("Failed to get URL for file ID: {}", fileId, e);
            throw new RuntimeException("Failed to get file URL: " + e.getMessage());
        }
    }
    
    @Override
    public File getFileById(Integer fileId) {
        try {
            return fileDao.getFileById(fileId);
        } catch (Exception e) {
            logger.error("Failed to get file by ID: {}", fileId, e);
            throw new RuntimeException("File not found: " + e.getMessage());
        }
    }
    
    /**
     * Determine file type based on MIME type
     */
    private File.FileType determineFileType(String mimeType) {
        if (mimeType == null) return File.FileType.OTHER;
        
        if (mimeType.startsWith("image/")) return File.FileType.IMAGE;
        if (mimeType.startsWith("video/")) return File.FileType.VIDEO;
        if (mimeType.startsWith("audio/")) return File.FileType.AUDIO;
        if (mimeType.equals("application/pdf") || 
            mimeType.startsWith("application/msword") ||
            mimeType.startsWith("application/vnd.openxmlformats") ||
            mimeType.equals("text/plain")) {
            return File.FileType.DOCUMENT;
        }
        
        return File.FileType.OTHER;
    }
}
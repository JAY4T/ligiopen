package com.jabulani.ligiopen.service.file;

import com.jabulani.ligiopen.entity.file.File;
import com.jabulani.ligiopen.entity.file.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    
    /**
     * Upload a file to Digital Ocean Spaces and save metadata to database
     * @param file The multipart file to upload
     * @param uploadedBy User ID of the uploader
     * @param description Optional description for the file
     * @param isPublic Whether the file should be publicly accessible
     * @return The saved File entity with metadata
     */
    File uploadFile(MultipartFile file, Long uploadedBy, String description, boolean isPublic);
    
    /**
     * Upload a profile picture specifically (automatically sets as public image)
     * @param file The multipart file to upload
     * @param uploadedBy User ID of the uploader
     * @return The saved File entity
     */
    File uploadProfilePicture(MultipartFile file, Long uploadedBy);
    
    /**
     * Delete a file from both Digital Ocean Spaces and database
     * @param fileId The file ID to delete
     * @return Success message
     */
    String deleteFile(Integer fileId);
    
    /**
     * Get file download/access URL
     * @param fileId The file ID
     * @return Public URL to access the file
     */
    String getFileUrl(Integer fileId);
    
    /**
     * Get file metadata
     * @param fileId The file ID
     * @return File entity with metadata
     */
    File getFileById(Integer fileId);
}
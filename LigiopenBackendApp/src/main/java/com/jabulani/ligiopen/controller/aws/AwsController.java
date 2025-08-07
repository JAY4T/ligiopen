package com.jabulani.ligiopen.controller.aws;

import com.jabulani.ligiopen.config.response.BuildResponse;
import com.jabulani.ligiopen.model.aws.FileType;
import com.jabulani.ligiopen.service.aws.AwsService;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/s3bucketstorage")
public class AwsController {

    private final BuildResponse buildResponse;

    private final AwsService service;

    @Autowired
    public AwsController(BuildResponse buildResponse, AwsService service) {
        this.buildResponse = buildResponse;
        this.service = service;
    }

    // Endpoint to list files in a bucket
    @GetMapping("/{bucketName}")
    public ResponseEntity<?> listFiles(
            @PathVariable("bucketName") String bucketName
    ) {
        val body = service.listFiles(bucketName);
        return ResponseEntity.ok(body);
    }

    // Endpoint to upload a file to a bucket
    @PostMapping("/{bucketName}/upload")
    @SneakyThrows(IOException.class)
    public ResponseEntity<?> uploadFiles(
            @PathVariable("bucketName") String bucketName,
            @RequestParam("file") MultipartFile[] files
    ) throws Exception {
        try {
            if (files.length == 0) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("error", "Files is empty");
                return buildResponse.error("files", errors, HttpStatus.BAD_REQUEST);
            } else {
                return buildResponse.success(service.uploadFiles(bucketName, files),"files",  null, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    // Endpoint to download a file from a bucket
    @SneakyThrows
    @GetMapping("/{bucketName}/download/{fileName}")
    public ResponseEntity<?> downloadFile(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName
    ) {
        val body = service.downloadFile(bucketName, fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(FileType.fromFilename(fileName))
                .body(body.toByteArray());
    }

    // Endpoint to delete a file from a bucket
    @DeleteMapping("/{bucketName}/{fileName}")
    public ResponseEntity<?> deleteFile(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName
    ) {
        service.deleteFile(bucketName, fileName);
        return ResponseEntity.ok().build();
    }
}
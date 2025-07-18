package com.jabulani.ligiopen.config;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DiskMultipartFile implements MultipartFile {
    private final File file;
    private final String contentType;

    public DiskMultipartFile(File file, String contentType) {
        this.file = file;
        this.contentType = contentType;
    }

    @Override public String getName() { 
        return "file"; 
    }

    @Override public String getOriginalFilename() { 
        return file.getName(); 
    }

    @Override public String getContentType() { 
        return contentType; 
    }

    @Override public boolean isEmpty() { 
        return file.length() == 0; 
    }

    @Override public long getSize() { 
        return file.length(); 
    }

    @Override public byte[] getBytes() throws IOException { 
        return Files.readAllBytes(file.toPath()); 
    }

    @Override public InputStream getInputStream() throws IOException { 
        return new FileInputStream(file); 
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}

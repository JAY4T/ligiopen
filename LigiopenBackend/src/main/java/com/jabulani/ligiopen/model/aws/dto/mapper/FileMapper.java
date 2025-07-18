package com.jabulani.ligiopen.model.aws.dto.mapper;

import com.jabulani.ligiopen.config.Constants;
import com.jabulani.ligiopen.model.aws.File;
import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.service.aws.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
    private final AwsService awsService;
    @Autowired
    public FileMapper(AwsService awsService) {
        this.awsService = awsService;
    }

    public FileDto fileDto(File file) {
        return FileDto.builder()
                .fileId(file.getId())
                .link(awsService.getFileUrl(Constants.BUCKET_NAME, file.getName()))
                .build();
    }
}

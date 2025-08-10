package com.jabulani.ligiopen.mapper;

import com.jabulani.ligiopen.dto.response.SuccessDto;

import org.springframework.stereotype.Component;

@Component
public class SuccessMapper {
    
    public SuccessDto toSuccessDto(boolean success) {
        return SuccessDto.builder()
                .success(success)
                .message(success ? "Operation completed successfully" : "Operation failed")
                .build();
    }
    
    public SuccessDto toSuccessDto(boolean success, String message) {
        return SuccessDto.builder()
                .success(success)
                .message(message)
                .build();
    }
}
package com.jabulani.ligiopen.model.dto.mapper;

import com.jabulani.ligiopen.model.dto.classes.SuccessDto;
import com.jabulani.ligiopen.model.tables.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class SuccessMapper {
    public SuccessDto toSuccessDto(Boolean success) {
        return SuccessDto.builder()
                .success(success)
                .build();
    }
}

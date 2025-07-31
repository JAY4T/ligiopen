package com.jabulani.ligiopen.service.userEnity;

import com.jabulani.ligiopen.model.dto.classes.SignupRequestDto;
import com.jabulani.ligiopen.model.dto.classes.SuccessDto;
import com.jabulani.ligiopen.model.dto.classes.UserDto;

public interface UserEntityService {
    SuccessDto createUser(SignupRequestDto signupRequestDto);
    UserDto getUserById(Long id);
}

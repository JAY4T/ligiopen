package com.jabulani.ligiopen.service.userEnity;

import com.jabulani.ligiopen.model.dto.classes.OauthMethod;
import com.jabulani.ligiopen.model.dto.classes.SignupRequestDto;
import com.jabulani.ligiopen.model.dto.classes.SuccessDto;

public interface UserEntityService {
    SuccessDto createUser(SignupRequestDto signupRequestDto);

}

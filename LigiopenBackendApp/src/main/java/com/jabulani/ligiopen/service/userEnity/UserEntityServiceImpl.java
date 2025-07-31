package com.jabulani.ligiopen.service.userEnity;

import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.model.dto.classes.SignupRequestDto;
import com.jabulani.ligiopen.model.dto.classes.SuccessDto;
import com.jabulani.ligiopen.model.dto.classes.UserDto;
import com.jabulani.ligiopen.model.dto.mapper.SuccessMapper;
import com.jabulani.ligiopen.model.dto.mapper.UserMapper;
import com.jabulani.ligiopen.model.tables.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserEntityServiceImpl implements UserEntityService{

    private final UserEntityDao userEntityDao;
    private final UserMapper userMapper;
    private final SuccessMapper successMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserEntityServiceImpl(
            UserEntityDao userEntityDao,
            UserMapper userMapper,
            SuccessMapper successMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userEntityDao = userEntityDao;
        this.userMapper = userMapper;
        this.successMapper = successMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public SuccessDto createUser(SignupRequestDto signupRequestDto) {


        UserEntity userEntity = UserEntity.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build();
        userEntityDao.createUser(userEntity);

        return successMapper.toSuccessDto(true);
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toUserDto(userEntityDao.getUserById(id));
    }
}

package com.daniel.security_jwt.mapper;

import org.modelmapper.ModelMapper;

import com.daniel.security_jwt.user.User;
import com.daniel.security_jwt.user.UserDTO;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class UserMapper implements IMapper<User, UserDTO> {

    private final ModelMapper userMapper;

    @Autowired
    public UserMapper(final ModelMapper newUserMapper) {
        this.userMapper = newUserMapper;
    }

    @Override
    public UserDTO mapTo(final User user) {
        return this.userMapper.map(user, UserDTO.class);
    }

    @Override
    public User mapFrom(final UserDTO userDTO) {
        return this.userMapper.map(userDTO, User.class);
    }

}
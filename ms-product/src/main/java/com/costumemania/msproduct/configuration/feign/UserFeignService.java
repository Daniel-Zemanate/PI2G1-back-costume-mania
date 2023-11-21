package com.costumemania.msproduct.configuration.feign;

import com.costumemania.msusers.model.dto.UserAccountResponse;
import com.costumemania.msusers.model.entity.UserEntity;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFeignService{

    @Autowired
    private UsersFeignRepository usersFeignRepository;

    public UserFeign getByUsername(String username) {
        return usersFeignRepository.authUsersFeign(username);
    }
}

package com.costumemania.mscatalog.configuration.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFeignService {

    @Autowired
    private UsersFeignRepository usersFeignRepository;

    public UserFeign getByUsername(String username) {
        return usersFeignRepository.authUsersFeign(username);
    }
}

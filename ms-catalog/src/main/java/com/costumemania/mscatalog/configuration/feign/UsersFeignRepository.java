package com.costumemania.mscatalog.configuration.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-users")
public interface UsersFeignRepository {

    @GetMapping("/api/v1/users/feign/{username}")
    UserFeign authUsersFeign(@PathVariable String username);

}
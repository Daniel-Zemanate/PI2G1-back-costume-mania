package com.costumemania.msfavorite.client;



import com.costumemania.msfavorite.model.UsersEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name= "ms-users")
public interface IUserClient {

    @GetMapping("api/v1/users/all")
    List<UsersEntity> allUsers();

}

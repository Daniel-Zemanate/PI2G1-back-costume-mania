package com.costumemania.msusers.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserExists {
    private boolean userExists;


    public static UserExists fromUserAccountResponse(UserAccountResponse user) {
        return UserExists.builder()
                .userExists(!user.getSoftDelete())
                .build();
    }
}

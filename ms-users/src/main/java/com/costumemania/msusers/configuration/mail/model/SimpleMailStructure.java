package com.costumemania.msusers.configuration.mail.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SimpleMailStructure {

    private String[] toUser;
    private String subject;
    private String message;
}
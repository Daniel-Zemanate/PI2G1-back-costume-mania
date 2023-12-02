package com.costumemania.msusers.configuration.mail.service;


import com.costumemania.msusers.configuration.mail.model.SimpleMailStructure;

import java.io.File;

public interface IEmailService {

    void sendEmail(SimpleMailStructure mailStructure);

    void sendEmailWithFile(String[] toUser, String subject, String message, File file);
}
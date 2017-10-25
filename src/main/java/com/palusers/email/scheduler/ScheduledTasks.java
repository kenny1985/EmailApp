package com.palusers.email.scheduler;

/**
 * Created by 299787 on 13/10/17.
 */

import com.palusers.email.common.AppConstants;
import com.palusers.email.domain.EmailEntity;
import com.palusers.email.logger.ILogger;
import com.palusers.email.logger.LoggerFactory;
import com.palusers.email.services.EmailCreationService;
import com.palusers.email.utils.PropertyFileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Component
public class ScheduledTasks {

    private static ILogger logger;

    @Autowired
    private EmailCreationService emailservice;

    @Autowired
    private PropertyFileReader fileReader;


    //@Value("${delayValue}")
    private String delayValue;

    public ScheduledTasks(LoggerFactory loggerFactory)
    {
        logger = loggerFactory.getLoggerInstance();
    }




    @Scheduled(fixedRateString = "50000")
    public void RunJob() {

        logger.info("Job started at "+ LocalDateTime.now());

        try{

            insertValues();
            logger.info("before calling list size--");
           List<EmailEntity> emailEntity =  emailservice.GetEmailEntities(AppConstants.DeletionStatus.TOBESENT.name());

           logger.info("list size--"+emailEntity.size());

           for(EmailEntity entity : emailEntity){

               emailservice.UpdateEmailStatus(entity.getId(),AppConstants.DeletionStatus.SENDING.name(),LocalDateTime.now());

              if( entity.getPasswords()!=null && entity.getPasswords().trim().length()>0)
               updateMessageContent(entity);

               sendEmail(entity);
           }

        }
        catch(Exception ex){
         ex.printStackTrace();
        }

        logger.info("Job ended at "+ LocalDateTime.now());
    }

    private void insertValues() {

        logger.info("before inserting data");

        EmailEntity ent = new EmailEntity();
        ent.setId(10L);
        ent.setBcc("test@test.com");
        ent.setCc("test@test.com");
        ent.setMessage("message content 1: ##PH##    content2: ##PH1##");
       // ent.setCreationdate(LocalDateTime.now());
        ent.setFromEmailId("test@test.com");
      //  ent.setLastmodifieddate(LocalDateTime.now());
        ent.setPasswords(fileReader.getMessage("password_template"));
        ent.setStatus("TOBESENT");
        ent.setSubject("test sub");
        ent.setToEmailId("test@test.com");
        ent.setUsername("kannan");


        emailservice.saveAll(ent);

        logger.info("after inserting data");
    }

    private void updateMessageContent(EmailEntity entity) throws Exception{

        String message = entity.getMessage();
        String pwds = entity.getPasswords();

        JSONParser parser = new JSONParser();
        JSONObject resultObj = (JSONObject) parser.parse(pwds);
        JSONArray array = (JSONArray) resultObj.get("passwords");

        for(Object users : array) {

           String placeHolder = (String) ((JSONObject)users).get("PLACEHOLDER");
           String value = (String) ((JSONObject)users).get("VALUE");

           encryptVal(value);
            message = message.replaceAll(placeHolder,value);
        }

        entity.setMessage(message);
    }

    private void encryptVal(String value) throws Exception{

        //String key = "echo-alpha-timee"; // AES only supports key sizes of 16, 24 or 32 bytes

        String key = UUID.randomUUID().toString().replace("-",""); // This is to arrive the 32 bytes key size

        // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        // encrypt the text
//        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
//        byte[] encrypted = cipher.doFinal(value.getBytes());
//        System.err.println(new String(encrypted));
        // decrypt the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        value = new String(cipher.doFinal(value.getBytes()));
        System.err.println("decrypted val--"+value);
    }

    private void sendEmail(EmailEntity entity){

        try{
            // CALL TO EMAIL APP
            emailservice.UpdateEmailStatus(entity.getId(),AppConstants.DeletionStatus.SENT.name(),LocalDateTime.now());
        }
        catch(Exception e){

            emailservice.UpdateEmailStatus(entity.getId(),AppConstants.DeletionStatus.TOBESENT.name(),LocalDateTime.now());
        }

    }



}


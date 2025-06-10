package com.infytelfriendfamily.controller;

import com.infytelfriendfamily.dto.FriendFamilyDTO;
import com.infytelfriendfamily.service.FriendFamilyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class FriendFamilyController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private FriendFamilyService friendFamilyService;

    @RequestMapping(value="/customers/{phoneNo}/friends",method = RequestMethod.POST,consumes = "application/json")
    public ResponseEntity<?> saveFriend(@PathVariable Long phoneNo,@RequestBody FriendFamilyDTO friendDTO) {
       String msg =friendFamilyService.saveFriend(phoneNo, friendDTO);

       return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }


    @RequestMapping(value="/customers/{phoneNo}/friends",method = RequestMethod.GET)
    public ResponseEntity<List<Long>> getFriendDetailsByPhoneNumber(@PathVariable Long phoneNo) {
        logger.info("Friend family request received  :"+"  "+"getFriendDetailsByPhoneNumber  :"+phoneNo);
      /*  if(phoneNo == 9009009001L) {
            throw  new RuntimeException();
        }*/
        try{
            Thread.sleep(5000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        List<Long> friendsPhoneNumberList =friendFamilyService.getSpecificFriends(phoneNo);

        return new ResponseEntity<>(friendsPhoneNumberList, HttpStatus.OK);
    }



}

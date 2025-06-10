package com.infytelcustomer.feignConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "FRIENDFAMILYMS")
public interface FriendFamilyClient {
    @RequestMapping(value="/customers/{phoneNo}/friends",method = RequestMethod.GET)
      ResponseEntity<List<Long>> getFriendDetailsByPhoneNumber(@PathVariable Long phoneNo);
}

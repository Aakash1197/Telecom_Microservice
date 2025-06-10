package com.infytelfriendfamily.service;

import com.infytelfriendfamily.dto.FriendFamilyDTO;
import com.infytelfriendfamily.entity.FriendFamily;
import com.infytelfriendfamily.repository.FriendFamilyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendFamilyService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FriendFamilyRepository friendFamilyRepository;
    // Save the friend details of a specific customer
    public String saveFriend(Long phoneNo, FriendFamilyDTO friendDTO) {
        logger.info("Creation request for customer {} with data {}", phoneNo, friendDTO);
        friendDTO.setPhoneNo(phoneNo);
        FriendFamily friend = friendDTO.createFriend();

       friendFamilyRepository.save(friend);

       return "Requested Mobile number  :"+phoneNo+"  Has been added into your family.";

    }

    public List<Long> getSpecificFriends(Long PhoneNo){
        List<Long> list = new ArrayList<>();
       List<FriendFamily> friendFamilies= friendFamilyRepository.getByPhoneNo(PhoneNo);

       friendFamilies.forEach(friendFamily -> {list.add(friendFamily.getFriendAndFamily());});
       return list;
    }
}

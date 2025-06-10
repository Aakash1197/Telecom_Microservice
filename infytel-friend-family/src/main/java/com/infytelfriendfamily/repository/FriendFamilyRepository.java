package com.infytelfriendfamily.repository;

import com.infytelfriendfamily.entity.FriendFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendFamilyRepository extends JpaRepository<FriendFamily, Integer> {

    public List<FriendFamily>  getByPhoneNo(Long phoneNo);
}

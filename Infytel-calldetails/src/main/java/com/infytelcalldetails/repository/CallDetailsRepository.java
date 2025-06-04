package com.infytelcalldetails.repository;



import com.infytelcalldetails.entity.CallDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CallDetailsRepository extends JpaRepository<CallDetails, Long> {

	List<CallDetails> findByCalledBy(long calledBy);
}

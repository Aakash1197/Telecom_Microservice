package com.infytelcalldetails.service;


import com.infytelcalldetails.dto.CallDetailsDTO;
import com.infytelcalldetails.dto.CustomerDTO;
import com.infytelcalldetails.entity.CallDetails;
import com.infytelcalldetails.entity.Customer;
import com.infytelcalldetails.repository.CallDetailsRepository;
import com.infytelcalldetails.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class CustomerService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CustomerRepository custRepo;

	@Autowired
	CallDetailsRepository callDetailsRepo;




	// Fetches full profile of a specific customer
	public CustomerDTO getCustomerProfile(Long phoneNo) {
		CustomerDTO custDTO=null;
		logger.info("Profile request for customer {}", phoneNo);
		Optional<Customer> cust = custRepo.findById(phoneNo);
		if(cust.isPresent())
			custDTO = CustomerDTO.valueOf(cust.get());

		logger.info("Profile for customer : {}", custDTO);
		return custDTO;
	}

	// Fetches call details of a specific customer
	public List<CallDetailsDTO> getCustomerCallDetails(@PathVariable long phoneNo) {

		logger.info("Calldetails request for customer {}", phoneNo);

		List<CallDetails> callDetails = callDetailsRepo.findByCalledBy(phoneNo);
		List<CallDetailsDTO> callsDTO = new ArrayList<>();

		for (CallDetails call : callDetails) {
			callsDTO.add(CallDetailsDTO.valueOf(call));
		}
		logger.info("Calldetails for customer : {}", callDetails);

		return callsDTO;
	}



}

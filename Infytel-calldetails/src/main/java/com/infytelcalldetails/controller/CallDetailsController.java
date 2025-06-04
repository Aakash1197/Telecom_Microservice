package com.infytelcalldetails.controller;


import com.infytelcalldetails.dto.CallDetailsDTO;
import com.infytelcalldetails.dto.CustomerDTO;
import com.infytelcalldetails.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin

public class CallDetailsController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CustomerService custService;

	// Fetches call details of a specific customer

	@GetMapping(value = "/customers/{phoneNo}/calldetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CallDetailsDTO>> getCustomerCallDetails(@PathVariable long phoneNo) {
		logger.info("Calldetails request for customer {}", phoneNo);

		return new ResponseEntity<>(custService.getCustomerCallDetails(phoneNo), HttpStatus.FOUND);
	}



}

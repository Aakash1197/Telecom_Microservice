package com.infytelcustomer.controller;

import com.infytelcustomer.config.CustomerConfig;
import com.infytelcustomer.dto.CustomerDTO;
import com.infytelcustomer.dto.LoginDTO;
import com.infytelcustomer.service.CustomerService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin
public class CustomerController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CustomerService custService;


	// Create a new customer
	
	@PostMapping(value = "/customers",  consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCustomer(@RequestBody CustomerDTO custDTO) {
		logger.info("Creation request for customer {}", custDTO);
	return new ResponseEntity<>(	custService.createCustomer(custDTO), HttpStatus.CREATED);
	}

	// Login
	
	@PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
		String msg=null;
		ResponseEntity<?> responseEntity=null;
		logger.info("Login request for customer {} with password {}", loginDTO.getPhoneNo(),loginDTO.getPassword());
		if(custService.login(loginDTO)){
			 msg = "YOU HAVE SUCCESSFULLY LOGGED!!.";
			responseEntity=new ResponseEntity<>(	msg, HttpStatus.OK);
		}
		else{
			 msg = "YOU HAVE ENTERED WRONG MOBILE NUMBER OR PASSWORD PLEASE CHECK AND TRY AGAIN!!.";
			responseEntity=new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}

	// Fetches full profile of a specific customer

	@GetMapping(value = "/customers/{phoneNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerDTO> getCustomerProfile(@PathVariable Long phoneNo) {

		logger.info("Profile request for customer {}", phoneNo);


		return new ResponseEntity<>(	custService.getCustomerProfile(phoneNo), HttpStatus.OK);
	}






}

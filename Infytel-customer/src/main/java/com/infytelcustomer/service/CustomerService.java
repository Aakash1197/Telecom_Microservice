package com.infytelcustomer.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infytelcustomer.dto.CustomerDTO;
import com.infytelcustomer.dto.LoginDTO;
import com.infytelcustomer.dto.PlanDTO;
import com.infytelcustomer.entity.Customer;
import com.infytelcustomer.feignConfig.FriendFamilyClient;
import com.infytelcustomer.feignConfig.PlanClient;
import com.infytelcustomer.repository.CustomerRepository;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.concurrent.Callable;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import io.vavr.concurrent.Future;

@Service
public class CustomerService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CustomerRepository custRepo;

	@Autowired
	private DiscoveryClient discoveryClient;


	@Autowired
	private RestTemplate resTemplate;

	@Autowired
	private WebClient.Builder webClient;
	@Autowired
	private PlanClient planClient;

	@Autowired
	private FriendFamilyClient friendFamilyClient;

	@Autowired
	private ObjectMapper objectMapper;



	private String planUrl;

	private String friendsUrl;
	@Autowired
	private ExecutorService executorService;
	// Create a new customer
	public String createCustomer(CustomerDTO custDTO) {
		logger.info("Creation request for customer {}", custDTO);

		// Create customer entity
		Customer cust = custDTO.createEntity();


		// Validate friendAndFamily list
		if (custDTO.getFriendAndFamily().isEmpty()) {
			throw new RuntimeException("No friend list provided");
		}

		custRepo.save(cust);


		return "Customer created successfully";
	}
	// Login
	public boolean login(LoginDTO loginDTO) {
		Boolean flag=false;
		logger.info("Login request for customer {} with password {}", loginDTO.getPhoneNo(),loginDTO.getPassword());
		Optional<Customer> cust;
		cust=custRepo.findById(loginDTO.getPhoneNo());
		if(cust.isPresent() && cust.get() != null && cust.get().getPassword().equals(loginDTO.getPassword())) {
			flag= true;
		}
		return flag;
	}

	// Fetches full profile of a specific customer
	@CircuitBreaker(name="customerService", fallbackMethod = "getCustomerProfileFallBack")
	public CustomerDTO getCustomerProfile(Long phoneNo) {

		CustomerDTO custDTO=null;
		logger.info("Profile request for customer {}", phoneNo);
		Long overAllStartTime = System.currentTimeMillis();

		Optional<Customer> cust = custRepo.findById(phoneNo);
		if(cust.isPresent()) {
			custDTO = CustomerDTO.valueOf(cust.get());
		}
        assert custDTO != null;

/*		List<ServiceInstance> planMSInstance = discoveryClient.getInstances("plansMS");
		logger.info("planMSInstance {}", planMSInstance.stream().toList());
		if (planMSInstance.isEmpty()) {
			throw new RuntimeException("No available instances for planMS");
		}

		ServiceInstance planInstance = planMSInstance.get(0);

		int planPort=planInstance.getPort();
		planUrl = planInstance.getUri()+"/plans/"+ custDTO.getCurrentPlan().getPlanId();*/



/*planUrl = "http://PLANSMS/plans/" + custDTO.getCurrentPlan().getPlanId();*/

		//REST TEMPLATE
		logger.info("PLAN URL {}", planUrl);
 		/*PlanDTO planDTO =resTemplate.getForObject(planUrl, PlanDTO.class);*/

/*		//WEB CLIENT
		PlanDTO planDTO= webClient
				.build()
				.get()
				.uri(planUrl)
				.retrieve()
				.bodyToMono(PlanDTO.class)
				.timeout(Duration.ofSeconds(30))
				.block();*/

		//feign client
		Long planStartTime=System.currentTimeMillis();


		ResponseEntity<PlanDTO> responseFromPlanMS = getPlanServiceWithCircuitBreaker(custDTO);

		Long planEndTime=System.currentTimeMillis();

		logger.info("Plan available instance {}", responseFromPlanMS);
			if (!responseFromPlanMS.getStatusCode().is2xxSuccessful()) {
				throw new RuntimeException("PlanMS service is unavailable!");
			}
			PlanDTO planDTO=responseFromPlanMS.getBody();




/*


		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		List<ServiceInstance> friendFamilyMSInstance = discoveryClient.getInstances("FRIENDFAMILYMS");
		if (friendFamilyMSInstance.isEmpty()) {
			throw new RuntimeException("No available instances for FriendFamilyMS");
		}
		ServiceInstance friendFamilyInstance = friendFamilyMSInstance.get(0);
		friendsUrl = friendFamilyInstance.getUri().toString()+"/customers/";
		//String friendsFamilyUrl = url+ phoneNo + "/friends";

		//rest template
		ResponseEntity<List> response = resTemplate.exchange(
				friendsUrl,
				HttpMethod.GET,
				entity,
				List.class,
				phoneNo,"/friends"
		);
*/
/*		ResponseEntity<List> response = (ResponseEntity<List>) restemplate.getForObject(url+"/customers/",List.class, entity, phoneNo);*//*



        assert response != null;
        List<Long> friendList = response.getBody();
*/

/*		List<Long> friendList = new RestTemplate().getForObject("http://localhost:8083/customers/{phoneNo}/friends", List.class, phoneNo);*/

		//Feign client
		ResponseEntity<List<Long>> friendList;
		Long friendsStartTime=0L;
		Long friendEndTime=0L;
		try {
			 friendsStartTime=System.currentTimeMillis();

			 friendList = getFriendFamilyServiceWithCircuitBreaker(phoneNo);
			friendEndTime=System.currentTimeMillis();

		}
		catch (FeignException e) {
			logger.error("Error calling FriendFamilyMS: {}", e.getMessage());

			throw new RuntimeException("Service unavailable");
		} catch (Exception e) {
			logger.error("Error calling FriendFamilyMS: {}", e.getMessage());

			throw new RuntimeException("Service unavailable");
        }
        custDTO.setCurrentPlan(planDTO);
		//feign client
		custDTO.setFriendAndFamily(friendList.getBody());
	/*	//web and rest template client
		custDTO.setFriendAndFamily(friendList);*/
		logger.info("Profile for customer : {}", custDTO);
		Long overAllEndTime=System.currentTimeMillis();

		logger.info("Total time for Plan "+(planEndTime-planStartTime));
		logger.info("Total time for Friend "+(friendEndTime-friendsStartTime));
		logger.info("Total Overall time for Request "+(overAllEndTime-overAllStartTime));
		return custDTO;
	}

	private ResponseEntity<List<Long>> getFriendFamilyServiceWithCircuitBreaker(Long phoneNo) throws Exception {
	/*	//Asynchronous communication using Callable and ExecutorService
		try {
			Callable<ResponseEntity<List<Long>>> task = () -> friendFamilyClient.getFriendDetailsByPhoneNumber(phoneNo);

			executorService.submit(task);
//		return friendFamilyClient.getFriendDetailsByPhoneNumber(phoneNo);
			return task.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}*/

		//Asynchronous communication using future
		Future<ResponseEntity<List<Long>>> futureOfFriendFamily= Future.of(()-> friendFamilyClient.getFriendDetailsByPhoneNumber(phoneNo));

		return futureOfFriendFamily.get();
	}

	@CircuitBreaker(name = "customerService")
	private ResponseEntity<PlanDTO> getPlanServiceWithCircuitBreaker(CustomerDTO custDTO)  {
//		ResponseEntity<PlanDTO> responseFromPlanMS=planClient.getAllPlans(custDTO.getCurrentPlan().getPlanId());
		//Asynchronous communication using Callable and ExecutorService
		/*try {
			Callable<ResponseEntity<PlanDTO>> task = () -> planClient.getAllPlans(custDTO.getCurrentPlan().getPlanId());
			executorService.submit(task);

		*//*return responseFromPlanMS;*//*
		return  task.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}*/

		//Asynchronous communication using future
		Future<ResponseEntity<PlanDTO>> futureOfPlan= Future.of(()-> planClient.getAllPlans(custDTO.getCurrentPlan().getPlanId()));

		return futureOfPlan.get();

	}

	@CircuitBreaker(name = "customerService")
	public CustomerDTO getCustomerProfileFallBack( Long phoneNo,Throwable throwable) {
		logger.info("*****IN FALLBACK****** {}", phoneNo);
		return new CustomerDTO();
	}

}

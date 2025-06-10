package com.infytelplans.service;


import com.infytelplans.dto.PlanDTO;
import com.infytelplans.entity.Plan;
import com.infytelplans.repository.PlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanService {
	@Autowired
	PlanRepository planRepo;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	// Fetches all plan details
	public List<PlanDTO> getAllPlans() {
		List<Plan> plans = planRepo.findAll();
		List<PlanDTO> planDTOs = new ArrayList<>();

		for (Plan plan : plans) {
			PlanDTO planDTO = PlanDTO.valueOf(plan);
			planDTOs.add(planDTO);
		}

		logger.info("Plan details : {}", planDTOs);
		return planDTOs;
	}

	public PlanDTO getPlanById(Integer planId) {
		Plan plan = planRepo.findById(planId).get();
		if(plan==null){
			throw new RuntimeException("Plan not found");
		}
		PlanDTO planDTO = PlanDTO.valueOf(plan);
		return planDTO;
	}
}

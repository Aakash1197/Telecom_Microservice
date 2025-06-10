package com.infytelcustomer.feignConfig;

import com.infytelcustomer.dto.PlanDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "PLANSMS")
public interface PlanClient {

    @GetMapping(value = "/plans/{planId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlanDTO> getAllPlans(@PathVariable Integer planId);
}

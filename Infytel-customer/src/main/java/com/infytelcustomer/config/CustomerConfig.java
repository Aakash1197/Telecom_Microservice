package com.infytelcustomer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import feign.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class CustomerConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
  /*  @Bean
    public IRule ribbonRule() {
        return new RoundRobinRule(); // Example: Round-robin load balancing
    }*/
  @Bean
  @LoadBalanced
  public WebClient.Builder webClientBuilder() {
      return WebClient.builder();
  }
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    @Bean
    public Request.Options feignRequestOptions() {
        return new Request.Options(5000, 10000, true); // Enable automatic redirects
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(5); // Customize as needed
    }





}

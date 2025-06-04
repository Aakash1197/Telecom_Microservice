package com.infytelcalldetails.repository;



import com.infytelcalldetails.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	
}

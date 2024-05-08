package com.ng.credit.creditapp.repo;


import com.ng.credit.creditapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo  extends JpaRepository<Customer, Integer> {

}

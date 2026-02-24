package com.banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.banking.entity.Customer;

@Repository

/*For JpaRepository<Customer, Long>
 Customer = entity class
 long = type of primary key (id) */

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Optional<Customer> findByEmail(String email);
}


package com.banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.entity.Transaction;

/*By extending JpaRepository<Transaction, Long>, we automatically get:
 save(),findById(),findAll(),deleteById(),count(), etc. No SQL needed*/
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
	/*Spring JPA reads method name and automatically generates query, equivalent to:
	 SELECT * FROM transactions WHERE customer_id = ?*/
	List<Transaction> findByCustomerId(Long customerId);

}

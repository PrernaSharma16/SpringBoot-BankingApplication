package com.banking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.entity.Transaction;

/*By extending JpaRepository<Transaction, Long>, we automatically get:
 save(),findById(),findAll(),deleteById(),count(), etc. No SQL needed*/
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
	/*Spring JPA reads method name and automatically generates query, equivalent to:
	 SELECT * FROM transactions WHERE customer_id = ?*/
	
	//24/02/26 - Updated repository for pagination using page and page able
	Page<Transaction> findByCustomerId(Long customerId, Pageable pageable);

}

package com.banking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.dto.CustomerRequestDTO;
import com.banking.dto.CustomerResponseDTO;
import com.banking.dto.TransactionResponseDTO;
import com.banking.dto.TransferRequestDTO;
import com.banking.dto.TransferResponseDTO;
import com.banking.entity.Customer;
import com.banking.entity.Transaction;
import com.banking.repository.TransactionRepository;
import com.banking.service.CustomerService;

import jakarta.validation.Valid;

/*@RestController: marks class as REST API controller. Returns JSON directly
  @RequestMapping("/API/customers"): Base URL for all end points
  @PostMapping: Handles HTTP POST requests
  @RequestBody: Converts JSON into java object automatically
  @PathVariable: extracts value from URL*/

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	
	@Autowired
	private TransactionRepository transactionRepository; 
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping
	public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO requestDTO) {
		return ResponseEntity.ok(customerService.createCustomer(requestDTO));
	}
	
	@GetMapping
	public List<Customer> getAllCustomers(){
		return customerService.getAllCustomers();
	}
	
	@GetMapping("/{id}")
	public Customer getCustomerById(@PathVariable Long id) {
		return customerService.getCustomerByID(id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		customerService.deleteCustomer(id);
	}
	
	@PutMapping("/{id}")
	public Customer updateCustomer(@PathVariable Long id,@Valid @RequestBody Customer customer) {
		return customerService.updateCustomer(id, customer);
	}
	
	@PostMapping("/deposit/{id}")
	public ResponseEntity<CustomerResponseDTO> deposit(@PathVariable Long id, @RequestParam Double amount){
		return ResponseEntity.ok(customerService.deposit(id, amount));
	}
	
	@PostMapping("/withdraw/{id}")
	public ResponseEntity<CustomerResponseDTO> withdraw(@PathVariable Long id, @RequestParam Double amount){
		return ResponseEntity.ok(customerService.withdraw(id, amount));
	}
	
	@GetMapping("/transactions/{id}")
	public ResponseEntity<List<TransactionResponseDTO>> getTransactions(@PathVariable Long id){
		return ResponseEntity.ok(customerService.getTransactionHistory(id));
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<TransferResponseDTO> transfer(@Valid @RequestBody TransferRequestDTO request){
		return ResponseEntity.ok(customerService.transfer(request));
	}

}

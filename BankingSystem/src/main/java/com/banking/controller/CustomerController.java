package com.banking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/*@RestController: marks class as REST API controller. Returns JSON directly
  @RequestMapping("/API/customers"): Base URL for all end points
  @PostMapping: Handles HTTP POST requests
  @RequestBody: Converts JSON into java object automatically
  @PathVariable: extracts value from URL
  @Tag: Used on controller classes to group API end points and provide descriptions in the generated doc 
  (In case of tools like Swagger) 
  @Operation: Used to provide detailed metadata for a specific API in a REST controller*/

@Tag(name = "Customer APIs", description = "Operations related to customers and transactions")
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
	
	@Operation(summary = "Get paginated transaction history of a customer")
	@GetMapping("/transactions/{id}")
	
	public ResponseEntity<Page<TransactionResponseDTO>> getTransactions(
			@Parameter (description = "Customer ID") @PathVariable Long id,
			@Parameter (description = "Page number (starts from 0)")
			@RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Number of records per page")
			@RequestParam(defaultValue = "5") int size,
			@Parameter(description = "Field to sort by")
			@RequestParam(defaultValue = "transactionTime") String sortBy,
			@Parameter(description = "Sorting direction: asc or desc")
			@RequestParam(defaultValue = "desc") String direction){
		
		return ResponseEntity.ok(customerService.getTransactionHistory(id, page, size, sortBy, direction));
	}
	
	@Operation(summary = "Transfer money between two customers",
			description = "Transfers specified amount from sender to receiver and creates transaction records for both")
	@PostMapping("/transfer")
	public ResponseEntity<TransferResponseDTO> transfer(@Valid @RequestBody TransferRequestDTO request){
		return ResponseEntity.ok(customerService.transfer(request));
	}

}

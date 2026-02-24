package com.banking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.dto.CustomerRequestDTO;
import com.banking.dto.CustomerResponseDTO;
import com.banking.dto.TransactionResponseDTO;
import com.banking.dto.TransferRequestDTO;
import com.banking.dto.TransferResponseDTO;
import com.banking.entity.Customer;
import com.banking.entity.Transaction;
import com.banking.entity.TransactionType;
import com.banking.exception.EmailAlreadyExistsException;
import com.banking.exception.ResourceNotFoundException;
import com.banking.repository.CustomerRepository;
import com.banking.repository.TransactionRepository;

@Transactional //deposit/withdraw updates customer balance and insert transaction records.If one fails, both should roll back
@Service   //Marks this class as business logic layer. Spring detects it as a bean
public class CustomerService {
	
	@Autowired  //Injects CustomerRepository automatically
	private CustomerRepository customerRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
		
		Customer customer = new Customer();
		customer.setName(requestDTO.getName());
		customer.setEmail(requestDTO.getEmail());
		customer.setPhoneNo(requestDTO.getPhoneNo());
		customer.setBalance(requestDTO.getBalance());
		
		Customer saved = customerRepository.save(customer);
		return mapToResponseDTO(saved);
	}
	
	public List<Customer> getAllCustomers(){
		return customerRepository.findAll();
	}
	
	public Customer getCustomerByID(Long id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer with id "+id+" not found"));
	}
	
	public void deleteCustomer(Long id) {
		Customer existingCustomer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer with id "+id+" not found"));
		customerRepository.delete(existingCustomer);
	}
	
	public Customer updateCustomer(Long id, Customer updatedCustomer) {
		Customer existingCustomer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer with id "+id+" not found"));
		
		if(!existingCustomer.getEmail().equals(updatedCustomer.getEmail())) {
			if(customerRepository.findByEmail(updatedCustomer.getEmail()).isPresent()) {
				throw new EmailAlreadyExistsException("Email "+updatedCustomer.getEmail()+ " already exists");
			}
		}
		
		if(existingCustomer != null) {
			existingCustomer.setName(updatedCustomer.getName());
			existingCustomer.setEmail(updatedCustomer.getEmail());
			existingCustomer.setPhoneNo(updatedCustomer.getPhoneNo());
			existingCustomer.setBalance(updatedCustomer.getBalance());
			
			return customerRepository.save(existingCustomer);
		}
		return null;
	}
	
	/*Validates amount, fetches customer, updates balance, creates transaction record,
	 saves transaction, saves updated customer*/
	public CustomerResponseDTO deposit(Long customerId, Double amount) {
		
		if(amount <= 0) {
			throw new RuntimeException("Deposit amount should be positive");
		}
		
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));
		
		customer.setBalance(customer.getBalance() + amount); //update balance
		
		//creating transaction
		Transaction transaction = new Transaction();
		transaction.setType(TransactionType.DEPOSIT);
		transaction.setAmount(amount);
		transaction.setTransactionTime(LocalDateTime.now());
		transaction.setCustomer(customer);
		
		transactionRepository.save(transaction);
		
		Customer updatedCustomer = customerRepository.save(customer);
		return mapToResponseDTO(updatedCustomer);
	}
	
	public CustomerResponseDTO withdraw(Long customerId, Double amount) {
		
		if(amount <=0) {
			throw new RuntimeException("Withdraw amount must be positive");
		}
		
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));
		
		if(customer.getBalance() < amount) {
			throw new RuntimeException("Insufficient balance!");
		}
		
		customer.setBalance(customer.getBalance() - amount);
		
		Transaction transaction = new Transaction();
		transaction.setType(TransactionType.WITHDRAW);
		transaction.setAmount(amount);
		transaction.setTransactionTime(LocalDateTime.now());
		transaction.setCustomer(customer);
		
		transactionRepository.save(transaction);
		
		Customer updatedCustomer = customerRepository.save(customer);
		return mapToResponseDTO(updatedCustomer);
	}
	
	//converting entity to DTO
	private CustomerResponseDTO mapToResponseDTO(Customer customer) {
		CustomerResponseDTO dto = new CustomerResponseDTO();
		dto.setId(customer.getId());
		dto.setName(customer.getName());
		dto.setEmail(customer.getEmail());
		dto.setPhoneNo(customer.getPhoneNo());
		dto.setBalance(customer.getBalance());
		return dto;
	}
	
	private TransactionResponseDTO mapToTransactionDTO(Transaction transaction) {
		TransactionResponseDTO dto = new TransactionResponseDTO();
		dto.setId(transaction.getId());
		dto.setType(transaction.getType().name());
		dto.setAmount(transaction.getAmount());
		dto.setTransactionTime(transaction.getTransactionTime());
		dto.setReferenceId(transaction.getReferenceId());
		return dto;
	}
	
	//Transaction history method. Now controller doesn't talk to repository directly anymore
	//24/02/26 - Added Pagination Logic
	public Page<TransactionResponseDTO> getTransactionHistory(
			Long customerId, int page, int size, String sortBy, String direction){
		if(!customerRepository.existsById(customerId)) {
			throw new RuntimeException("Customer not found");
		}
		
		Sort sort = direction.equalsIgnoreCase("desc")
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Transaction> transactions = transactionRepository.findByCustomerId(customerId, pageable);
		
		return transactions.map(this::mapToTransactionDTO);
	}
	
	//Transfer method
	public TransferResponseDTO transfer(TransferRequestDTO request) {
		String referenceId = "REF- "+System.currentTimeMillis();
		if(request.getSenderId().equals(request.getReceiverId())) {
			throw new RuntimeException("Sender and Receiver cannot be the same");
		}
		
		Customer sender = customerRepository.findById(request.getSenderId())
				.orElseThrow(() -> new RuntimeException("Sender not found"));
		
		Customer receiver = customerRepository.findById(request.getReceiverId())
				.orElseThrow(() -> new RuntimeException("Receiver not found"));
		
		if(sender.getBalance() < request.getAmount()) {
			throw new RuntimeException("Insufficient balance");
		}
		
		//Deduct from sender
		sender.setBalance(sender.getBalance() - request.getAmount());
		
		Transaction senderTransaction = new Transaction();
		senderTransaction.setType(TransactionType.WITHDRAW);
		senderTransaction.setAmount(request.getAmount());
		senderTransaction.setTransactionTime(LocalDateTime.now());
		senderTransaction.setCustomer(sender);
		senderTransaction.setReferenceId(referenceId);
		
		transactionRepository.save(senderTransaction);
		
		//Add to receiver
		receiver.setBalance(receiver.getBalance() + request.getAmount());
		
		Transaction receiverTransaction = new Transaction();
		receiverTransaction.setType(TransactionType.DEPOSIT);
		receiverTransaction.setAmount(request.getAmount());
		receiverTransaction.setTransactionTime(LocalDateTime.now());
		receiverTransaction.setCustomer(receiver);
		receiverTransaction.setReferenceId(referenceId);
		
		transactionRepository.save(receiverTransaction);
		customerRepository.save(sender);
		customerRepository.save(receiver);
		
		TransferResponseDTO response = new TransferResponseDTO();
		response.setMessage("Transfer successful :)");
		response.setSenderId(sender.getId());
		response.setReceiverId(receiver.getId());
		response.setAmount(request.getAmount());
		response.setSenderBalance(sender.getBalance());
		response.setReceiverBalance(receiver.getBalance());
		
		return response;
	}

}

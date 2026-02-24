package com.banking.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "transactions")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING) //Database will store DEPOSIT/WITHDRAW instead of 0 and 1. Much safer and readable
	private TransactionType type;
	
	private Double amount;
	
	private LocalDateTime transactionTime;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	@JsonIgnore  //When returning transactions in API, it wont keep nesting customer inside transaction repeatedly
	private Customer customer;
	
	private String referenceId;
	
	public Transaction() {
	}
	
	public Transaction(TransactionType type, Double amount, LocalDateTime transactionTime, Customer customer, String referenceId) {
		this.type = type;
		this.amount = amount;
		this.transactionTime = transactionTime;
		this.customer = customer;
		this.referenceId = referenceId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
}

package com.banking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class CustomerRequestDTO {
	
	@NotBlank(message = "Name is required")
	private String name;
	
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is required")
	private String email;
	
	@NotBlank(message = "Phone number is required")
	private String phoneNo;
	
	@NotNull(message = "Balance is required")
	@PositiveOrZero(message = "Balance cannot be negative")
	private Double balance;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

}

package com.banking.entity;

import jakarta.persistence.*;
import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/*mappedBy = "customer": refers to the field name in Transaction class
 cascade = cascadeType.ALL: if customer is deleted, transactions delete too, 
 keeping DB consistent*/

@Entity
@Table(name = "customer")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Name is required")
	@Size(min = 2, max = 100, message = "Name muste be between 2 and 100 characters")
	private String name;
	
	@Column(unique = true)
	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	private String email;
	
	@NotBlank(message = "Phone no is required")
	@Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
	private String phoneNo;
	
	@NotNull(message = "Balance is required")
	@PositiveOrZero(message = "Balance cannot be negative")
	private Double balance;
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Transaction> transactions;
	
	public Customer() {	
	}
	
	public Customer(String name, String email, String phoneNo, Double balance) {
		this.name = name;
		this.email = email;
		this.phoneNo = phoneNo;
		this.balance = balance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

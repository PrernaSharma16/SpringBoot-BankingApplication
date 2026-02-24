package com.banking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransferRequestDTO {
	
	@NotNull(message = "Sender ID is required")
	private Long senderId;
	
	@NotNull(message = "Receiver ID is required")
	private Long receiverId;
	
	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be positive")
	private Double amount;

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}

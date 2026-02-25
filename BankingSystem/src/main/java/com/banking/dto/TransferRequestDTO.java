package com.banking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request object for transferring money between customers")
public class TransferRequestDTO {
	
	@NotNull(message = "Sender ID is required")
	@Schema(description = "Sender Customer ID", example="1")
	private Long senderId;
	
	@Schema(description = "Receiver Customer ID", example = "2")
	@NotNull(message = "Receiver ID is required")
	private Long receiverId;
	
	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be positive")
	@Schema(description = "Amount to transfer", example = "500.0")
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

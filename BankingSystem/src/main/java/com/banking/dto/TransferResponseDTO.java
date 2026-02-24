package com.banking.dto;

public class TransferResponseDTO {
	
	private String message;
	private Long senderId;
	private Long receiverId;
	private Double amount;
	private Double senderBalance;
	private Double receiverBalance;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
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
	public Double getSenderBalance() {
		return senderBalance;
	}
	public void setSenderBalance(Double senderBalance) {
		this.senderBalance = senderBalance;
	}
	public Double getReceiverBalance() {
		return receiverBalance;
	}
	public void setReceiverBalance(Double receiverBalance) {
		this.receiverBalance = receiverBalance;
	}

}

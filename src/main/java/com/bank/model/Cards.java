package com.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
@Entity
@Table(name = "cards")
public class Cards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long card_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(unique = true)
    private String cardNumber; 

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @NotNull
    private BigDecimal limitAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus requestStatus = RequestStatus.NOTAPPLIED;  // default

    public enum CardType {
        CREDIT("Credit Card"),
        DEBIT("Debit Card");
        private final String displayName;
        CardType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum CardStatus {
        ACTIVE("Active"),
        BLOCKED("Blocked"),
        EXPIRED("Expired");
        private final String displayName;
        CardStatus(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum RequestStatus {
        NOTAPPLIED, PENDING, APPROVED, REJECTED
    }

	public Long getCard_id() {
		return card_id;
	}

	public void setCard_id(Long card_id) {
		this.card_id = card_id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public CardStatus getStatus() {
		return status;
	}

	public void setStatus(CardStatus status) {
		this.status = status;
	}

	public BigDecimal getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(BigDecimal limitAmount) {
		this.limitAmount = limitAmount;
	}

	public RequestStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(RequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Cards(Long card_id, Users user, String cardNumber, CardType cardType, CardStatus status,
			@NotNull BigDecimal limitAmount, RequestStatus requestStatus) {
		super();
		this.card_id = card_id;
		this.user = user;
		this.cardNumber = cardNumber;
		this.cardType = cardType;
		this.status = status;
		this.limitAmount = limitAmount;
		this.requestStatus = requestStatus;
	}

	public Cards() {
		
	}

    
    
    
}

package com.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "transactions")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transaction_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Accounts account;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @NotNull
    private BigDecimal amount;

    private LocalDateTime timestamp;

    private String beneficiaryAccount; 

    @PrePersist
    public void prePersist() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER,
        LOAN_DISBURSEMENT,
        LOAN_REPAYMENT,
        INTEREST_CREDIT,
        CARD_PAYMENT,
        FIXED_DEPOSIT,
        RECURRING_DEPOSIT,
        LOAN_PAYMENT,      
        INTEREST_CHARGE
    }
    
    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    private RequestStatus requestStatus;

    public enum RequestStatus {
        NOT_APPLIED,
        PENDING,
        APPROVED,
        REJECTED,
        ACTIVE,
        CLOSED,
        DEFAULTED
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }


	public Long getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(Long transaction_id) {
		this.transaction_id = transaction_id;
	}

	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getBeneficiaryAccount() {
		return beneficiaryAccount;
	}

	public void setBeneficiaryAccount(String beneficiaryAccount) {
		this.beneficiaryAccount = beneficiaryAccount;
	}

	public Transactions() {
		
	}

	public Transactions(Long transaction_id, Accounts account, TransactionType type, @NotNull BigDecimal amount,
			LocalDateTime timestamp, String beneficiaryAccount, RequestStatus requestStatus) {
		super();
		this.transaction_id = transaction_id;
		this.account = account;
		this.type = type;
		this.amount = amount;
		this.timestamp = timestamp;
		this.beneficiaryAccount = beneficiaryAccount;
		this.requestStatus = requestStatus;
	}

	

   

}
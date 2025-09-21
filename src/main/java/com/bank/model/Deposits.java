package com.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "deposits")
public class Deposits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deposit_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users user;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepositType depositType;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal interestRate;

    @NotNull
    private LocalDate maturityDate;

    @Enumerated(EnumType.STRING)
    private DepositStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus requestStatus = RequestStatus.NOTAPPLIED;

    public enum DepositType {
        FIXED, RECURRING, SAVINGS
    }

    public enum DepositStatus {
        PENDING,ACTIVE, MATURED, CLOSED
    }

    public enum RequestStatus {
        NOTAPPLIED, PENDING, APPROVED, REJECTED
    }

    public Deposits() {}

	public Long getDeposit_id() {
		return deposit_id;
	}

	public void setDeposit_id(Long deposit_id) {
		this.deposit_id = deposit_id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public DepositType getDepositType() {
		return depositType;
	}

	public void setDepositType(DepositType depositType) {
		this.depositType = depositType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public LocalDate getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(LocalDate maturityDate) {
		this.maturityDate = maturityDate;
	}

	public DepositStatus getStatus() {
		return status;
	}

	public void setStatus(DepositStatus status) {
		this.status = status;
	}

	public RequestStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(RequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Deposits(Long deposit_id, Users user, DepositType depositType, @NotNull BigDecimal amount,
			@NotNull BigDecimal interestRate, @NotNull LocalDate maturityDate, DepositStatus status,
			RequestStatus requestStatus) {
		super();
		this.deposit_id = deposit_id;
		this.user = user;
		this.depositType = depositType;
		this.amount = amount;
		this.interestRate = interestRate;
		this.maturityDate = maturityDate;
		this.status = status;
		this.requestStatus = requestStatus;
	}
    
    
}

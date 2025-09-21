package com.bank.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "loans")
public class Loans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loan_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    private BigDecimal amount; 
    private BigDecimal interestRate; 
    private Integer tenureMonths;
    private BigDecimal emi;
    private BigDecimal remainingAmount;

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.PENDING; 

    public enum LoanStatus {
        PENDING,
        ACTIVE,
        CLOSED,
        DEFAULTED,
        REJECTED
    }

    public enum LoanType {
        PERSONAL,
        HOME,
        EDUCATION,
        CAR
    }
	public Long getLoan_id() {
		return loan_id;
	}
	public void setLoan_id(Long loan_id) {
		this.loan_id = loan_id;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public LoanType getLoanType() {
		return loanType;
	}
	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
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
	public Integer getTenureMonths() {
		return tenureMonths;
	}
	public void setTenureMonths(Integer tenureMonths) {
		this.tenureMonths = tenureMonths;
	}
	public BigDecimal getEmi() {
		return emi;
	}
	public void setEmi(BigDecimal emi) {
		this.emi = emi;
	}
	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	public LoanStatus getStatus() {
		return status;
	}
	public void setStatus(LoanStatus status) {
		this.status = status;
	}
	public Loans(Long loan_id, Users user, LoanType loanType, BigDecimal amount, BigDecimal interestRate,
			Integer tenureMonths, BigDecimal emi, BigDecimal remainingAmount, LoanStatus status) {
		super();
		this.loan_id = loan_id;
		this.user = user;
		this.loanType = loanType;
		this.amount = amount;
		this.interestRate = interestRate;
		this.tenureMonths = tenureMonths;
		this.emi = emi;
		this.remainingAmount = remainingAmount;
		this.status = status;
	}
	public Loans() {
		
	}
    
    
    
  
}

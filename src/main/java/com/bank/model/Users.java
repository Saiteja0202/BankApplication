package com.bank.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int user_id;

	@NotBlank(message = "Name cannot be Blank")
	private String name;

	@NotBlank(message = "Email cannot be Blank")
	@Email(message = "Invalid email format")
	@Column(unique = true, nullable = false)
	private String email;

	@NotBlank(message = "Password cannot be Blank")
	@Size(min = 8, message = "Password must be between 8 to 50 characters")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(unique = true)
	private String userToken;

	private LocalDateTime generatedAt;

	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@Enumerated(EnumType.STRING)
	private KycStatus kycStatus;

	public enum UserRole {
		CUSTOMER, ADMIN
	}

	public enum KycStatus {
		PENDING, VERIFIED
	}

	public int OTP;

	public Users() {
	}

	public int getOTP() {
		return OTP;
	}

	public void setOTP(int oTP) {
		OTP = oTP;
	}
	public enum OtpStatus {
	    GENERATE, PENDING, VERIFIED, EXPIRED
	}

	@Enumerated(EnumType.STRING)
	private OtpStatus otpStatus;

	private LocalDateTime otpGeneratedAt;

	public Users(int user_id, @NotBlank(message = "Name cannot be Blank") String name,
			@NotBlank(message = "Email cannot be Blank") @Email(message = "Invalid email format") String email,
			@NotBlank(message = "Password cannot be Blank") @Size(min = 8, message = "Password must be between 8 to 50 characters") String password,
			String userToken, LocalDateTime generatedAt, UserRole userRole, KycStatus kycStatus, int oTP,
			OtpStatus otpStatus, LocalDateTime otpGeneratedAt) {
		super();
		this.user_id = user_id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.userToken = userToken;
		this.generatedAt = generatedAt;
		this.userRole = userRole;
		this.kycStatus = kycStatus;
		OTP = oTP;
		this.otpStatus = otpStatus;
		this.otpGeneratedAt = otpGeneratedAt;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public LocalDateTime getGeneratedAt() {
		return generatedAt;
	}

	public void setGeneratedAt(LocalDateTime generatedAt) {
		this.generatedAt = generatedAt;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public KycStatus getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(KycStatus kycStatus) {
		this.kycStatus = kycStatus;
	}

	public OtpStatus getOtpStatus() {
		return otpStatus;
	}

	public void setOtpStatus(OtpStatus otpStatus) {
		this.otpStatus = otpStatus;
	}

	public LocalDateTime getOtpGeneratedAt() {
		return otpGeneratedAt;
	}

	public void setOtpGeneratedAt(LocalDateTime otpGeneratedAt) {
		this.otpGeneratedAt = otpGeneratedAt;
	}


}
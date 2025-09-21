package com.bank.service;

import com.bank.model.Accounts;

public interface AccountsService {
	
	
	public String createAccount(Integer userId);
	public String changeAccountType(Long accountId, Accounts.AccountType newType);
	public String deleteAccount(Integer userId, Long accountId);

}

package com.bank.service;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.UserNotFoundException;
import com.bank.model.Accounts;
import com.bank.model.Users;
import com.bank.repository.AccountsRepo;
import com.bank.repository.UsersRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;

@Service
public class AccountsServiceImpl implements AccountsService {

    private final AccountsRepo accountsRepo;
    private final UsersRepo usersRepo;

    public AccountsServiceImpl(AccountsRepo accountsRepo, UsersRepo usersRepo) {
        this.accountsRepo = accountsRepo;
        this.usersRepo = usersRepo;
    }

    @Override
    public String createAccount(Integer userId) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        Accounts account = new Accounts();
        account.setUser(user);
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setAccountType(Accounts.AccountType.SAVINGS); // default type
        account.setBalance(BigDecimal.ZERO);

        accountsRepo.save(account);

        return "Account created successfully. Account Number: " + account.getAccountNumber() +account.getAccount_id();
    }

    @Override
    public String changeAccountType(Long accountId, Accounts.AccountType newType) {
        Accounts account = accountsRepo.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));

        account.setAccountType(newType);
        accountsRepo.save(account);

        return "Account type updated to " + newType.name();
    }

    private String generateUniqueAccountNumber() {
        SecureRandom random = new SecureRandom();
        String accountNumber;

        do {
            accountNumber = random.ints(12, 0, 36)
                    .mapToObj(i -> i < 10 ? String.valueOf(i) : String.valueOf((char) ('A' + i - 10)))
                    .reduce("", String::concat)
                    .toUpperCase(Locale.ROOT);
        } while (accountsRepo.existsByAccountNumber(accountNumber));

        return accountNumber;
    }
    
    
    @Override
    public String deleteAccount(Integer userId, Long accountId) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        Accounts account = accountsRepo.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));


        if (account.getUser().getUser_id() != user.getUser_id()) {
            throw new AccountNotFoundException("Account does not belong to this user.");
        }


        accountsRepo.delete(account);
        return "Account with number " + account.getAccountNumber() + " deleted successfully.";
    }
    
    
    @Override
    public List<Accounts> getAccountsByUser(Integer userId) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        return accountsRepo.findByUser(user); // Return even if empty
    }


}

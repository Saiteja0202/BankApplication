package com.bank.controller;

import com.bank.model.Accounts;
import com.bank.service.AccountsService;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping("/create/{userId}")
    public String createAccount(@PathVariable Integer userId) {
        return accountsService.createAccount(userId);
    }

    @PutMapping("/change-type/{accountId}")
    public String changeAccountType(@PathVariable Long accountId,
                                    @RequestBody Map<String, String> request) {
        String type = request.get("newType");
        Accounts.AccountType newType = Accounts.AccountType.valueOf(type.toUpperCase());
        return accountsService.changeAccountType(accountId, newType);
    }
    
    
    @DeleteMapping("/{userId}/delete/{accountId}")
    public String deleteAccount(@PathVariable Integer userId,
                                @PathVariable Long accountId) {
        return accountsService.deleteAccount(userId, accountId);
    }
}

package com.bank.service;

import java.util.HashMap;

import com.bank.model.Users;

public interface UsersService {
	
	public String registerCustomer(Users users);
	public HashMap<String, String> loginCustomer(Users users);
	
	
	public String registerAdmin(Users users);
    public HashMap<String, String> loginAdmin(Users users);
    
    
    public String deleteAccount(Integer userId);
    public String updatePassword(Integer userId, String oldPassword, String newPassword);
    public String updateDetails(Integer userId, Users updatedUser);

}

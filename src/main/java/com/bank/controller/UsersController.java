package com.bank.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.model.Users;
import com.bank.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {

	private UsersService usersServices;

	public UsersController(UsersService usersServices) {

		this.usersServices = usersServices;
	}

	@PostMapping("/customer/register")
	public String registerCustomer(@RequestBody Users users) {
		return usersServices.registerCustomer(users);
	}

	@PostMapping("/customer/login")
	public HashMap<String, String> loginCustomer(@RequestBody Users users) {
		return usersServices.loginCustomer(users);
	}

	@PostMapping("/admin/register")

    public String registerAdmin(@RequestBody Users users) {
        return usersServices.registerAdmin(users);
    }

    @PostMapping("/admin/login")
    public HashMap<String, String> loginAdmin(@RequestBody Users users) {
        return usersServices.loginAdmin(users);
    }
    
    
    
    @PostMapping("/delete/{userId}")
    public String deleteAccount(@PathVariable Integer userId) {
        return usersServices.deleteAccount(userId);
    }

    @PutMapping("/password/{userId}")
    public String updatePassword(@PathVariable Integer userId,
                                 @RequestBody HashMap<String, String> request) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        return usersServices.updatePassword(userId, oldPassword, newPassword);
    }



    @PostMapping("/update-details/{userId}")
    public String updateDetails(@PathVariable Integer userId,
                                @RequestBody Users updatedUser) {
        return usersServices.updateDetails(userId, updatedUser);
    }
    
    
    
    
    @PostMapping("/generate-otp")
    public String generateOtp(@RequestBody HashMap<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        return usersServices.generateOtp(name, email);
    }


    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody HashMap<String, String> request) {
        String email = request.get("email");
        int otp = Integer.parseInt(request.get("otp"));
        return usersServices.verifyOtp(email, otp);
    }

	
	public String registerAdmin(@RequestBody Users users) {
		return usersServices.registerAdmin(users);
	}

	@PostMapping("/admin/login")
	public HashMap<String, String> loginAdmin(@RequestBody Users users) {
		return usersServices.loginAdmin(users);
	}

	@PostMapping("/delete/{userId}")
	public String deleteAccount(@PathVariable Integer userId) {
		return usersServices.deleteAccount(userId);
	}

	@PutMapping("/password/{userId}")
	public String updatePassword(@PathVariable Integer userId, @RequestBody HashMap<String, String> request) {
		String oldPassword = request.get("oldPassword");
		String newPassword = request.get("newPassword");

		return usersServices.updatePassword(userId, oldPassword, newPassword);
	}

	@PostMapping("/update-details/{userId}")
	public String updateDetails(@PathVariable Integer userId, @RequestBody Users updatedUser) {
		return usersServices.updateDetails(userId, updatedUser);
	}


}

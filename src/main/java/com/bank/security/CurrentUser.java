package com.bank.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.bank.model.Users;


@Component
@RequestScope
public class CurrentUser {
	
	private Users currentUser;

	public Users getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Users currentUser) {
		this.currentUser = currentUser;
	}
	
}

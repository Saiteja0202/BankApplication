package com.bank.security;

import java.io.IOException;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bank.exception.UnauthorizedAccessException;
import com.bank.model.Users;
import com.bank.repository.UsersRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final CurrentUser currentUser;
    private final UsersRepo usersRepo;


    public AuthenticationFilter(CurrentUser currentUser, UsersRepo usersRepo) {
        this.currentUser = currentUser;
        this.usersRepo = usersRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        if (path.startsWith("/users")) {
            filterChain.doFilter(request, response);
            return;
        }


        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Basic ")) {
            token = token.substring(6);
        }

        if (token != null && !token.isBlank()) {
            Users users = usersRepo.findByUserToken(token)
                    .orElseThrow(() -> new UnauthorizedAccessException("Invalid or expired token"));

            currentUser.setCurrentUser(users);

        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

package com.example.todo.todo_api.security;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import com.example.todo.todo_api.model.User;
import com.example.todo.todo_api.repository.UserRepository;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        System.out.println("CustomOidcUserService bean initialized");
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        System.out.println("CustomOidcUserService.loadUser() called");
        OidcUser oidcUser = super.loadUser(userRequest);

        System.out.println("OIDC User Name: " + oidcUser.getName());
        System.out.println("OIDC User Authorities: " + oidcUser.getAuthorities());
        System.out.println("OIDC User Claims:");
        oidcUser.getClaims().forEach((key, value) -> System.out.println(key + " = " + value));

        String email = oidcUser.getEmail();
        String name = oidcUser.getName();
        String googleId = oidcUser.getSubject(); 

        if (email == null || googleId == null) {
            System.out.println("Missing required attributes: email=" + email + ", googleId=" + googleId);
            throw new RuntimeException("Required user attributes missing");
        }

        User user = userRepository.findByGoogleId(googleId)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setGoogleId(googleId);
                newUser.setUsername(email); 
                newUser.setRoles(Collections.singleton("ROLE_USER"));
                System.out.println("Creating new user: " + email);
                return newUser;
            });

        user.setEmail(email);
        user.setUsername(email);
        userRepository.save(user);
        System.out.println("Saved/Updated user: " + email);

        return new DefaultOidcUser(
            oidcUser.getAuthorities(),
            oidcUser.getIdToken(),
            oidcUser.getUserInfo(),
            "email" 
        );
    }
}
package com.example.demo;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
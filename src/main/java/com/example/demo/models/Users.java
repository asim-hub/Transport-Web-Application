package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private String email;
    private Integer id;
    private String username;
    private String password;
    private String passwordConfirm;
    private String telefon;
    private String type;
}

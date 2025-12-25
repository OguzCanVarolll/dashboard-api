package com.dev.dashboard.dashboard_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name="users", indexes =  {@Index(columnList = "email")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true,nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String hashPassword;

    public User (String email, String hashPassword){
        this.email = email;
        this.hashPassword = hashPassword;
    }


}


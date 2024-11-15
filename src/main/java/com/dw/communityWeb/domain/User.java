package com.dw.communityWeb.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCode;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(nullable = false)
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Column(length = 15, nullable = false, unique = true)
    private String id;

    @Column(length = 60, nullable = false)
    private String pwd;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Board> boardList = new ArrayList<>();
}
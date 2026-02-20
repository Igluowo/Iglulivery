package com.iglu.iglulivery.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Data
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    private String password;
    private String role;
}

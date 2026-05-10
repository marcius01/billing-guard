package tech.skullprogrammer.bguard.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tech.skullprogrammer.bguard.domain.enumeration.ERole;
import tech.skullprogrammer.bguard.domain.enumeration.EUserStatus;

@Getter
@Setter
@Entity
@Table (name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private ERole role;
    @Enumerated(EnumType.STRING)
    private EUserStatus status;
    private String token;
    private String email;
    private String name;
    private String surname;

}

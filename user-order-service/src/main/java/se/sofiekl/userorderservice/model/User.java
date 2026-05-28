package se.sofiekl.userorderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist //JPA lifecyclehook, runs before saving enity to db first time
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        if(role==null) role="ROLE_USER";
    }

}

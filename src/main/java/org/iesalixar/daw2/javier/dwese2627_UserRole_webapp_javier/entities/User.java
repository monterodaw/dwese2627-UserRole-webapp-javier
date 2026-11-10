package org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

/**
 * Entidad JPA para la tabla 'users'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    /** BIGINT AUTO_INCREMENT PRIMARY KEY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** VARCHAR(100) NOT NULL UNIQUE */
    @Column(name = "email", nullable = false, unique = true, length = 40)
    private String email;

    /** VARCHAR(500) NOT NULL */
    @Column(name = "password_hash", nullable = false, length = 500)
    private String passwordHash;

    /** BOOLEAN NOT NULL DEFAULT TRUE */
    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    /** BOOLEAN NOT NULL DEFAULT TRUE */
    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked = Boolean.TRUE;

    /** DATETIME NULL */
    @Column(name = "last_password_change")
    private LocalDateTime lastPasswordChange;

    /** DATETIME NULL */
    @Column(name = "password_expires_at")
    private LocalDateTime passwordExpiresAt;

    /** INT DEFAULT 0 */
    @Column(name = "failed_login_attempts", nullable = false)
    private Integer failedLoginAttempts = 0;

    /** BOOLEAN NOT NULL DEFAULT FALSE */
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = Boolean.FALSE;

    /** BOOLEAN NOT NULL DEFAULT FALSE */
    @Column(name = "must_change_password", nullable = false)
    private Boolean mustChangePassword = Boolean.FALSE;
}

package org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.dtos;

import java.time.LocalDateTime;

public class UserDetailDTO {
    private Long id;
    private String email;
    private String passwordHash;
    private Boolean active;
    private Boolean accountNonLocked;
    private LocalDateTime lastPasswordChange;
    private LocalDateTime passwordExpiresAt;
    private Integer failedLoginAttempts;
    private Boolean emailVerified;
    private Boolean mustChangePassword;
}

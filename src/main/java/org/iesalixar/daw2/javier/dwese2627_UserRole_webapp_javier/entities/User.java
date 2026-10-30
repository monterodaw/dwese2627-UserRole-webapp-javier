package org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/** Usuario de la aplicación (tabla users). Puede tener uno o varios roles. */
@Data
@NoArgsConstructor
public class User {
    private Long id;
    @NotEmpty(message = "{msg.user.username.notEmpty}")
    @Size(max = 40, message = "{msg.user.username.size}")
    private String username;
    @NotEmpty(message = "{msg.user.password.notEmpty}")
    @Size(max = 500, message = "{msg.user.password.size}")
    private String passwordHash;
    private Boolean active = true;
    private Boolean accountNonLocked = true;
    @NotNull(message = "{msg.user.lastPasswordChange.notNull}")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime lastPasswordChange;
    // Se calcula en el controlador (lastPasswordChange + 3 meses), no viene del formulario
    private LocalDateTime passwordExpiresAt;
    @NotNull(message = "{msg.user.failedLoginAttempts.notNull}")
    @Min(value = 0, message = "{msg.user.failedLoginAttempts.min}")
    private Integer failedLoginAttempts = 0;
    private Boolean emailVerified = false;
    private Boolean mustChangePassword = false;
    // IDs de los roles marcados en el formulario (los checkboxes se enlazan con este campo)
    @NotEmpty(message = "{msg.user.roles.notEmpty}")
    private List<Long> roleIds = new ArrayList<>();
    // Roles completos del usuario (los carga el DAO para mostrarlos en la lista y el detalle)
    private List<Role> roles = new ArrayList<>();
}

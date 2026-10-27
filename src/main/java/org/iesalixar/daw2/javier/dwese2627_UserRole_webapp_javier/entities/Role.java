package org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/** Rol de la aplicación (tabla roles). Un rol puede estar en varios usuarios. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    @NotEmpty(message = "{msg.role.name.notEmpty}")
    @Size(max = 50, message = "{msg.role.name.size}")
    private String name;
    public Role(String name) {
        this.name = name;
    }
}

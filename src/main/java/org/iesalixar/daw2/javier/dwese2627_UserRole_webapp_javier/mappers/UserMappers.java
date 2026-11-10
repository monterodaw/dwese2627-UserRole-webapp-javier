package org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.mappers;

import org.hibernate.type.EntityType;
import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.dtos.UserDTO;
import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.entities.User;

public class UserMappers {
    public static UserDTO toDTO(User entity) {
        if (entity == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setPasswordHash(entity.getPasswordHash());
        dto.setActive(entity.getActive());
        dto.setAccountNonLocked(entity.getAccountNonLocked());
        dto.setLastPasswordChange(entity.getLastPasswordChange());
        dto.setPasswordExpiresAt(entity.getPasswordExpiresAt());
        dto.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        dto.setEmailVerified(entity.getEmailVerified());
        dto.setMustChangePassword(entity.getMustChangePassword());
        return dto;
    }
}


package org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.Daos;

import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.entities.Role;
import java.util.List;
public interface RoleDAO {
    List<Role> listAllRoles();
    Role getRoleById(Long id);
    void insertRole(Role role);
    void updateRole(Role role);
    void deleteRole(Long id);
    boolean existsRoleByName(String name);
    boolean existsRoleByNameAndNotId(String name, Long id);
}
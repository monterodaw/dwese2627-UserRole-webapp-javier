package org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.daos;

import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.entities.User;

import java.util.List;

public interface UserDAO {
    List<User> listAllUsers();
    User getUserById(Long id);
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    boolean existsUserByUsername(String username);
    boolean existsUserByUsernameAndNotId(String username, Long id);
}

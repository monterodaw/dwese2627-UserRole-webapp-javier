package org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.Daos;

import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.entities.Role;
import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
    private final JdbcTemplate jdbcTemplate;
    public UserDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
// ---------- Métodos privados para la tabla intermedia user_roles ----------
    /** Devuelve los roles de un usuario (JOIN entre roles y user_roles). */
    private List<Role> findRolesByUserId(Long userId) {
        String sql = "SELECT r.id, r.name FROM roles r "
                + "JOIN user_roles ur ON ur.role_id = r.id "
                + "WHERE ur.user_id = ? ORDER BY r.name";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Role.class), userId);
    }
    /** Rellena user.roles (objetos) y user.roleIds (ids para marcar los checkboxes). */
    private void fillRoles(User user) {
        List<Role> roles = findRolesByUserId(user.getId());
        user.setRoles(roles);
        user.setRoleIds(roles.stream().map(Role::getId).collect(Collectors.toList()));
    }
    /** Sustituye los roles del usuario: borra los que tenía e inserta los marcados. */
    private void saveUserRoles(Long userId, List<Long> roleIds) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id = ?", userId);
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                jdbcTemplate.update(
                        "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)", userId, roleId);
            }
        }
    }
    // ---------- CRUD ----------
    @Override
    public List<User> listAllUsers() {
        String sql = "SELECT * FROM users ORDER BY id";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        for (User user : users) {
            fillRoles(user);
        }
        return users;
    }
    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
            fillRoles(user);
            return user;
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No user found with id: {}", id);
            return null;
        }
    }
    /** @Transactional: o se guardan el usuario Y sus roles, o no se guarda nada. */
    @Override
    @Transactional
    public void insertUser(User user) {
        String sql = "INSERT INTO users (username, password_hash, active, account_non_locked, "
                + "last_password_change, password_expires_at, failed_login_attempts, "
                + "email_verified, must_change_password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
// aquí nos devuelve el id nuevo
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setBoolean(3, user.getActive());
            ps.setBoolean(4, user.getAccountNonLocked());
            ps.setObject(5, user.getLastPasswordChange());
            ps.setObject(6, user.getPasswordExpiresAt());
            ps.setInt(7, user.getFailedLoginAttempts());
            ps.setBoolean(8, user.getEmailVerified());
            ps.setBoolean(9, user.getMustChangePassword());
            return ps;
        }, keyHolder);
        Long newId = keyHolder.getKey().longValue();
        saveUserRoles(newId, user.getRoleIds());
    }
    @Override
    @Transactional
    public void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password_hash = ?, active = ?, "
                + "account_non_locked = ?, last_password_change = ?, password_expires_at = ?, "
                + "failed_login_attempts = ?, email_verified = ?, must_change_password = ? "
                + "WHERE id = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getPasswordHash(), user.getActive(),
                user.getAccountNonLocked(), user.getLastPasswordChange(),
                user.getPasswordExpiresAt(), user.getFailedLoginAttempts(),
                user.getEmailVerified(), user.getMustChangePassword(), user.getId());
        saveUserRoles(user.getId(), user.getRoleIds());
    }
    /** Al borrar el usuario, la BD borra sus filas de user_roles sola (ON DELETE CASCADE). */
    @Override
    public void deleteUser(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
    @Override
    public boolean existsUserByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE UPPER(username) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username.toUpperCase());
        return count != null && count > 0;
    }
    @Override
    public boolean existsUserByUsernameAndNotId(String username, Long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE UPPER(username) = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(
                sql, Integer.class, username.toUpperCase(), id);
        return count != null && count > 0;
    }
}
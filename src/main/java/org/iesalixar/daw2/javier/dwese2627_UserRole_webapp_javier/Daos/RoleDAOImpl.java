package org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.Daos;

import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDAOImpl implements RoleDAO {
    private static final Logger logger = LoggerFactory.getLogger(RoleDAOImpl.class);
    private final JdbcTemplate jdbcTemplate;
    public RoleDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Role> listAllRoles() {
        String sql = "SELECT * FROM roles ORDER BY name";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Role.class));
    }
    @Override
    public Role getRoleById(Long id) {
        String sql = "SELECT * FROM roles WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Role.class), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No role found with id: {}", id);
            return null;
        }
    }
    @Override
    public void insertRole(Role role) {
        String sql = "INSERT INTO roles (name) VALUES (?)";
        jdbcTemplate.update(sql, role.getName());
    }
    @Override
    public void updateRole(Role role) {
        String sql = "UPDATE roles SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, role.getName(), role.getId());
    }
    @Override
    public void deleteRole(Long id) {
        String sql = "DELETE FROM roles WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    @Override
    public boolean existsRoleByName(String name) {
        String sql = "SELECT COUNT(*) FROM roles WHERE UPPER(name) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name.toUpperCase());
        return count != null && count > 0;
    }
    @Override
    public boolean existsRoleByNameAndNotId(String name, Long id) {
        String sql = "SELECT COUNT(*) FROM roles WHERE UPPER(name) = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name.toUpperCase(), id);
        return count != null && count > 0;
    }
}

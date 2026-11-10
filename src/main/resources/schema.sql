-- Roles de la aplicación
CREATE TABLE IF NOT EXISTS roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);
-- Crear tabla users si no existe
CREATE TABLE IF NOT EXISTS users (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   email VARCHAR(100) NOT NULL UNIQUE,
   password_hash VARCHAR(500) NOT NULL,
   active BOOLEAN NOT NULL DEFAULT TRUE,
   account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
   last_password_change DATETIME NULL,
   password_expires_at DATETIME NULL,
   failed_login_attempts INT DEFAULT 0,
   email_verified BOOLEAN NOT NULL DEFAULT FALSE,
   must_change_password BOOLEAN NOT NULL DEFAULT FALSE
);

-- Tabla intermedia de la relación N:M: una fila = "este usuario tiene este rol"
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id)
    );

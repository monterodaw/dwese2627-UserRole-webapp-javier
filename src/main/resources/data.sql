INSERT IGNORE INTO roles (id, name) VALUES
(1, 'ADMIN'),
(2, 'USER'),
(3, 'MANAGER');
-- Inserta si no existe (ignora duplicados por UNIQUE username o id)
INSERT IGNORE INTO users (
   id, email, password_hash, active, account_non_locked,
   last_password_change, password_expires_at, failed_login_attempts,
   email_verified, must_change_password
) VALUES
(1, 'admin@app.local',        'admin123',  TRUE,  TRUE,  NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE,  FALSE),
(2, 'jdoe@app.local',         '1234',      TRUE,  TRUE,  NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 1, FALSE, FALSE),
(3, 'maria@app.local',        'changeme',  TRUE,  TRUE,  NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE,  TRUE ),
(4, 'blockeduser@app.local',  'secret',    FALSE, FALSE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 5, FALSE, FALSE);

INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(1, 1), (1, 2),
(2, 2),
(3, 3), (3, 2),
(4, 2);
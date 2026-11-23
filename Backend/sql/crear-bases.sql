-- Respaldo: crea las bases de los tres microservicios.
-- Normalmente no hace falta: cada microservicio crea su base al arrancar
-- (createDatabaseIfNotExist=true). Usar si eso falla, desde phpMyAdmin > SQL.

CREATE DATABASE IF NOT EXISTS corpaliv_productos
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS corpaliv_programas
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS corpaliv_colaboraciones
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

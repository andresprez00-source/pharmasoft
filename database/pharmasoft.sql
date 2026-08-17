CREATE DATABASE IF NOT EXISTS pharmasoft
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE pharmasoft;

CREATE TABLE IF NOT EXISTS productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    categoria VARCHAR(80) NOT NULL,
    precio DECIMAL(12,2) NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    fecha_vencimiento DATE NULL
);

INSERT INTO productos (codigo, nombre, categoria, precio, cantidad, fecha_vencimiento)
VALUES
('MED001', 'Acetaminofen 500 mg', 'Analgesicos', 5000.00, 50, '2028-06-30'),
('MED002', 'Ibuprofeno 400 mg', 'Antiinflamatorios', 7000.00, 30, '2027-11-30')
ON DUPLICATE KEY UPDATE codigo = codigo;

CREATE DATABASE galeria_arte_0_1 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE galeria_arte_0_1;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) DEFAULT 'comprador',
    estado VARCHAR(20) DEFAULT 'activo'
);

CREATE TABLE portafolios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    artista_id INT NOT NULL,
    biografia TEXT,
    link_portafolio VARCHAR(255),
    FOREIGN KEY (artista_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE obras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    url_imagen VARCHAR(255),
    artista_id INT,
    estado VARCHAR(20) NOT NULL DEFAULT 'pendiente',
    motivo_rechazo TEXT,
    FOREIGN KEY (artista_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    obra_id INT NOT NULL,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (obra_id) REFERENCES obras(id)
);

CREATE TABLE favoritos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    obra_id INT NOT NULL,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_usuario_obra (usuario_id, obra_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (obra_id) REFERENCES obras(id)
);

INSERT INTO usuarios (nombre, email, password, rol, estado) VALUES
('Admin', 'admin@galeria.com', '12345', 'admin', 'activo'),
('María Arte', 'artista@galeria.com', '12345', 'artista', 'activo'),
('Juan Comprador', 'comprador@galeria.com', '12345', 'comprador', 'activo');

INSERT INTO obras (titulo, descripcion, precio, url_imagen, artista_id, estado) VALUES
('Atardecer Urbano', 'Pintura abstracta inspirada en la ciudad', 250000, '../Imagenes/arte1.jpeg', 2, 'aprobada'),
('Rostro en Neón', 'Retrato contemporáneo con técnica mixta', 180000, '../Imagenes/arte2.jpeg', 2, 'pendiente'),
('Sueños Azules', 'Serie conceptual sobre el paisaje urbano', 310000, '../Imagenes/arte3.jpeg', 2, 'aprobada');
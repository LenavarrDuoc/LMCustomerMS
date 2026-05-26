-- 1. Crear tabla de Clientes primero (lado "Uno" de la relación)
CREATE TABLE cliente (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         num_run INT NOT NULL UNIQUE,
                         dv_run VARCHAR(1) NOT NULL,
                         p_nombre VARCHAR(100) NOT NULL,
                         s_nombre VARCHAR(100) NULL,
                         ap_paterno VARCHAR(100) NOT NULL,
                         ap_materno VARCHAR(100) NOT NULL,
                         email VARCHAR(150) NOT NULL,
                         fono VARCHAR(12) NOT NULL, -- Configurado a 12 caracteres según tu @Length
                         fec_nacimiento DATE NOT NULL,
                         fec_creacion DATETIME NOT NULL,
                         fec_actualizacion DATETIME NOT NULL
);

-- 2. Crear tabla de Direcciones (lado "Muchos" de la relación)
CREATE TABLE direccion (
                           direccion_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           numero INT NOT NULL,
                           nro_depto INT NULL,
                           calle VARCHAR(150) NOT NULL,
                           comuna VARCHAR(100) NOT NULL,
                           region VARCHAR(100) NOT NULL,
                           es_Default BOOLEAN DEFAULT FALSE,
                           fk_cliente_id BIGINT NOT NULL,

    -- Restricción de Clave Foránea
                           CONSTRAINT fk_direccion_cliente FOREIGN KEY (fk_cliente_id)
                               REFERENCES cliente(id) ON DELETE CASCADE,

    -- Tu restricción UNIQUE compuesta (uk_direccion_por_cliente)
                           CONSTRAINT uk_direccion_por_cliente UNIQUE (calle, numero, nro_depto, comuna, fk_cliente_id)
);
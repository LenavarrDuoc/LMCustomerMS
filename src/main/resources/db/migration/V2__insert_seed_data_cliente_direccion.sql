-- 1. Insertar Datos de Clientes
INSERT INTO cliente (
    num_run, dv_run, p_nombre, s_nombre, ap_paterno, ap_materno,
    email, fono, fec_nacimiento, fec_creacion, fec_actualizacion
) VALUES
      (
          11111111, '1', 'Carlos', 'Andrés', 'Silva', 'Araya',
          'carlos.silva@email.com', '+56911111111', '1988-03-12', NOW(), NOW()
      ),
      (
          22222222, '2', 'Ana', 'Luisa', 'Tapia', 'Rojas',
          'ana.tapia@email.com', '+56922222222', '1993-07-19', NOW(), NOW()
      );

-- 2. Insertar Datos de Direcciones vinculadas a los Clientes
INSERT INTO direccion (
    numero, nro_depto, calle, comuna, region, es_Default, fk_cliente_id
) VALUES
-- Dirección para el Cliente 1 (Carlos)
(
    1420, NULL, 'Avenida Providencia', 'Providencia', 'Región Metropolitana', TRUE, 1
),
-- Primera dirección para el Cliente 2 (Ana - Casa)
(
    543, NULL, 'Calle Los Aromos', 'Viña del Mar', 'Región de Valparaíso', TRUE, 2
),
-- Segunda dirección para el Cliente 2 (Ana - Departamento Trabajo)
(
    90, 402, 'Paseo Ahumada', 'Santiago', 'Región Metropolitana', FALSE, 2
);
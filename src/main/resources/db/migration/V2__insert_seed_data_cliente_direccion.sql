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
-- ====================================================================
-- Versión Corregida para calzar con la estructura real de direccion
-- ====================================================================

INSERT INTO direccion (
    direccion_id,
    es_default,
    nro_depto,
    numero,
    fk_cliente_id,
    calle,
    comuna,
    region
) VALUES
-- Dirección para el Cliente 1 (Carlos) - ID de dirección: 1
(
    1,
    TRUE,
    NULL,
    1420,
    1,
    'Avenida Providencia',
    'Providencia',
    'Región Metropolitana'
),
-- Primera dirección para el Cliente 2 (Ana - Casa) - ID de dirección: 2
(
    2,
    TRUE,
    NULL,
    543,
    2,
    'Calle Los Aromos',
    'Viña del Mar',
    'Región de Valparaíso'
),
-- Segunda dirección para el Cliente 2 (Ana - Trabajo) - ID de dirección: 3
(
    3,
    FALSE,
    402,
    90,
    2,
    'Paseo Ahumada',
    'Santiago',
    'Región Metropolitana'
);
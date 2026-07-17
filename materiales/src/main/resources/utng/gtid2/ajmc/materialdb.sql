

-- ---------- Catálogo de tipos de equipo (cmbTipo) ----------
CREATE TABLE tipos_equipo (
    id_tipo         SERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL UNIQUE,
    descripcion     TEXT
);

-- ---------- Responsables (cmbResponsable) ----------
CREATE TABLE responsables (
    id_responsable  SERIAL PRIMARY KEY,
    nombre_completo VARCHAR(150) NOT NULL,
    departamento    VARCHAR(100),
    email           VARCHAR(120) UNIQUE,
    telefono        VARCHAR(20)
);

-- ---------- Equipos (tablaEquipos / formularios Registrar-Editar) ----------
CREATE TABLE equipos (
    id_equipo         SERIAL PRIMARY KEY,
    codigo            VARCHAR(30) NOT NULL UNIQUE,     -- colCodigo / txtCodigo
    nombre            VARCHAR(150) NOT NULL,            -- colNombre / txtNombre
    id_tipo           INTEGER NOT NULL REFERENCES tipos_equipo(id_tipo),      -- colTipo / cmbTipo
    id_responsable    INTEGER REFERENCES responsables(id_responsable),        -- colResponsable / cmbResponsable
    estado            VARCHAR(20) NOT NULL DEFAULT 'Activo'                   -- colEstado / cmbEstado
                      CHECK (estado IN ('Activo','En reparación','En resguardo','De baja')),
    descripcion       TEXT,                              -- txtDescripcion
    fecha_registro    TIMESTAMP NOT NULL DEFAULT NOW()    -- colFechaRegistro
);

-- ---------- Bajas de equipo (EquiposBaja.fxml) ----------
CREATE TABLE bajas_equipo (
    id_baja         SERIAL PRIMARY KEY,
    id_equipo       INTEGER NOT NULL UNIQUE REFERENCES equipos(id_equipo) ON DELETE CASCADE,
    motivo          VARCHAR(30) NOT NULL                  -- cmbMotivoBaja
                    CHECK (motivo IN ('Obsolescencia','Daño irreparable','Robo/Extravío','Venta','Donación','Otro')),
    observaciones   TEXT,                                 -- txtObservacionesBaja
    fecha_baja      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Índices de apoyo para consultas y filtros de la tabla principal
CREATE INDEX idx_equipos_tipo ON equipos(id_tipo);
CREATE INDEX idx_equipos_responsable ON equipos(id_responsable);
CREATE INDEX idx_equipos_estado ON equipos(estado);
CREATE INDEX idx_bajas_equipo ON bajas_equipo(id_equipo);

-- Trigger: al confirmar una baja (btnConfirmarBaja), marcar el equipo como "De baja"
CREATE OR REPLACE FUNCTION fn_marcar_equipo_baja()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE equipos SET estado = 'De baja' WHERE id_equipo = NEW.id_equipo;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_marcar_equipo_baja
AFTER INSERT ON bajas_equipo
FOR EACH ROW
EXECUTE FUNCTION fn_marcar_equipo_baja();

-- ============================================================
-- DATOS SEMILLA
-- ============================================================

INSERT INTO tipos_equipo (nombre, descripcion) VALUES
('Cómputo', 'Equipos de escritorio, laptops y servidores'),
('Mobiliario', 'Escritorios, sillas y muebles de oficina'),
('Herramienta', 'Herramientas manuales y eléctricas'),
('Equipo de laboratorio', 'Instrumentos y equipo especializado de laboratorio'),
('Vehículo', 'Vehículos de uso institucional');

INSERT INTO responsables (nombre_completo, departamento, email, telefono) VALUES
('Laura Hernández Solís', 'Sistemas', 'lhernandez@empresa.mx', '4151234567'),
('Ricardo Peña Morales', 'Mantenimiento', 'rpena@empresa.mx', '4157654321'),
('Karla Jiménez Ortiz', 'Laboratorio', 'kjimenez@empresa.mx', '4159988776');

INSERT INTO equipos (codigo, nombre, id_tipo, id_responsable, estado, descripcion) VALUES
('EQ-0001', 'Laptop Dell Latitude', 1, 1, 'Activo', 'Laptop asignada al área de Sistemas'),
('EQ-0002', 'Escritorio ejecutivo', 2, 1, 'Activo', 'Escritorio en oficina de Sistemas'),
('EQ-0003', 'Taladro industrial', 3, 2, 'En reparación', 'Taladro Bosch en revisión por falla eléctrica'),
('EQ-0004', 'Microscopio óptico', 4, 3, 'Activo', 'Microscopio Olympus para Laboratorio A'),
('EQ-0005', 'Impresora HP LaserJet', 1, 1, 'De baja', 'Impresora reemplazada por modelo más reciente');

INSERT INTO bajas_equipo (id_equipo, motivo, observaciones) VALUES
(5, 'Obsolescencia', 'Equipo reemplazado por modelo más reciente; se dona a taller escolar');
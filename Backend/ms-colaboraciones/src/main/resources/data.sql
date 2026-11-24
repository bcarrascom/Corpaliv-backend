-- Solicitudes de ejemplo para el panel de vinculación. Datos ficticios.
-- IDs fijos + INSERT IGNORE: si la fila ya existe, se salta y no se duplica.
-- Fechas en 2027: la fecha de contacto debe ser hoy o futura, así se pueden editar sin error.

INSERT IGNORE INTO colaboraciones
(id, nombre_contacto, organizacion, correo, tipo_colaboracion, cantidad_personas, fecha_contacto, acepta_contacto, estado) VALUES
(1, 'Camila Rojas', 'Empresa Ficticia SpA', 'camila.rojas@ejemplo.cl', 'Alianza', 3, '2027-03-10', TRUE, 'Pendiente'),
(2, 'Matías Fuentes', 'Particular', 'matias.fuentes@ejemplo.cl', 'Voluntariado', 1, '2027-03-15', TRUE, 'Contactado'),
(3, 'Valentina Soto', 'Colegio Los Aromos', 'valentina.soto@ejemplo.cl', 'Capacitación', 25, '2027-04-05', TRUE, 'Cerrada');

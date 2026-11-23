-- Catálogo inicial de Alba Lab. Se ejecuta cada vez que arranca el microservicio.
-- IDs fijos + INSERT IGNORE: si la fila ya existe, se salta y no se duplica.
-- precio NULL = precio a convenir (servicios).

INSERT IGNORE INTO productos (id, nombre, descripcion, precio, categoria, disponible) VALUES
(1, 'Velas aromáticas artesanales', 'Velas de cera de soya elaboradas en el taller de Alba Lab.', 6990, 'Producto', TRUE),
(2, 'Jabones naturales', 'Jabones hechos a mano con aceites esenciales.', 3990, 'Producto', TRUE),
(3, 'Caja de regalo corporativa', 'Set de productos del taller para regalos de empresa o eventos.', 19990, 'Producto', TRUE),
(4, 'Tarjetas y papelería artesanal', 'Tarjetas de saludo y libretas hechas con papel reciclado.', 2490, 'Producto', FALSE),
(5, 'Servicio de catering inclusivo', 'Coffee break y colaciones para reuniones, preparadas por el equipo de Alba Lab.', NULL, 'Servicio', TRUE),
(6, 'Taller de sensibilización para empresas', 'Capacitación sobre inclusión laboral de personas con discapacidad, como parte de la RSE.', NULL, 'Servicio', TRUE);

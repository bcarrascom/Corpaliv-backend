INSERT INTO programas
(id, nombre, resumen, descripcion, categoria, imagen_url,
 estado_publicacion, activo, fecha_creacion, fecha_actualizacion)
VALUES
(1,
 'Programa de Inclusión Laboral',
 'Promovemos la inclusión laboral de personas con discapacidad múltiple.',
 'Brindamos una variedad de mecanismos diseñados específicamente para promover y facilitar la inclusión laboral de personas con discapacidad múltiple. Estos mecanismos están orientados a ayudarlas a integrarse de manera efectiva y exitosa en el mundo laboral, garantizando así que puedan desarrollar sus habilidades y alcanzar su máximo potencial en sus entornos de trabajo.',
 'Inclusión laboral',
 NULL,
 'PUBLICADO',
 TRUE,
 CURRENT_TIMESTAMP,
 CURRENT_TIMESTAMP),

(2,
 'Taller Sociolaboral',
 'Desarrollamos habilidades sociales, emocionales y laborales para fortalecer la autonomía y la empleabilidad.',
 'Nos enfocamos en la formación integral de personas, brindándoles habilidades sociales, emocionales y laborales que les permitan integrarse con éxito al mundo laboral. A través de diversas metodologías, fortalecemos su independencia, autonomía y competencias de empleabilidad. Además de estas habilidades, también nos dedicamos al diseño, fabricación y producción de productos con valor y sentido, fomentando un ambiente de colaboración, aprendizaje y crecimiento mutuo entre beneficiarios y emprendimientos.',
 'Formación sociolaboral',
 NULL,
 'PUBLICADO',
 TRUE,
 CURRENT_TIMESTAMP,
 CURRENT_TIMESTAMP)

ON DUPLICATE KEY UPDATE
nombre = VALUES(nombre),
resumen = VALUES(resumen),
descripcion = VALUES(descripcion),
categoria = VALUES(categoria),
imagen_url = VALUES(imagen_url),
estado_publicacion = VALUES(estado_publicacion),
activo = VALUES(activo),
fecha_actualizacion = CURRENT_TIMESTAMP;


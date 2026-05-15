DROP DATABASE IF EXISTS SistemaNotas;
CREATE DATABASE SistemaNotas;
USE SistemaNotas;

CREATE TABLE alumnos (
    carnet VARCHAR(15) NOT NULL PRIMARY KEY,
    nombres VARCHAR(60) NOT NULL,
    apellidos VARCHAR(60) NOT NULL,
    seccion CHAR(1) NOT NULL
);

CREATE TABLE notas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    carnet VARCHAR(15) NOT NULL,
    zona DECIMAL(5,2) DEFAULT 0,
    examen DECIMAL(5,2) DEFAULT 0,
    nota_final DECIMAL(5,2) DEFAULT 0,
    FOREIGN KEY (carnet) REFERENCES alumnos(carnet) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO alumnos VALUES ('2024001', 'Carlos Andres', 'Lopez Garcia', 'A');
INSERT INTO alumnos VALUES ('2024002', 'Maria Jose', 'Perez Hernandez', 'A');
INSERT INTO alumnos VALUES ('2024003', 'Pedro Luis', 'Martinez Ruiz', 'A');
INSERT INTO notas (carnet, zona, examen, nota_final) VALUES ('2024001', 35, 30, 65);
INSERT INTO notas (carnet, zona, examen, nota_final) VALUES ('2024002', 40, 35, 75);
INSERT INTO notas (carnet, zona, examen, nota_final) VALUES ('2024003', 38, 28, 66);

INSERT INTO alumnos VALUES ('2024004', 'Ana Sofia', 'Gomez Torres', 'B');
INSERT INTO alumnos VALUES ('2024005', 'Luis Fernando', 'Vasquez Morales', 'B');
INSERT INTO alumnos VALUES ('2024006', 'Gabriela', 'Cifuentes Paz', 'B');
INSERT INTO notas (carnet, zona, examen, nota_final) VALUES ('2024004', 42, 38, 80);
INSERT INTO notas (carnet, zona, examen, nota_final) VALUES ('2024005', 30, 25, 55);
INSERT INTO notas (carnet, zona, examen, nota_final) VALUES ('2024006', 45, 40, 85);

SELECT * FROM alumnos;
SELECT * FROM notas;
SELECT a.carnet, a.nombres, a.apellidos, a.seccion,
       n.zona, n.examen, n.nota_final
FROM alumnos a
LEFT JOIN notas n ON a.carnet = n.carnet
ORDER BY a.seccion, a.carnet;


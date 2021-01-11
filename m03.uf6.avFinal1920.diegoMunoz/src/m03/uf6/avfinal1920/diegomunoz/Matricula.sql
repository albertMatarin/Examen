/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  jmartin
 */

DROP DATABASE IF EXISTS matricula;
CREATE DATABASE matricula;
DROP USER IF EXISTS 'admin_matricula'@'localhost';
CREATE USER 'admin_matricula'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON matricula.* TO 'admin_matricula'@'localhost';
USE matricula;

CREATE TABLE assignatures(
  codi INT NOT NULL,
  nom VARCHAR(40) NOT NULL,
  credits INT NOT NULL,
  PRIMARY KEY(codi)
);

CREATE TABLE alumnes(
  nif VARCHAR(9) NOT NULL,
  nom VARCHAR(30),
  cognoms VARCHAR(30),
  PRIMARY KEY(nif)
);

CREATE TABLE matriculacions(
  codi_assignatura INT NOT NULL,
  nif_alumne VARCHAR(9) NOT NULL,
  PRIMARY KEY(codi_assignatura, nif_alumne)
);

ALTER TABLE matriculacions ADD CONSTRAINT fk_matriculacions_codi_assignatura
 FOREIGN KEY (codi_assignatura) REFERENCES assignatures(codi), ADD CONSTRAINT 
 fk_matriculacions_nif_alumne FOREIGN KEY (nif_alumne) REFERENCES alumnes(nif);

INSERT INTO assignatures VALUES(103801, 'Àlgebra', 6);
INSERT INTO assignatures VALUES(103802, 'Càlcul', 6);
INSERT INTO assignatures VALUES(102771, 'Electricitat i Electrònica', 9);
INSERT INTO assignatures VALUES(103806, 'Fonaments d''Informàtica', 9);
INSERT INTO assignatures VALUES(102765, 'Fonaments dels Computadors', 6);
INSERT INTO assignatures VALUES(102772, 'Matemàtica Discreta', 6);
INSERT INTO assignatures VALUES(102764, 'Metodologia de la Programació', 6);
INSERT INTO assignatures VALUES(103807, 'Organització i Gestió d''Empreses', 6);
INSERT INTO assignatures VALUES(103805, 'Fonaments d''Enginyeria', 6);

INSERT INTO alumnes VALUES('19613579F','Victor','Rios Marti');
INSERT INTO alumnes VALUES('79310413L','Vanessa','Rebollo Serna');
INSERT INTO alumnes VALUES('35353776Q','Lorenzo','Becerra Mendoza');
INSERT INTO alumnes VALUES('85621610Q','Francisco Antonio','Aguilera Barrera');
INSERT INTO alumnes VALUES('41417986T','Fernando','Rodríguez Jiménez');
INSERT INTO alumnes VALUES('93526017J','María Carmen','Antequera Izquierdo');
INSERT INTO alumnes VALUES('57392013K','Jesús','Artola Vázquez');

INSERT INTO matriculacions VALUES(103801,'19613579F');
INSERT INTO matriculacions VALUES(103802,'19613579F');

DELIMITER //
CREATE PROCEDURE matricula(IN codi INT, IN nif VARCHAR(9))
BEGIN
    INSERT INTO matriculacions VALUES(codi, nif);
END
//
DELIMITER ;

CALL matricula(102771, '19613579F');





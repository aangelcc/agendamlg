-- -----------------------------------------------------
-- Schema AgendaMLG
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table AgendaMLG.Evento
-- -----------------------------------------------------
CREATE TABLE Evento (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
  tipo DECIMAL(1,0) NOT NULL,
  nombre VARCHAR(45) NOT NULL,
  descripcion LONG VARCHAR NOT NULL,
  fecha TIMESTAMP NOT NULL,
  precio DECIMAL(9,2),
  direccion VARCHAR(200) NOT NULL,
  validado DECIMAL(1,0) NOT NULL DEFAULT 0,
  likes INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table AgendaMLG.Archivo
-- -----------------------------------------------------
CREATE TABLE Archivo (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
  ruta VARCHAR(500) NOT NULL,
  Evento_id INT,
  PRIMARY KEY (id),
  CONSTRAINT fk_Archivo_Evento1
    FOREIGN KEY (Evento_id)
    REFERENCES Evento (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table AgendaMLG.Usuario
-- -----------------------------------------------------
CREATE TABLE Usuario (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
  tipo DECIMAL(1,0) NOT NULL,
  alias VARCHAR(45) NOT NULL,
  nombre VARCHAR(45) NOT NULL,
  apellidos VARCHAR(90) NOT NULL,
  email VARCHAR(45) NOT NULL,
  password VARCHAR(500) NOT NULL,
  foto INT,
  fechaNacimiento DATE NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_Usuario_Archivo1
    FOREIGN KEY (foto)
    REFERENCES Archivo (id)
    ON DELETE SET NULL
    ON UPDATE NO ACTION);

ALTER TABLE USUARIO 
ADD CONSTRAINT ALIAS_UNICO UNIQUE (alias);

ALTER TABLE USUARIO 
ADD CONSTRAINT EMAIL_UNICO UNIQUE (email);

-- -----------------------------------------------------
-- Table AgendaMLG.Categoria
-- -----------------------------------------------------
CREATE TABLE Categoria (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
  nombre VARCHAR(45) NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE CATEGORIA 
ADD CONSTRAINT NOMBRE_UNICO UNIQUE (nombre);

-- -----------------------------------------------------
-- Table AgendaMLG.CategoriaEvento
-- -----------------------------------------------------
CREATE TABLE CategoriaEvento (
  Evento_id INT NOT NULL,
  Categoria_id INT NOT NULL,
  PRIMARY KEY (Evento_id, Categoria_id),
  CONSTRAINT fk_Evento_has_Categoria_Evento
    FOREIGN KEY (Evento_id)
    REFERENCES Evento (id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Evento_has_Categoria_Categoria1
    FOREIGN KEY (Categoria_id)
    REFERENCES Categoria (id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table AgendaMLG.Preferencias
-- -----------------------------------------------------
CREATE TABLE Preferencias (
  Usuario_id INT NOT NULL,
  Categoria_id INT NOT NULL,
  PRIMARY KEY (Usuario_id, Categoria_id),
  CONSTRAINT fk_Usuario_has_Categoria_Usuario1
    FOREIGN KEY (Usuario_id)
    REFERENCES Usuario (id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Usuario_has_Categoria_Categoria1
    FOREIGN KEY (Categoria_id)
    REFERENCES Categoria (id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table AgendaMLG.Comentario
-- -----------------------------------------------------
CREATE TABLE Comentario (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
  comentario VARCHAR(1000) NOT NULL,
  Evento_id INT NOT NULL,
  Usuario_id INT NOT NULL,
  valoracion DECIMAL(1,1) NOT NULL,
  PRIMARY KEY (id, Evento_id, Usuario_id),
  CONSTRAINT fk_Comentario_Evento1
    FOREIGN KEY (Evento_id)
    REFERENCES Evento (id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Comentario_Usuario1
    FOREIGN KEY (Usuario_id)
    REFERENCES Usuario (id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table AgendaMLG.Calendario
-- -----------------------------------------------------
CREATE TABLE Calendario (
  Evento_id INT NOT NULL,
  Usuario_id INT NOT NULL,
  PRIMARY KEY (Evento_id, Usuario_id),
  CONSTRAINT fk_Evento_has_Usuario_Evento1
    FOREIGN KEY (Evento_id)
    REFERENCES Evento (id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Evento_has_Usuario_Usuario1
    FOREIGN KEY (Usuario_id)
    REFERENCES Usuario (id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

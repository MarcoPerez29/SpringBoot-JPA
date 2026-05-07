package com.ebac.practica63.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDireccion;
    private String estado;
    private String calle;
    private int numero;
    @OneToOne
    @JsonManagedReference // Parte no administrada de la relación
    private Usuario usuario;
}

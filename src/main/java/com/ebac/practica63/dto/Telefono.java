package com.ebac.practica63.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Telefono {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTelefono;
    private String tipoTelefono;
    private int lada;
    private String numero;
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    @JsonBackReference // Parte no administrada de la relación
    private Usuario usuario;
}

package com.example.practica63.service;

import com.example.practica63.dto.Direccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DireccionService {

    @Autowired
    DireccionRepository direccionRepository;

    public Direccion crearDireccion(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    public Optional<Direccion> obtenerDireccionPorId(Long idDireccion) {
        return direccionRepository.findById(idDireccion);
    }

    public List<Direccion> obtenerDirecciones() {
        return direccionRepository.findAll();
    }

    public void actualizarDireccion(Direccion direccion) {
        direccionRepository.save(direccion);
    }

    public void eliminarDireccion(Long id) {
        direccionRepository.deleteById(id);
    }
}

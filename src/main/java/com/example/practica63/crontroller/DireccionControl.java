package com.example.practica63.crontroller;

import com.example.practica63.dto.Direccion;
import com.example.practica63.service.DireccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class DireccionControl {

    @Autowired
    DireccionService direccionService;

    @GetMapping("/direcciones")
    public List<Direccion> obtenerDirecciones() {
        return direccionService.obtenerDirecciones();
    }

    @GetMapping("/direcciones/{id}")
    public ResponseEntity<Direccion> obtenerDireccionPorId(@PathVariable Long id) {
        System.out.println("Id recibido: " + id);
        Optional<Direccion> direccionOptional = direccionService.obtenerDireccionPorId(id);

        return direccionOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/direcciones")
    public ResponseEntity<Direccion> crearDireccion(@RequestBody Direccion direccion) throws URISyntaxException {
        direccionService.crearDireccion(direccion);

        return ResponseEntity.created(new URI("http://local/host/direcciones")).build();
    }

    @PutMapping("/direcciones/{id}")
    public ResponseEntity<Direccion> actualizarDireccion(@PathVariable Long id, @RequestBody Direccion direccionActualizada) {
        Optional<Direccion> direccionOptional = direccionService.obtenerDireccionPorId(id);

        if (direccionOptional.isPresent()) {
            direccionActualizada.setIdDireccion(direccionOptional.get().getIdDireccion());
            direccionService.actualizarDireccion(direccionActualizada);

            return ResponseEntity.ok(direccionActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/direcciones/{id}")
    public ResponseEntity<Void> eliminarDireccion(@PathVariable Long id) {
        direccionService.eliminarDireccion(id);

        return ResponseEntity.noContent().build();
    }
}

package com.ebac.practica63.crontroller;

import com.ebac.practica63.dto.Direccion;
import com.ebac.practica63.service.DireccionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class DireccionControl {

    @Autowired
    DireccionService direccionService;

    @GetMapping("/direcciones")
    public ResponseWrapper<List<Direccion>> obtenerDirecciones() {
        log.info("Obteniendo direcciones");
        List<Direccion> direccionList = direccionService.obtenerDirecciones();
        ResponseEntity<List<Direccion>> responseEntity = ResponseEntity.ok(direccionList);
        return new ResponseWrapper<>(true, "Listado de direcciones", responseEntity);
    }

    @GetMapping("/direcciones/{id}")
    public ResponseWrapper<Direccion> obtenerDireccionPorId(@PathVariable Long id) {
        Optional<Direccion> direccionOptional = direccionService.obtenerDireccionPorId(id);

        log.info("Obteniendo direccion por id...");
        ResponseEntity<Direccion> responseEntity =
                direccionOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        if (direccionOptional.isPresent()) {
            log.info("Se ha obtenido la direccion con id {}", id);
            return new ResponseWrapper<>(true, "Informacion de la direccion " + id, responseEntity);
        } else {
            log.info("Id {} no existente", id);
            return new ResponseWrapper<>(false, "El id: " + id + ", no existe", responseEntity);
        }
    }

    @PostMapping("/direcciones")
    public ResponseWrapper<Direccion> crearDireccion(@RequestBody Direccion direccion) throws URISyntaxException {
        direccionService.crearDireccion(direccion);
        ResponseEntity<Direccion> responseEntity = ResponseEntity.created(new URI("http://local/host/direcciones")).build();
        log.info("Creando direccion...");
        log.info("Ha ocurrido un error al intentar crear la direccion");
        return new ResponseWrapper<>(true, "Direccion creada exitosamente", responseEntity);
    }

    @PutMapping("/direcciones/{id}")
    public ResponseWrapper<Direccion> actualizarDireccion(@PathVariable Long id, @RequestBody Direccion direccionActualizada) {
        Optional<Direccion> direccionOptional = direccionService.obtenerDireccionPorId(id);
        log.info("Actualizando direccion con id {}...", id);

        if (direccionOptional.isPresent()) {
            direccionActualizada.setIdDireccion(direccionOptional.get().getIdDireccion());
            direccionService.actualizarDireccion(direccionActualizada);

            ResponseEntity<Direccion> responseEntity = ResponseEntity.ok(direccionActualizada);
            log.info("La direccion con id {} se ha actualizado correctamente", id);
            return new ResponseWrapper<>(true, "Direccion actualizada exitosamente", responseEntity);
        } else {
            ResponseEntity<Direccion> responseEntity = ResponseEntity.notFound().build();
            log.info("La direccion con id {} no existente", id);
            return new ResponseWrapper<>(false, "La direccion indicada no existe", responseEntity);
        }
    }

    @DeleteMapping("/direcciones/{id}")
    public ResponseWrapper<Void> eliminarDireccion(@PathVariable Long id) {
        direccionService.eliminarDireccion(id);

        ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
        log.info("Se ha eliminado la direccion con id {}", id);
        return new ResponseWrapper<>(true, "Direccion eliminada correctamente", responseEntity);
    }
}

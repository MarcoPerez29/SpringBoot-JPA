package com.ebac.practica63.crontroller;

import com.ebac.practica63.dto.Telefono;
import com.ebac.practica63.dto.Usuario;
import com.ebac.practica63.service.TelefonoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class TelefonoControl {

    @Autowired
    TelefonoService telefonoService;

    @GetMapping("/telefonos")
    public ResponseWrapper<List<Telefono>> obtenerTelefonos() {
        // Devuelve un objeto Telefono que se convertirá automáticamente en JSON/XML
        log.info("Obteniendo telefonos");
        List<Telefono> telefonoList = telefonoService.obtenerTelefonos();
        ResponseEntity<List<Telefono>> responseEntity = ResponseEntity.ok(telefonoList);
        return new ResponseWrapper<>(true, "Listado de telefonos", responseEntity);
    }

    @GetMapping("/telefonos/{id}")
    public ResponseWrapper<Telefono> obtenerTelefonoPorId(@PathVariable Long id) {
        // Lógiva para obtener el telefono por su ID
        // Devuelve n objeto Telefono que se convertirá automáticamente en JSON/XML
        Optional<Telefono> telefonoOptional = telefonoService.obtenerTelefonoPorId(id);
        log.info("Obteniendo telefono por id...");
        ResponseEntity<Telefono> responseEntity =
                telefonoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        if (telefonoOptional.isPresent()){
            log.info("Se ha obtenido el telefono con id {}", id);
            return new ResponseWrapper<>(true, "Informacion del telefono " + id, responseEntity);
        } else {
            log.info("Id {} no existente", id);
            return new ResponseWrapper<>(false, "El id: " + id + ", no existe", responseEntity);
        }

    }

    @PostMapping("/telefonos")
    public ResponseWrapper<Telefono> crearTelefono(@RequestBody Telefono telefono) {
        // Lógica para crear un nuevo telefono
        // Retorna ResponseEntity con el onbjeto User en el cuerpo y un código de estado 201 (CREATED) en la respuesta
        log.info("Creando telefono...");
        try {
            Telefono telefonoCreado = telefonoService.crearTelefono(telefono);
            ResponseEntity<Telefono> responseEntity = ResponseEntity.created(new URI("http://localhost/telefonos")).body(telefonoCreado);
            log.info("El telefono se ha creado exitosamente");
            return new ResponseWrapper<>(true, "Telefono creado exitosamente", responseEntity);
        } catch (Exception e) {
            ResponseEntity<Telefono> responseEntity = ResponseEntity.badRequest().build();
            log.info("Ha ocurrido un error al intentar crear el telefono");
            return new ResponseWrapper<>(false, e.getMessage(), responseEntity);
        }
    }

    @PutMapping("/telefonos/{id}")
    public ResponseWrapper<Telefono> actualizarTelefono(@PathVariable Long id, @RequestBody Telefono telefonoActualizado) {
        // Lógica para actualizar el telefono con el ID proporcionado
        // Retorno ResponseEntity con el objeto User actualizado en el cuerpo y un código de estado 200 (OK) en la respuesta

        Optional<Telefono> telefonoOptional = telefonoService.obtenerTelefonoPorId(id);
        log.info("Actualizando telefono con id {}...", id);


        ResponseEntity<Telefono> responseEntity =
                telefonoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        if (telefonoOptional.isPresent()) {
            Usuario usuario = new Usuario();
            int idUsuario = telefonoOptional.get().getUsuario().getIdUsuario();
            String nombre = telefonoOptional.get().getUsuario().getNombre();
            int edad = telefonoOptional.get().getUsuario().getEdad();

            usuario.setEdad(edad);
            usuario.setNombre(nombre);
            usuario.setIdUsuario(idUsuario);

            telefonoActualizado.setIdTelefono(telefonoOptional.get().getIdTelefono());
            telefonoActualizado.setUsuario(usuario);
            telefonoService.actualizarTelefono(telefonoActualizado);
            log.info("El telefono con id {} se ha actualizado correctamente", id);
            return new ResponseWrapper<>(true, "Telefono actualizado correctamente", responseEntity);
        } else {
            log.info("El telefono con id {} no existente", id);
            return new ResponseWrapper<>(false, "El telefono indicado no existe", responseEntity);
        }
    }

    @DeleteMapping("/telefonos/{id}")
    public ResponseWrapper<Void> eliminarTelefono(@PathVariable Long id) {
        // Lógica para eliminar el telefono con el ID proporcionado
        // Retorna ResponseEntity con un código de estado 204 (NO_CONTENT) en la respuesta
        telefonoService.eliminarTelefono(id);

        ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
        log.info("Se ha eliminado el telefono con id {}", id);
        return new ResponseWrapper<>(true, "Telefono eliminado correctamente", responseEntity);

    }
}

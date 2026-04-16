package com.example.practica63.crontroller;

import com.example.practica63.dto.Direccion;
import com.example.practica63.service.DireccionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DireccionControlTest {

    @Mock
    DireccionService direccionService;

    @InjectMocks
    DireccionControl direccionControl;

    @Test
    void obtenerDirecciones() {
        int direcciones = 5;
        List<Direccion> direccionesListExpected = crearDirecciones(direcciones);

        // Configuramos el comportamiento del mock
        when(direccionService.obtenerDirecciones()).thenReturn(direccionesListExpected);

        // Ejecutamos el metodo del controlador
        List<Direccion> direccionesListActual = direccionControl.obtenerDirecciones();

        // Validamos el resultado
        assertEquals(direcciones, direccionesListActual.size());
        assertEquals(direccionesListExpected, direccionesListActual);
    }

    @Test
    void obtenerDireccionesCuandoNoExisten() {
        // Configuramos el comportamiento del mock
        when(direccionService.obtenerDirecciones()).thenReturn(List.of());

        // Ejecutamos el metodo del controlador
        List<Direccion> direccionesListActual = direccionControl.obtenerDirecciones();

        // Validamos el resultado
        assertTrue(direccionesListActual.isEmpty());
        verify(direccionService, times(1)).obtenerDirecciones();
    }

    @Test
    void obtenerDireccionPorId() {
        long idDireccion = 1;
        Optional<Direccion> direccionExpected = Optional.of(crearDirecciones(1).get(0));

        // Configuramos el comportamiento del mock
        when(direccionService.obtenerDireccionPorId(idDireccion)).thenReturn(direccionExpected);

        // Ejecutamos el metodo del controlador
        ResponseEntity<Direccion> direccionResponseEntity = direccionControl.obtenerDireccionPorId(idDireccion);
        Direccion direccionActual = direccionResponseEntity.getBody();

        // Validamos el resultado
        assertEquals(200, direccionResponseEntity.getStatusCode().value());
        assertNotNull(direccionActual);
        assertEquals("Calle1", direccionActual.getCalle());
    }

    @Test
    void obtenerDireccionPorIdCuandoNoExiste() {
        long idDireccion = 1;

        // Configuramos el comportamiento del mock
        when(direccionService.obtenerDireccionPorId(idDireccion)).thenReturn(Optional.empty());

        // Ejecutamos el metodo del controlador
        ResponseEntity<Direccion> direccionResponseEntity = direccionControl.obtenerDireccionPorId(idDireccion);
        Direccion direccionActual = direccionResponseEntity.getBody();

        // Validamos el resultado
        assertEquals(404, direccionResponseEntity.getStatusCode().value());
        assertTrue(Objects.isNull(direccionActual));
    }

    @Test
    void crearDireccion() throws URISyntaxException {
        Direccion direccionExpected = crearDirecciones(1).get(0);

        // Configuramos el comportamiento del mock
        when(direccionService.crearDireccion(direccionExpected)).thenReturn(direccionExpected);

        // Ejecutamos el metodo del controlador
        ResponseEntity<Direccion> direccionResponseEntity = direccionControl.crearDireccion(direccionExpected);
        Direccion direccionActual = direccionResponseEntity.getBody();

        // Validamos el resultado
        assertEquals(201, direccionResponseEntity.getStatusCode().value());
        assertTrue(Objects.isNull(direccionActual));
    }

    @Test
    void actualizarDireccion() {
        int idDireccion = 5;
        String estadoActualizado = "Puebla";
        String calleActualizada = "Morelos";
        int numeroActualizado = 12;

        Direccion direccionAntigua = new Direccion();
        direccionAntigua.setIdDireccion(idDireccion);
        direccionAntigua.setEstado("Veracruz");
        direccionAntigua.setCalle("Porfirio");
        direccionAntigua.setNumero(8);

        Direccion direccionActualizada = new Direccion();
        direccionActualizada.setEstado(estadoActualizado);
        direccionActualizada.setCalle(calleActualizada);
        direccionActualizada.setNumero(numeroActualizado);

        // Configuramos el comportamiento del mock
        when(direccionService.obtenerDireccionPorId((long) idDireccion)).thenReturn(Optional.of(direccionAntigua));
        doNothing().when(direccionService).actualizarDireccion(direccionActualizada);

        // Ejecutamos el metodo del controlador
        ResponseEntity<Direccion> direccionResponseEntity = direccionControl.actualizarDireccion((long) idDireccion, direccionActualizada);
        Direccion direccionActual = direccionResponseEntity.getBody();

        // Validamos el resultado
        assertEquals(200, direccionResponseEntity.getStatusCode().value());
        assertNotNull(direccionActual);
        assertEquals(idDireccion, direccionActual.getIdDireccion());
        assertEquals(estadoActualizado, direccionActual.getEstado());
        assertEquals(calleActualizada, direccionActual.getCalle());
        assertEquals(numeroActualizado, direccionActual.getNumero());
    }

    @Test
    void actualizarDireccionCuandoLaDireccionNoExiste() {
        long idDireccion = 5;
        String estadoActualizado = "Puebla";
        String calleActualizada = "Morelos";
        int numeroActualizado = 12;

        Direccion direccionActualizada = new Direccion();
        direccionActualizada.setEstado(estadoActualizado);
        direccionActualizada.setCalle(calleActualizada);
        direccionActualizada.setNumero(numeroActualizado);

        // Configuramos el comportamiento del mock
        when(direccionService.obtenerDireccionPorId(idDireccion)).thenReturn(Optional.empty());

        // Ejecutamos el metodo del controlador
        ResponseEntity<Direccion> direccionResponseEntity = direccionControl.actualizarDireccion(idDireccion, direccionActualizada);
        Direccion direccionActual = direccionResponseEntity.getBody();

        // Validamos el resultado
        assertEquals(404, direccionResponseEntity.getStatusCode().value());
        assertNull(direccionActual);
        verify(direccionService, never()).actualizarDireccion(direccionActualizada);
    }

    @Test
    void eliminarDireccion() {
        long idDireccion = 1;

        // Configuramos el comportamiento del mock
        doNothing().when(direccionService).eliminarDireccion(idDireccion);

        // Ejecutamos el metodo del controlador
        ResponseEntity<Void> responseEntity = direccionControl.eliminarDireccion(idDireccion);

        // Validamos el resultado
        assertEquals(204, responseEntity.getStatusCode().value());
        verify(direccionService, atLeastOnce()).eliminarDireccion(idDireccion);
    }

    private List<Direccion> crearDirecciones(int elementos) {
        return IntStream.range(1, elementos+1)
                .mapToObj(i -> {
                    Direccion direccion = new Direccion();
                    direccion.setIdDireccion(i);
                    direccion.setEstado("Estado" + i);
                    direccion.setCalle("Calle" + i);
                    direccion.setNumero(i);
                    return direccion;
                }).collect(Collectors.toList());
    }
}
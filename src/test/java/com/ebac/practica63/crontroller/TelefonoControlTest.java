package com.ebac.practica63.crontroller;

import com.ebac.practica63.dto.Telefono;
import com.ebac.practica63.dto.Usuario;
import com.ebac.practica63.service.TelefonoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelefonoControlTest {

    @Mock
    TelefonoService telefonoService;

    @InjectMocks
    TelefonoControl telefonoControl;

    @Test
    void obtenerTelefonos() {
        int telefonos = 5;
        List<Telefono> telefonosListExpected = crearTelefonos(telefonos);

        // Configuramos el comportamiento del mock
        when(telefonoService.obtenerTelefonos()).thenReturn(telefonosListExpected);

        // Ejecutamos el metodo del controlador
        ResponseWrapper<List<Telefono>> telefonosWrapperActual = telefonoControl.obtenerTelefonos();
        List<Telefono> telefonosListActual = telefonosWrapperActual.getResponseEntity().getBody();
        boolean telefonosIsSuccesActual = telefonosWrapperActual.isSuccess();
        String telefonosMessageActual = telefonosWrapperActual.getMessage();

        // Validamos el resultado
        assertEquals(telefonos, telefonosListActual.size());
        assertEquals(telefonosListExpected, telefonosListActual);
        assertTrue(telefonosIsSuccesActual);
        assertEquals("Listado de telefonos", telefonosMessageActual);
    }

    @Test
    void obtenerTelefonosCuandoNoExisten() {
        // Configuramos el comportamiento del mock
        when(telefonoService.obtenerTelefonos()).thenReturn(List.of());

        // Ejecutamos el metodo del controlador
        ResponseWrapper<List<Telefono>> telefonosWrapperActual = telefonoControl.obtenerTelefonos();
        List<Telefono> usuarioListActual = (List<Telefono>) telefonosWrapperActual.getResponseEntity().getBody();

        //Validamos el resultado
        assertTrue(usuarioListActual.isEmpty());
        verify(telefonoService, times(1)).obtenerTelefonos();
    }

    @Test
    void obtenerTelefonoPorId() {
        long idTelefono = 1;
        Optional<Telefono> telefonoExpected = Optional.of(crearTelefonos(1).get(0));

        // Configuramos el comportamiento del mock
        when(telefonoService.obtenerTelefonoPorId(idTelefono)).thenReturn(telefonoExpected);

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Telefono> telefonoWrapperActual = telefonoControl.obtenerTelefonoPorId(idTelefono);
        ResponseEntity<Telefono> telefonoResponseEntity = telefonoWrapperActual.getResponseEntity();
        Telefono telefonoActual = telefonoWrapperActual.getResponseEntity().getBody();
        boolean telefonosIsSuccesActual = telefonoWrapperActual.isSuccess();
        String telefonosMessageActual = telefonoWrapperActual.getMessage();

        // Validamos el resultado
        assertEquals(200, telefonoResponseEntity.getStatusCode().value());
        assertNotNull(telefonoActual);
        assertEquals("TipoTelefono1", telefonoActual.getTipoTelefono());
        assertTrue(telefonosIsSuccesActual);
        assertEquals("Informacion del telefono 1", telefonosMessageActual);
    }

    @Test
    void obtenerUsuarioPorIdCuandoNoExiste() {
        long idTelefono = 1;

        // Configuramos el comportamiento del mock
        when(telefonoService.obtenerTelefonoPorId(idTelefono)).thenReturn(Optional.empty());

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Telefono> telefonoWrapperActual = telefonoControl.obtenerTelefonoPorId(idTelefono);
        ResponseEntity<Telefono> telefonoResponseEntity = telefonoWrapperActual.getResponseEntity();
        Telefono telefonoActual = telefonoWrapperActual.getResponseEntity().getBody();
        boolean telefonosIsSuccesActual = telefonoWrapperActual.isSuccess();
        String telefonosMessageActual = telefonoWrapperActual.getMessage();

        // Validar el resultado
        assertEquals(404, telefonoResponseEntity.getStatusCode().value());
        assertTrue(Objects.isNull(telefonoActual));
        assertFalse(telefonosIsSuccesActual);
        assertEquals("El id: 1, no existe", telefonosMessageActual);
    }

    @Test
    void crearTelefono() throws Exception {
        Telefono telefonoExpected = crearTelefonos(1).get(0);

        // Configuramos el comportamiento del mock
        when(telefonoService.crearTelefono(telefonoExpected)).thenReturn(telefonoExpected);

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Telefono> telefonoWrapperActual = telefonoControl.crearTelefono(telefonoExpected);
        ResponseEntity<Telefono> telefonoResponseEntity = telefonoWrapperActual.getResponseEntity();
        Telefono telefonoActual = telefonoWrapperActual.getResponseEntity().getBody();
        boolean telefonosIsSuccesActual = telefonoWrapperActual.isSuccess();
        String telefonosMessageActual = telefonoWrapperActual.getMessage();

        // Validamos el resultado
        assertEquals(201, telefonoResponseEntity.getStatusCode().value());
        assertEquals(telefonoActual.getTipoTelefono(), "TipoTelefono1");
        assertEquals(telefonoActual.getLada(), 1);
        assertEquals(telefonoActual.getNumero(), "0000000001");
        assertEquals(telefonoActual.getUsuario(), crearUsuarios(1));
        assertTrue(telefonosIsSuccesActual);
        assertEquals("Telefono creado exitosamente", telefonosMessageActual);
    }

    @Test
    void actualizarTelefono() {
        int idTelefono = 5;
        String tipoTelefonoActualizado = "casa";
        int ladaActualizada = 999;
        String numeroActualizado = "1234567890";

        Telefono telefonoAntiguo = new Telefono();
        telefonoAntiguo.setIdTelefono(idTelefono);
        telefonoAntiguo.setTipoTelefono("celular");
        telefonoAntiguo.setLada(234);
        telefonoAntiguo.setNumero("0987654321");
        telefonoAntiguo.setUsuario(crearUsuarios(1));

        Telefono telefonoActualizado = new Telefono();
        telefonoActualizado.setTipoTelefono(tipoTelefonoActualizado);
        telefonoActualizado.setLada(ladaActualizada);
        telefonoActualizado.setNumero(numeroActualizado);

        // Configuramos el comportamiento del mock
        when(telefonoService.obtenerTelefonoPorId((long) idTelefono)).thenReturn(Optional.of(telefonoAntiguo));
        doNothing().when(telefonoService).actualizarTelefono(telefonoActualizado);

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Telefono> telefonoWrapperActual = telefonoControl.actualizarTelefono((long) idTelefono, telefonoActualizado);
        ResponseEntity<Telefono> telefonoResponseEntity = telefonoWrapperActual.getResponseEntity();
        Telefono telefonoActual = telefonoWrapperActual.getResponseEntity().getBody();
        boolean telefonosIsSuccesActual = telefonoWrapperActual.isSuccess();
        String telefonosMessageActual = telefonoWrapperActual.getMessage();

        // Validamos el resultado
        assertEquals(200, telefonoResponseEntity.getStatusCode().value());
        assertNotNull(telefonoActual);
        assertEquals(idTelefono, telefonoActual.getIdTelefono());
        assertEquals(tipoTelefonoActualizado, telefonoActual.getTipoTelefono());
        assertEquals(ladaActualizada, telefonoActual.getLada());
        assertEquals(numeroActualizado, telefonoActual.getNumero());
        assertTrue(telefonosIsSuccesActual);
        assertEquals("Telefono actualizado correctamente", telefonosMessageActual);
    }

    @Test
    void actualizarTelefonoCuandoElTelefonoNoExiste() {
        int idTelefono = 5;
        String tipoTelefonoActualizado = "casa";
        int ladaActualizada = 999;
        String numeroActualizado = "1234567890";

        Telefono telefonoActualizado = new Telefono();
        telefonoActualizado.setTipoTelefono(tipoTelefonoActualizado);
        telefonoActualizado.setLada(ladaActualizada);
        telefonoActualizado.setNumero(numeroActualizado);

        // Configuramos el comportamiento del mock
        when(telefonoService.obtenerTelefonoPorId((long) idTelefono)).thenReturn(Optional.empty());

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Telefono> telefonoWrapperActual = telefonoControl.actualizarTelefono((long) idTelefono, telefonoActualizado);
        ResponseEntity<Telefono> telefonoResponseEntity = telefonoWrapperActual.getResponseEntity();
        Telefono telefonoActual = telefonoWrapperActual.getResponseEntity().getBody();
        boolean telefonosIsSuccesActual = telefonoWrapperActual.isSuccess();
        String telefonosMessageActual = telefonoWrapperActual.getMessage();

        // Validamos el resultado
        assertEquals(404, telefonoResponseEntity.getStatusCode().value());
        assertNull(telefonoActual);
        verify(telefonoService, never()).actualizarTelefono(telefonoActualizado);
        assertFalse(telefonosIsSuccesActual);
        assertEquals("El telefono indicado no existe", telefonosMessageActual);
    }

    @Test
    void eliminarTelefono() {
        long idTelefono = 1;

        // Configuramos el comportamiento del mock
        doNothing().when(telefonoService).eliminarTelefono(idTelefono);

        //Ejecutamos el metodo del controlador
        ResponseWrapper<Void> telefonoWrapperActual = telefonoControl.eliminarTelefono(idTelefono);
        ResponseEntity<Void> telefonoResponseEntity = telefonoWrapperActual.getResponseEntity();
        boolean telefonosIsSuccesActual = telefonoWrapperActual.isSuccess();
        String telefonosMessageActual = telefonoWrapperActual.getMessage();

        // Validamos el resultado
        assertEquals(204, telefonoResponseEntity.getStatusCode().value());
        assertTrue(telefonosIsSuccesActual);
        assertEquals("Telefono eliminado correctamente", telefonosMessageActual);
        verify(telefonoService, atLeastOnce()).eliminarTelefono(idTelefono);
    }

    @Test
    void crearTelefonoCuandoSeaMenorA15() throws Exception {
        Telefono telefono = new Telefono();
        telefono.setIdTelefono(1);
        telefono.setLada(1);
        telefono.setTipoTelefono("TipoTelefono");
        telefono.setUsuario(crearUsuarios(1));
        telefono.setNumero("123");

        // Configuramos el comportamiento del mock
        doThrow(Exception.class).when(telefonoService).crearTelefono(telefono);

        //Ejecutamos el metodo del controlador
        ResponseWrapper<Telefono> telefonoWrapperActual = telefonoControl.crearTelefono(telefono);
        ResponseEntity<Telefono> telefonoResponseEntity = telefonoWrapperActual.getResponseEntity();
        Telefono telefonoActual = telefonoWrapperActual.getResponseEntity().getBody();
        boolean telefonosIsSuccesActual = telefonoWrapperActual.isSuccess();
        String telefonosMessageActual = telefonoWrapperActual.getMessage();

        assertEquals(400, telefonoResponseEntity.getStatusCode().value());
        assertNull(telefonoActual);
        assertFalse(telefonosIsSuccesActual);
        assertNull(telefonosMessageActual);
    }

    private List<Telefono> crearTelefonos(int elementos) {
        return IntStream.range(1, elementos+1)
                .mapToObj(i -> {
                    Telefono telefono = new Telefono();
                    telefono.setIdTelefono(i);
                    telefono.setTipoTelefono("TipoTelefono" + i);
                    telefono.setNumero("000000000" + i);
                    telefono.setLada( i);
                    telefono.setUsuario(crearUsuarios(i));
                    return telefono;
                }).collect(Collectors.toList());
    }

    private Usuario crearUsuarios(int numero) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(numero);
        usuario.setNombre("Nombre" + numero);
        usuario.setEdad(15 + numero);
        return usuario;
    }
}
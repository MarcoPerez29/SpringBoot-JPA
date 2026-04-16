package com.example.practica63.service;

import com.example.practica63.dto.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(Usuario usuario) throws Exception {
        if (usuario.getEdad() >= 18) {
            return usuarioRepository.save(usuario);
        }
        throw new Exception("No se permite usuarios menores de 18 años");

    }

    public Optional<Usuario> obtenerUsuarioPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void  eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}

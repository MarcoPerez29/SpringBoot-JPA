package com.ebac.practica63.repository;

import com.ebac.practica63.dto.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}

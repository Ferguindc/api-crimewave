package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario createUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        usuario.setNombre(usuarioDetails.getNombre());

        // Solo actualizar email si es diferente y no existe
        if (!usuario.getEmail().equals(usuarioDetails.getEmail())) {
            if (usuarioRepository.existsByEmail(usuarioDetails.getEmail())) {
                throw new RuntimeException("El email ya está registrado");
            }
            usuario.setEmail(usuarioDetails.getEmail());
        }

        if (usuarioDetails.getPasswordHash() != null && !usuarioDetails.getPasswordHash().isEmpty()) {
            usuario.setPasswordHash(usuarioDetails.getPasswordHash());
        }
        usuario.setRol(usuarioDetails.getRol());

        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuarioRepository.delete(usuario);
    }
}


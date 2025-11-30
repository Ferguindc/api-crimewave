package com.example.demo.service;

import com.example.demo.model.Categoria;
import com.example.demo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> getCategoriaById(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria createCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria updateCategoria(Long id, Categoria categoriaDetails) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));

        categoria.setNombre(categoriaDetails.getNombre());

        return categoriaRepository.save(categoria);
    }

    public void deleteCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
        categoriaRepository.delete(categoria);
    }
}


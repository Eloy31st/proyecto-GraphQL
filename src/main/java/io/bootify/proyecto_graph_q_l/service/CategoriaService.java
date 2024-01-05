package io.bootify.proyecto_graph_q_l.service;

import io.bootify.proyecto_graph_q_l.domain.Categoria;
import io.bootify.proyecto_graph_q_l.domain.Libro;
import io.bootify.proyecto_graph_q_l.model.CategoriaDTO;
import io.bootify.proyecto_graph_q_l.repos.CategoriaRepository;
import io.bootify.proyecto_graph_q_l.repos.LibroRepository;
import io.bootify.proyecto_graph_q_l.util.NotFoundException;
import io.bootify.proyecto_graph_q_l.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final LibroRepository libroRepository;

    public CategoriaService(final CategoriaRepository categoriaRepository,
            final LibroRepository libroRepository) {
        this.categoriaRepository = categoriaRepository;
        this.libroRepository = libroRepository;
    }

    public List<CategoriaDTO> findAll() {
        final List<Categoria> categorias = categoriaRepository.findAll(Sort.by("id"));
        return categorias.stream()
                .map(categoria -> mapToDTO(categoria, new CategoriaDTO()))
                .toList();
    }

    public CategoriaDTO get(final Long id) {
        return categoriaRepository.findById(id)
                .map(categoria -> mapToDTO(categoria, new CategoriaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CategoriaDTO categoriaDTO) {
        final Categoria categoria = new Categoria();
        mapToEntity(categoriaDTO, categoria);
        return categoriaRepository.save(categoria).getId();
    }

    public void update(final Long id, final CategoriaDTO categoriaDTO) {
        final Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoriaDTO, categoria);
        categoriaRepository.save(categoria);
    }

    public void delete(final Long id) {
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO mapToDTO(final Categoria categoria, final CategoriaDTO categoriaDTO) {
        categoriaDTO.setId(categoria.getId());
        categoriaDTO.setNombre(categoria.getNombre());
        return categoriaDTO;
    }

    private Categoria mapToEntity(final CategoriaDTO categoriaDTO, final Categoria categoria) {
        categoria.setNombre(categoriaDTO.getNombre());
        return categoria;
    }

    public String getReferencedWarning(final Long id) {
        final Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Libro categoriaLibro = libroRepository.findFirstByCategoria(categoria);
        if (categoriaLibro != null) {
            return WebUtils.getMessage("categoria.libro.categoria.referenced", categoriaLibro.getId());
        }
        return null;
    }

}

package io.bootify.proyecto_graph_q_l.service;

import io.bootify.proyecto_graph_q_l.domain.Autor;
import io.bootify.proyecto_graph_q_l.domain.Libro;
import io.bootify.proyecto_graph_q_l.model.AutorDTO;
import io.bootify.proyecto_graph_q_l.repos.AutorRepository;
import io.bootify.proyecto_graph_q_l.repos.LibroRepository;
import io.bootify.proyecto_graph_q_l.util.NotFoundException;
import io.bootify.proyecto_graph_q_l.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AutorService {

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;

    public AutorService(final AutorRepository autorRepository,
            final LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public List<AutorDTO> findAll() {
        final List<Autor> autors = autorRepository.findAll(Sort.by("id"));
        return autors.stream()
                .map(autor -> mapToDTO(autor, new AutorDTO()))
                .toList();
    }

    public AutorDTO get(final Long id) {
        return autorRepository.findById(id)
                .map(autor -> mapToDTO(autor, new AutorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AutorDTO autorDTO) {
        final Autor autor = new Autor();
        mapToEntity(autorDTO, autor);
        return autorRepository.save(autor).getId();
    }

    public void update(final Long id, final AutorDTO autorDTO) {
        final Autor autor = autorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(autorDTO, autor);
        autorRepository.save(autor);
    }

    public void delete(final Long id) {
        autorRepository.deleteById(id);
    }

    private AutorDTO mapToDTO(final Autor autor, final AutorDTO autorDTO) {
        autorDTO.setId(autor.getId());
        autorDTO.setNombre(autor.getNombre());
        return autorDTO;
    }

    private Autor mapToEntity(final AutorDTO autorDTO, final Autor autor) {
        autor.setNombre(autorDTO.getNombre());
        return autor;
    }

    public String getReferencedWarning(final Long id) {
        final Autor autor = autorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Libro autorLibro = libroRepository.findFirstByAutor(autor);
        if (autorLibro != null) {
            return WebUtils.getMessage("autor.libro.autor.referenced", autorLibro.getId());
        }
        return null;
    }

}

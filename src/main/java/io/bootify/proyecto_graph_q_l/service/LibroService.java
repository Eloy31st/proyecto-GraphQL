package io.bootify.proyecto_graph_q_l.service;

import io.bootify.proyecto_graph_q_l.domain.Autor;
import io.bootify.proyecto_graph_q_l.domain.Categoria;
import io.bootify.proyecto_graph_q_l.domain.Libro;
import io.bootify.proyecto_graph_q_l.domain.Reserva;
import io.bootify.proyecto_graph_q_l.model.LibroDTO;
import io.bootify.proyecto_graph_q_l.repos.AutorRepository;
import io.bootify.proyecto_graph_q_l.repos.CategoriaRepository;
import io.bootify.proyecto_graph_q_l.repos.LibroRepository;
import io.bootify.proyecto_graph_q_l.repos.ReservaRepository;
import io.bootify.proyecto_graph_q_l.util.NotFoundException;
import io.bootify.proyecto_graph_q_l.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final ReservaRepository reservaRepository;

    public LibroService(final LibroRepository libroRepository,
            final AutorRepository autorRepository, final CategoriaRepository categoriaRepository,
            final ReservaRepository reservaRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<LibroDTO> findAll() {
        final List<Libro> libroes = libroRepository.findAll(Sort.by("id"));
        return libroes.stream()
                .map(libro -> mapToDTO(libro, new LibroDTO()))
                .toList();
    }

    public LibroDTO get(final Long id) {
        return libroRepository.findById(id)
                .map(libro -> mapToDTO(libro, new LibroDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final LibroDTO libroDTO) {
        final Libro libro = new Libro();
        mapToEntity(libroDTO, libro);
        return libroRepository.save(libro).getId();
    }

    public void update(final Long id, final LibroDTO libroDTO) {
        final Libro libro = libroRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(libroDTO, libro);
        libroRepository.save(libro);
    }

    public void delete(final Long id) {
        libroRepository.deleteById(id);
    }

    private LibroDTO mapToDTO(final Libro libro, final LibroDTO libroDTO) {
        libroDTO.setId(libro.getId());
        libroDTO.setTitulo(libro.getTitulo());
        libroDTO.setAutor(libro.getAutor() == null ? null : libro.getAutor().getId());
        libroDTO.setCategoria(libro.getCategoria() == null ? null : libro.getCategoria().getId());
        return libroDTO;
    }

    private Libro mapToEntity(final LibroDTO libroDTO, final Libro libro) {
        libro.setTitulo(libroDTO.getTitulo());
        final Autor autor = libroDTO.getAutor() == null ? null : autorRepository.findById(libroDTO.getAutor())
                .orElseThrow(() -> new NotFoundException("autor not found"));
        libro.setAutor(autor);
        final Categoria categoria = libroDTO.getCategoria() == null ? null : categoriaRepository.findById(libroDTO.getCategoria())
                .orElseThrow(() -> new NotFoundException("categoria not found"));
        libro.setCategoria(categoria);
        return libro;
    }

    public String getReferencedWarning(final Long id) {
        final Libro libro = libroRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Reserva libroReserva = reservaRepository.findFirstByLibro(libro);
        if (libroReserva != null) {
            return WebUtils.getMessage("libro.reserva.libro.referenced", libroReserva.getId());
        }
        return null;
    }

}

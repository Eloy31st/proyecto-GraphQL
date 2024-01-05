package io.bootify.proyecto_graph_q_l.service;

import io.bootify.proyecto_graph_q_l.domain.Libro;
import io.bootify.proyecto_graph_q_l.domain.Reserva;
import io.bootify.proyecto_graph_q_l.model.ReservaDTO;
import io.bootify.proyecto_graph_q_l.repos.LibroRepository;
import io.bootify.proyecto_graph_q_l.repos.ReservaRepository;
import io.bootify.proyecto_graph_q_l.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final LibroRepository libroRepository;

    public ReservaService(final ReservaRepository reservaRepository,
            final LibroRepository libroRepository) {
        this.reservaRepository = reservaRepository;
        this.libroRepository = libroRepository;
    }

    public List<ReservaDTO> findAll() {
        final List<Reserva> reservas = reservaRepository.findAll(Sort.by("id"));
        return reservas.stream()
                .map(reserva -> mapToDTO(reserva, new ReservaDTO()))
                .toList();
    }

    public ReservaDTO get(final Long id) {
        return reservaRepository.findById(id)
                .map(reserva -> mapToDTO(reserva, new ReservaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReservaDTO reservaDTO) {
        final Reserva reserva = new Reserva();
        mapToEntity(reservaDTO, reserva);
        return reservaRepository.save(reserva).getId();
    }

    public void update(final Long id, final ReservaDTO reservaDTO) {
        final Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reservaDTO, reserva);
        reservaRepository.save(reserva);
    }

    public void delete(final Long id) {
        reservaRepository.deleteById(id);
    }

    private ReservaDTO mapToDTO(final Reserva reserva, final ReservaDTO reservaDTO) {
        reservaDTO.setId(reserva.getId());
        reservaDTO.setLibro(reserva.getLibro() == null ? null : reserva.getLibro().getId());
        return reservaDTO;
    }

    private Reserva mapToEntity(final ReservaDTO reservaDTO, final Reserva reserva) {
        final Libro libro = reservaDTO.getLibro() == null ? null : libroRepository.findById(reservaDTO.getLibro())
                .orElseThrow(() -> new NotFoundException("libro not found"));
        reserva.setLibro(libro);
        return reserva;
    }

}

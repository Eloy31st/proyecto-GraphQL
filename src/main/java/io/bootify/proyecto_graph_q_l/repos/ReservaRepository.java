package io.bootify.proyecto_graph_q_l.repos;

import io.bootify.proyecto_graph_q_l.domain.Libro;
import io.bootify.proyecto_graph_q_l.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Reserva findFirstByLibro(Libro libro);

}

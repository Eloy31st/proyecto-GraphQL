package io.bootify.proyecto_graph_q_l.repos;

import io.bootify.proyecto_graph_q_l.domain.Autor;
import io.bootify.proyecto_graph_q_l.domain.Categoria;
import io.bootify.proyecto_graph_q_l.domain.Libro;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LibroRepository extends JpaRepository<Libro, Long> {

    Libro findFirstByAutor(Autor autor);

    Libro findFirstByCategoria(Categoria categoria);

}

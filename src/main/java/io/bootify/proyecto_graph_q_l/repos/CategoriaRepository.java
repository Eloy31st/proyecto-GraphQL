package io.bootify.proyecto_graph_q_l.repos;

import io.bootify.proyecto_graph_q_l.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}

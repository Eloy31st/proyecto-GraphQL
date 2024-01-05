package io.bootify.proyecto_graph_q_l.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AutorDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

}

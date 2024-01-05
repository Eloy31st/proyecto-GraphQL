package io.bootify.proyecto_graph_q_l.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReservaDTO {

    private Long id;

    @NotNull
    private Long libro;

}

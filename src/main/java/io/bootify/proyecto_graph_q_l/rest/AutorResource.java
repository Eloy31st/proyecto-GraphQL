package io.bootify.proyecto_graph_q_l.rest;

import io.bootify.proyecto_graph_q_l.model.AutorDTO;
import io.bootify.proyecto_graph_q_l.service.AutorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/autors", produces = MediaType.APPLICATION_JSON_VALUE)
public class AutorResource {

    private final AutorService autorService;

    public AutorResource(final AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> getAllAutors() {
        return ResponseEntity.ok(autorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> getAutor(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(autorService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAutor(@RequestBody @Valid final AutorDTO autorDTO) {
        final Long createdId = autorService.create(autorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAutor(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AutorDTO autorDTO) {
        autorService.update(id, autorDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAutor(@PathVariable(name = "id") final Long id) {
        autorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

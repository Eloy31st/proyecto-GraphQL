package io.bootify.proyecto_graph_q_l.controller;

import io.bootify.proyecto_graph_q_l.domain.Autor;
import io.bootify.proyecto_graph_q_l.domain.Categoria;
import io.bootify.proyecto_graph_q_l.model.LibroDTO;
import io.bootify.proyecto_graph_q_l.repos.AutorRepository;
import io.bootify.proyecto_graph_q_l.repos.CategoriaRepository;
import io.bootify.proyecto_graph_q_l.service.LibroService;
import io.bootify.proyecto_graph_q_l.util.CustomCollectors;
import io.bootify.proyecto_graph_q_l.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/libros")
public class LibroController {

    private final LibroService libroService;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    public LibroController(final LibroService libroService, final AutorRepository autorRepository,
            final CategoriaRepository categoriaRepository) {
        this.libroService = libroService;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("autorValues", autorRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Autor::getId, Autor::getNombre)));
        model.addAttribute("categoriaValues", categoriaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Categoria::getId, Categoria::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("libroes", libroService.findAll());
        return "libro/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("libro") final LibroDTO libroDTO) {
        return "libro/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("libro") @Valid final LibroDTO libroDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "libro/add";
        }
        libroService.create(libroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("libro.create.success"));
        return "redirect:/libros";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("libro", libroService.get(id));
        return "libro/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("libro") @Valid final LibroDTO libroDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "libro/edit";
        }
        libroService.update(id, libroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("libro.update.success"));
        return "redirect:/libros";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = libroService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            libroService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("libro.delete.success"));
        }
        return "redirect:/libros";
    }

}

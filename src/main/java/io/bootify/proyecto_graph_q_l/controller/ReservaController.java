package io.bootify.proyecto_graph_q_l.controller;

import io.bootify.proyecto_graph_q_l.domain.Libro;
import io.bootify.proyecto_graph_q_l.model.ReservaDTO;
import io.bootify.proyecto_graph_q_l.repos.LibroRepository;
import io.bootify.proyecto_graph_q_l.service.ReservaService;
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
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final LibroRepository libroRepository;

    public ReservaController(final ReservaService reservaService,
            final LibroRepository libroRepository) {
        this.reservaService = reservaService;
        this.libroRepository = libroRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("libroValues", libroRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Libro::getId, Libro::getTitulo)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("reservas", reservaService.findAll());
        return "reserva/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("reserva") final ReservaDTO reservaDTO) {
        return "reserva/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("reserva") @Valid final ReservaDTO reservaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reserva/add";
        }
        reservaService.create(reservaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reserva.create.success"));
        return "redirect:/reservas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("reserva", reservaService.get(id));
        return "reserva/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("reserva") @Valid final ReservaDTO reservaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reserva/edit";
        }
        reservaService.update(id, reservaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reserva.update.success"));
        return "redirect:/reservas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        reservaService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("reserva.delete.success"));
        return "redirect:/reservas";
    }

}

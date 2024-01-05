package io.bootify.proyecto_graph_q_l.controller;

import io.bootify.proyecto_graph_q_l.model.AutorDTO;
import io.bootify.proyecto_graph_q_l.service.AutorService;
import io.bootify.proyecto_graph_q_l.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/autors")
public class AutorController {

    private final AutorService autorService;

    public AutorController(final AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("autors", autorService.findAll());
        return "autor/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("autor") final AutorDTO autorDTO) {
        return "autor/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("autor") @Valid final AutorDTO autorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "autor/add";
        }
        autorService.create(autorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("autor.create.success"));
        return "redirect:/autors";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("autor", autorService.get(id));
        return "autor/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("autor") @Valid final AutorDTO autorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "autor/edit";
        }
        autorService.update(id, autorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("autor.update.success"));
        return "redirect:/autors";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = autorService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            autorService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("autor.delete.success"));
        }
        return "redirect:/autors";
    }

}

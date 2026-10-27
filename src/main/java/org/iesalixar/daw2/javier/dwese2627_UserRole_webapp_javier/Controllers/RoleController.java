package org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.Controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.Daos.RoleDAO;
import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/roles")
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private MessageSource messageSource;
    /** Lista todos los roles. */
    @GetMapping
    public String listRoles(Model model, Locale locale) {
        try {
            model.addAttribute("listRoles", roleDAO.listAllRoles());
        } catch (Exception e) {
            logger.error("Error al listar los roles: {}", e.getMessage());
            model.addAttribute("errorMessage",
                    messageSource.getMessage("msg.role-controller.list.error", null, locale));
        }
        return "views/role/role-list";
    }
    /** Muestra el formulario vacío para crear un rol. */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("role", new Role());
        return "views/role/role-form";
    }
    /** Muestra el formulario con los datos del rol a editar. */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model,
                               RedirectAttributes redirectAttributes, Locale locale) {
        Role role = roleDAO.getRoleById(id);
        if (role == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.role-controller.notfound", null, locale));
            return "redirect:/roles";
        }
        model.addAttribute("role", role);
        return "views/role/role-form";
    }
    /** Vista de detalle: solo muestra id y nombre. No hay usuarios aquí. */
    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id, Model model,
                             RedirectAttributes redirectAttributes, Locale locale) {
        Role role = roleDAO.getRoleById(id);
        if (role == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.role-controller.notfound", null, locale));
            return "redirect:/roles";
        }
        model.addAttribute("role", role);
        return "views/role/role-detail";
    }
    /** Inserta un rol nuevo. */
    @PostMapping("/insert")
    public String insertRole(@Valid @ModelAttribute("role") Role role, BindingResult result,
                             RedirectAttributes redirectAttributes, Locale locale) {
        if (!result.hasFieldErrors("name") && roleDAO.existsRoleByName(role.getName())) {
            result.rejectValue("name", "msg.role.name.exists");
        }
        if (result.hasErrors()) {
            return "views/role/role-form";
// vuelve al formulario con los errores
        }
        try {
            roleDAO.insertRole(role);
        } catch (Exception e) {
            logger.error("Error al insertar el rol {}: {}", role.getName(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.role-controller.insert.error", null, locale));
        }
        return "redirect:/roles";
    }
    /** Actualiza un rol existente. */
    @PostMapping("/update")
    public String updateRole(@Valid @ModelAttribute("role") Role role, BindingResult result,
                             RedirectAttributes redirectAttributes, Locale locale) {
        if (!result.hasFieldErrors("name")
                && roleDAO.existsRoleByNameAndNotId(role.getName(), role.getId())) {
            result.rejectValue("name", "msg.role.name.exists");
        }
        if (result.hasErrors()) {
            return "views/role/role-form";
        }
        try {
            roleDAO.updateRole(role);
        } catch (Exception e) {
            logger.error("Error al actualizar el rol {}: {}", role.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.role-controller.update.error", null, locale));
        }
        return "redirect:/roles";
    }
    /** Elimina un rol. Si algún usuario lo tiene asignado, la BD lo impide y avisamos. */
    @PostMapping("/delete")
    public String deleteRole(@RequestParam("id") Long id,
                             RedirectAttributes redirectAttributes, Locale locale) {
        try {
            roleDAO.deleteRole(id);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.role-controller.delete.inUse", null, locale));
        } catch (Exception e) {
            logger.error("Error al eliminar el rol {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.role-controller.delete.error", null, locale));
        }
        return "redirect:/roles";
    }
}
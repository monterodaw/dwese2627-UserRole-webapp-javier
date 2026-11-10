package org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.daos.RoleDAO;
import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.daos.UserDAO;
import org.iesalixar.daw2.javier.dwese2627_UserRole_webapp_javier.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Locale;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    // La contraseña caduca 3 meses después del último cambio (igual que en tu CRUD anterior)
    private static final int PASSWORD_VALIDITY_MONTHS = 3;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    // hace falta para pintar los checkboxes de roles
    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listUsers(Model model, Locale locale) {
        try {
            model.addAttribute("listUsers", userDAO.listAllUsers());
        } catch (Exception e) {
            logger.error("Error al listar los usuarios: {}", e.getMessage());
            model.addAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.list.error", null, locale));
        }
        return "views/user/user-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        User user = new User();
        user.setLastPasswordChange(LocalDateTime.now().withSecond(0).withNano(0));
        model.addAttribute("user", user);
        model.addAttribute("listRoles", roleDAO.listAllRoles());
        return "views/user/user-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model,
                               RedirectAttributes redirectAttributes, Locale locale) {
        User user = userDAO.getUserById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.notfound", null, locale));
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        model.addAttribute("listRoles", roleDAO.listAllRoles());
        return "views/user/user-form";
    }

    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id, Model model,
                             RedirectAttributes redirectAttributes, Locale locale) {
        User user = userDAO.getUserById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.notfound", null, locale));
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "views/user/user-detail";
    }

    @PostMapping("/insert")
    public String insertUser(@Valid @ModelAttribute("user") User user, BindingResult result,
                             Model model, RedirectAttributes redirectAttributes, Locale locale) {
        if (!result.hasFieldErrors("username") && userDAO.existsUserByUsername(user.getUsername())) {
            result.rejectValue("username", "msg.user.username.exists");
        }
        if (result.hasErrors()) {
            model.addAttribute("listRoles", roleDAO.listAllRoles());
// ¡no olvidarlo!
            return "views/user/user-form";
        }
        try {
            user.setPasswordExpiresAt(
                    user.getLastPasswordChange().plusMonths(PASSWORD_VALIDITY_MONTHS));
            userDAO.insertUser(user);
        } catch (Exception e) {
            logger.error("Error al insertar el usuario {}: {}", user.getUsername(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.insert.error", null, locale));
        }
        return "redirect:/users";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult result,
                             Model model, RedirectAttributes redirectAttributes, Locale locale) {
        if (!result.hasFieldErrors("username")
                && userDAO.existsUserByUsernameAndNotId(user.getUsername(), user.getId())) {
            result.rejectValue("username", "msg.user.username.exists");
        }
        if (result.hasErrors()) {
            model.addAttribute("listRoles", roleDAO.listAllRoles());
            return "views/user/user-form";
        }
        try {
            user.setPasswordExpiresAt(
                    user.getLastPasswordChange().plusMonths(PASSWORD_VALIDITY_MONTHS));
            userDAO.updateUser(user);
        } catch (Exception e) {
            logger.error("Error al actualizar el usuario {}: {}", user.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.update.error", null, locale));
        }
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id,
                             RedirectAttributes redirectAttributes, Locale locale) {
        try {
            userDAO.deleteUser(id);
        } catch (Exception e) {
            logger.error("Error al eliminar el usuario {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.delete.error", null, locale));
        }
        return "redirect:/users";
    }
}
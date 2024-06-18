package com.backend.clinica_odontologica.view;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController implements ErrorController {
    @GetMapping({"/", "/index", "/index.html"})
    public String main() {
        return "index";
    }

    @GetMapping({"/pacientes", "/pacientes.html"})
    public String pacientes() {
        return "pacientes";
    }

    @GetMapping({"/odontologos", "/odontologos.html"})
    public String odontologos() {
        return "odontologos";
    }

    @RequestMapping("/error")
    public String handleError() {
        return "pages-error-404";
    }
}

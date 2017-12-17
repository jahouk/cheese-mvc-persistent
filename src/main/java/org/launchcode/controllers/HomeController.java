package org.launchcode.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class HomeController {

    // Request path: /
    @RequestMapping(value = "")
    public String index(Model model) {

        return "redirect:cheese";
    }
}

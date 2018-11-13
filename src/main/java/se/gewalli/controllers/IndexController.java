package se.gewalli.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RedirectView get(@RequestParam(value = "name", defaultValue = "") String name) {
        return new RedirectView("/swagger-ui.html");
    }
}


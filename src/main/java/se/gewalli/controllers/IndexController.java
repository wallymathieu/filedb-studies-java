package se.gewalli.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.gewalli.models.IndexModel;

@RestController
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public IndexModel get(@RequestParam(value = "name", defaultValue = "") String name) {
        return new IndexModel(name);
    }
}


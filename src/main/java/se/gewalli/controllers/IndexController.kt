package se.gewalli.controllers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
class IndexController {
    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    operator fun get(@RequestParam(value = "name", defaultValue = "") name: String?): RedirectView {
        return RedirectView("/swagger-ui.html")
    }
}
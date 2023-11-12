package be.patryksitko.contest.ip2location.com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReactAppController {

    @RequestMapping(value = { "/", "/{x:[\\w\\-]+}", "/{x:^(?!api$).*$}/*/{y:[\\w\\-]+}",
            "/{x:^(?!rsc$).*$}/*/{y:[\\w\\-]+}", "/error" })
    public String getIndex(HttpServletRequest request) {
        return "/index.html";
    }

}

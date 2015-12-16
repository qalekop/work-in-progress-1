package ak.scrabble.web.controller;

import ak.scrabble.web.security.SecurityModel;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by akopylov on 16.09.2015.
 */
@Controller
public class LoginController {

    public static final String LOGIN_PAGE = "login";
    public static final String LOGIN_URI = "/" + LOGIN_PAGE;

    @RequestMapping(value = LOGIN_URI, method = RequestMethod.GET)
    public String loginForm(SecurityContextHolderAwareRequestWrapper request) {
        if (request.isUserInRole(SecurityModel.ROLE)) {
            return "redirect:" + "/scrabble/200.html";
        }
        return LOGIN_PAGE;
    }
}

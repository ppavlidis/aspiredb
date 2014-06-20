package ubc.pavlab.aspiredb.server.controller;

import gemma.gsec.util.JSONUtil;
import gemma.gsec.util.SecurityUtil;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RemoteProxy
public class LoginController {

    @RequestMapping("/keep_alive.html")
    public void loadUser( HttpServletRequest request, HttpServletResponse response ) throws IOException {

        String jsonText = null;

        if ( !SecurityUtil.isUserLoggedIn() ) {
            jsonText = "{success:false}";

        } else {
            jsonText = "{success:true}";
        }

        JSONUtil jsonUtil = new JSONUtil( request, response );

        jsonUtil.writeToResponse( jsonText );

    }

    @RequestMapping("/login.html")
    public String showLogin( ModelMap model ) {
        return "login";
    }

}
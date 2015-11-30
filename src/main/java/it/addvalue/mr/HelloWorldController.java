package it.addvalue.mr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController
{
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public @ResponseBody String authenticationService(@RequestParam(value = "user", defaultValue = "stranger") String user)
    {
        return "Hello " + user;
    }
}

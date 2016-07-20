package com.bivgroup.websocket.restcontollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bush on 19.04.2016.
 */
@RestController
public class CommonApi {


    //@PreAuthorize("#oauth2.hasScope('server') or #name.equals('demo')"
    //path = "/test", method = RequestMethod.GET
    @RequestMapping("/test")
    public
    @ResponseBody
   String greeting(@RequestParam(value = "message",required=true, defaultValue = "Hellow World") String message) {
       // model.addAttribute("name", message);
        return null;
    }

    //@Valid @RequestBody
}

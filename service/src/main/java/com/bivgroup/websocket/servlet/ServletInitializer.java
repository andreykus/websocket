package com.bivgroup.websocket.servlet;


import com.bivgroup.websocket.app.SogCabLifeApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Created by bush on 20.04.2016.
 */

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        //SogCabLifeApplication.configure();
        return application.sources(SogCabLifeApplication.class);
    }
}


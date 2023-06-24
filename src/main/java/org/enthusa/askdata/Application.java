package org.enthusa.askdata;

import org.enthusa.askdata.config.GlobalSetting;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author henry
 * @date 2023/6/22
 */
@Controller
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Resource
    private GlobalSetting globalSetting;

    @GetMapping("/")
    public ModelAndView home(ModelAndView model) {
        model.addObject("env", globalSetting.getEnv());
        model.setViewName("main");
        return model;
    }
}

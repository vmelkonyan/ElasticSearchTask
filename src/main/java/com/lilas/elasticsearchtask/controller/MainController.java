package com.lilas.elasticsearchtask.controller;

import com.lilas.elasticsearchtask.constants.KeyConstants;
import com.lilas.elasticsearchtask.services.MainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class MainController {

    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @PostMapping("/upload")
    public String addToElastic(Model model,@RequestParam("file") MultipartFile file) {
        if(file.getOriginalFilename().contains(".zip")) {
            try {
                mainService.addToElastic(file.getInputStream(), model);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error_message","Something went wrong pleas try again.");
                return KeyConstants.SPLASH_VIEW_KEY;
            }
            return KeyConstants.STATISTIC_VIEW_KEY;
        }
        model.addAttribute("error_message","You can choose only zip file");
        return KeyConstants.SPLASH_VIEW_KEY;
    }

    @PostMapping("/find")
    public String find(Model model, @RequestParam() Map<String, String> form) {
        mainService.find(model, form);
        return KeyConstants.STATISTIC_VIEW_KEY;
    }


    @GetMapping("/deleteAll")
    public String deleteAll(Model model) {
        mainService.deleteAll(model);
        return KeyConstants.STATISTIC_VIEW_KEY;
    }

    @GetMapping("/")
    public String general() {
        return KeyConstants.SPLASH_VIEW_KEY;
    }

}

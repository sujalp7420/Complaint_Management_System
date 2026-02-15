package com.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
       @GetMapping("/login")
       public String loginHome(){
           return "Welcome!";
       }
}

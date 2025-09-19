package com.hieu.jobhunter.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
@GetMapping("/admin")
    public String DashBoard(){
        System.out.println("hello1w");
        return "admin/index";
    }


   
    
}

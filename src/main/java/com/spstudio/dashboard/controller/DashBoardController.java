package com.spstudio.dashboard.controller;

import com.spstudio.dashboard.service.DashboardProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {

    @Autowired
    DashboardProvider dashboardProvider;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("dashboard", dashboardProvider.getDashBoard());
        return "dashboard";
    }
}

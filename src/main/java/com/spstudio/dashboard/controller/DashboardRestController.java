package com.spstudio.dashboard.controller;

import com.spstudio.dashboard.service.DashboardProvider;
import com.spstudio.dashboard.service.model.DashboardLiveData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardRestController {

    @Autowired
    DashboardProvider dashboardProvider;

    @PostMapping("/dashboard")
    public ResponseEntity<Void> uploadLiveData(@RequestBody DashboardLiveData dashboardLiveData) {
        dashboardProvider.update(dashboardLiveData);
        return ResponseEntity.ok().build();
    }
}

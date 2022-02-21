package com.spstudio.dashboard.controller;

import com.spstudio.dashboard.service.DashboardProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DashboardRestController {

    @Autowired
    DashboardProvider dashboardProvider;

    @PostMapping("/dashboard")
    public ResponseEntity<Void> uploadLiveData(@RequestBody DashboardLiveData dashboardLiveData) {
        dashboardProvider.update(dashboardLiveData);
        return ResponseEntity.ok().build();
    }

    @Getter
    public static class DashboardLiveData {
        private List<Product> activeProducts;
        private List<SoldProduct> soldProducts;
    }

    @Getter
    public static class Product {
        private String productId;
        private Double holdingQuota;
        private Double basePrice;
        private Double balance;
        private Double latestCbPrice;
        private Long created;
    }

    @Getter
    public static class SoldProduct {
        private String productId;
        private Long timestamp;
        private Double soldPrice;
    }
}

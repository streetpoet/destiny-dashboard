package com.spstudio.dashboard.service;

import com.spstudio.dashboard.controller.DashboardRestController;
import com.spstudio.dashboard.service.model.Dashboard;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class DashboardProvider {

    private final DecimalFormat format = new DecimalFormat("0.#");
    private final Dashboard dashBoard = new Dashboard();

    public Dashboard getDashBoard() {
        return dashBoard;
    }

    public void update(DashboardRestController.DashboardLiveData dashboardLiveData) {
        List<Dashboard.DashboardProduct> matureDashboardProducts = new ArrayList<>();
        List<Dashboard.DashboardProduct> incubationDashboardProducts = new ArrayList<>();
        List<Dashboard.DashboardSoldProduct> dashboardSoldProducts = new ArrayList<>();
        dashBoard.setMatureDashboardProducts(matureDashboardProducts);
        dashBoard.setIncubationDashboardProducts(incubationDashboardProducts);
        dashBoard.setDashboardSoldProducts(dashboardSoldProducts);
        List<DashboardRestController.Product> activeProducts = dashboardLiveData.getActiveProducts();
        activeProducts.sort((p1, p2) -> p1.getLatestCbPrice() * 100 / (p1.getBasePrice() * 1.05) < p2.getLatestCbPrice() * 100 / (p2.getBasePrice() * 1.05) ? 1 : -1);
        for (DashboardRestController.Product product : activeProducts) {
            double maturePercentage = product.getLatestCbPrice() * 100 / (product.getBasePrice() * 1.05);
            Dashboard.DashboardProduct dashboardProduct = new Dashboard.DashboardProduct();
            dashboardProduct.setProductId(product.getProductId().substring(0, product.getProductId().indexOf("-")));
            try {
                String completePercentage = new BigDecimal(maturePercentage).setScale(2, RoundingMode.HALF_UP).toPlainString();
                dashboardProduct.setCompletePercentage(completePercentage);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            Long created = product.getCreated();
            if (created != null) {
                Duration between = Duration.between(Instant.ofEpochSecond(created), Instant.now());
                long days = between.abs().toDays();
                dashboardProduct.setLastDays(String.valueOf(days));
            }else {
                dashboardProduct.setLastDays("N/A");
            }
            if (product.getLatestCbPrice() >= product.getBasePrice()) {
                matureDashboardProducts.add(dashboardProduct);
            } else {
                incubationDashboardProducts.add(dashboardProduct);
            }
        }
        List<DashboardRestController.SoldProduct> soldProducts = dashboardLiveData.getSoldProducts();
        soldProducts.sort(Comparator.comparing(DashboardRestController.SoldProduct::getTimestamp));
        Collections.reverse(soldProducts);
        for (DashboardRestController.SoldProduct soldProduct : soldProducts) {
            if (soldProduct.getTimestamp() > Instant.now().minus(10L, ChronoUnit.DAYS).getEpochSecond()) {
                Dashboard.DashboardSoldProduct dashboardSoldProduct = new Dashboard.DashboardSoldProduct();
                dashboardSoldProduct.setProductId(soldProduct.getProductId());
                dashboardSoldProduct.setCompletePercentage("100");
                dashboardSoldProduct.setSoldPrice(format.format(soldProduct.getSoldPrice()));
                dashboardSoldProduct.setSoldDateTime(LocalDateTime.ofEpochSecond(soldProduct.getTimestamp(), 0, ZoneOffset.UTC).toString());
                dashboardSoldProducts.add(dashboardSoldProduct);
            }
        }
    }
}

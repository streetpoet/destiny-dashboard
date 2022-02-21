package com.spstudio.dashboard.service.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Dashboard {

    private List<DashboardProduct> matureDashboardProducts = Collections.emptyList();
    private List<DashboardProduct> incubationDashboardProducts = Collections.emptyList();
    private List<DashboardSoldProduct> dashboardSoldProducts = Collections.emptyList();

    @Data
    public static class DashboardProduct {
        private String productId;
        private String completePercentage;
        private String lastDays;
    }

    @Data
    public static class DashboardSoldProduct extends DashboardProduct{
        private String soldPrice;
        private String soldDateTime;
    }
}

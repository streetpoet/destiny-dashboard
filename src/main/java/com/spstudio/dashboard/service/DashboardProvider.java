package com.spstudio.dashboard.service;

import com.spstudio.dashboard.service.model.DashboardLiveData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardProvider {
    private final DashboardLiveData dashBoard = new DashboardLiveData();

    public DashboardLiveData getDashBoard() {
        return dashBoard;
    }

    public void update(DashboardLiveData dashboardLiveData) {
        List<DashboardLiveData.DashboardProductStatus> activeProducts = dashboardLiveData.getActiveProducts();
        activeProducts.sort((p1, p2) -> {
            double v1 = p1.getLatestCbPrice() * 100 / (p1.getBasePrice() * p1.getPreferredProfit());
            double v2 = p2.getLatestCbPrice() * 100 / (p2.getBasePrice() * p2.getPreferredProfit());
            return Double.compare(v2, v1);
        });
        synchronized (dashBoard) {
            dashBoard.setActiveProducts(activeProducts);
        }
    }
}

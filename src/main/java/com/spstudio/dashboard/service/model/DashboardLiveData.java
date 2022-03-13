package com.spstudio.dashboard.service.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DashboardLiveData {

    private List<DashboardProductStatus> activeProducts = new ArrayList<>();
    private AccountInfo accountInfo = new AccountInfo();

    @Data
    public static class DashboardProductStatus {
        private String productId;
        private Double basePrice;
        private Double latestCbPrice;
        private Double preferredProfit;
        private Boolean isLongTerm;
        private Integer buyCount;
        private Long created;
    }

    @Data
    public static class AccountInfo {
        private Double balance = 1.0;
        private Double usdBalance = 0.0;
        private Double sharpUpRate = 0.0;
        private Double sharpDownRate = 0.0;
        private Double reshuffleProgress = 0.0;
        private Double reshuffleProgress2 = 0.0;
    }
}

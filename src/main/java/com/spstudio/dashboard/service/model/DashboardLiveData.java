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
        private Double totalFundAmount = 1.0;
        private Double balance = 0.0;
    }
}

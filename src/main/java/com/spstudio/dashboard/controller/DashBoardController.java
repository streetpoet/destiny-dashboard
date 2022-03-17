package com.spstudio.dashboard.controller;

import com.spstudio.dashboard.service.DashboardProvider;
import com.spstudio.dashboard.service.model.DashboardLiveData;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashBoardController {

    private final DecimalFormat df = new DecimalFormat("#,###.##");

    @Autowired
    DashboardProvider dashboardProvider;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("dashboard", buildWebPageProductModelList());
        model.addAttribute("account", buildWebPageAccount());
        return "dashboard";
    }

    private DashBoardAccount buildWebPageAccount() {
        DashBoardAccount dashBoardAccount = new DashBoardAccount();
        DashboardLiveData.AccountInfo accountInfo = dashboardProvider.getDashBoard().getAccountInfo();
        dashBoardAccount.setBalance(df.format(accountInfo.getBalance()));
        double utilizeRate = (accountInfo.getBalance() - accountInfo.getUsdBalance()) * 100.0 / accountInfo.getBalance();
        dashBoardAccount.setUtilizeRate(BigDecimal.valueOf(utilizeRate).setScale(0, RoundingMode.UP).toPlainString());
        dashBoardAccount.setUsdBalance(df.format(accountInfo.getUsdBalance()));
        dashBoardAccount.setSharpUpRate(df.format(accountInfo.getSharpUpRate() * 100.0));
        dashBoardAccount.setSharpDownRate(df.format(accountInfo.getSharpDownRate() * 100.0));
        dashBoardAccount.setReshuffleProgress(df.format(accountInfo.getReshuffleProgress() * 100.0));
        dashBoardAccount.setReshuffleProgress2(df.format(accountInfo.getReshuffleProgress2() * 100.0));
        return dashBoardAccount;
    }

    private List<DashBoardModel> buildWebPageProductModelList() {
        List<DashBoardModel> products = dashboardProvider.getDashBoard().getActiveProducts().stream().map(x -> {
            DashBoardModel dashBoardModel = new DashBoardModel();
            dashBoardModel.setProductId(x.getProductId());
            long days = Instant.now().minusSeconds(x.getCreated()).getEpochSecond() / 24 / 3600L;
            dashBoardModel.setBuyDays(String.valueOf(days));
            dashBoardModel.setBuyCount(String.valueOf(x.getBuyCount()));
            double rate = x.getLatestCbPrice() * 100 / (x.getBasePrice() * (1.01 + x.getPreferredProfit()));
            dashBoardModel.setCompleteRate(df.format(rate));
            dashBoardModel.setCompleteRateInDouble(rate);
            dashBoardModel.setLongTerm(x.getIsLongTerm());
            return dashBoardModel;
        }).sorted(Comparator.comparing(DashBoardModel::getCompleteRateInDouble)).collect(Collectors.toList());
        Collections.reverse(products);
        return products;
    }

    @Data
    static class DashBoardAccount {
        private String balance;
        private String usdBalance;
        private String utilizeRate;
        private String sharpUpRate;
        private String sharpDownRate;
        private String reshuffleProgress;
        private String reshuffleProgress2;
    }

    @Data
    static class DashBoardModel {
        private String productId;
        private String completeRate;
        private String buyCount;
        private String buyDays;
        private Boolean longTerm;
        private Double completeRateInDouble;
    }
}

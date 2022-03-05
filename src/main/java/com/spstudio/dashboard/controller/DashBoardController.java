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

    private final DecimalFormat df = new DecimalFormat("0.#");

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
        double utilizeRate = (accountInfo.getTotalFundAmount() - accountInfo.getBalance()) * 100 / accountInfo.getTotalFundAmount();
        dashBoardAccount.setUtilizeRate(BigDecimal.valueOf(utilizeRate).setScale(0, RoundingMode.UP).toPlainString());
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
            dashBoardModel.setLongTerm(x.getIsLongTerm());
            return dashBoardModel;
        }).sorted(Comparator.comparing(DashBoardModel::getCompleteRate)).collect(Collectors.toList());
        Collections.reverse(products);
        return products;
    }

    @Data
    static class DashBoardAccount {
        private String balance;
        private String utilizeRate;
    }

    @Data
    static class DashBoardModel {
        private String productId;
        private String completeRate;
        private String buyCount;
        private String buyDays;
        private Boolean longTerm;
    }
}

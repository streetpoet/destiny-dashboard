package com.spstudio.dashboard.controller;

import com.spstudio.dashboard.service.DashboardProvider;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;
import java.time.Instant;
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
        List<DashBoardModel> products = dashboardProvider.getDashBoard().getActiveProducts().stream().map(x -> {
            DashBoardModel dashBoardModel = new DashBoardModel();
            dashBoardModel.setProductId(x.getProductId());
            long days = Instant.now().minusSeconds(x.getCreated()).getEpochSecond() / 24 / 3600L;
            dashBoardModel.setBuyDays(String.valueOf(days));
            dashBoardModel.setBuyCount(String.valueOf(x.getBuyCount()));
            double rate = x.getLatestCbPrice() * 100 / (x.getBasePrice() * x.getPreferredProfit());
            dashBoardModel.setCompleteRate(df.format(rate));
            dashBoardModel.setLongTerm(x.getIsLongTerm());
            return dashBoardModel;
        }).collect(Collectors.toList());
        return "dashboard";
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

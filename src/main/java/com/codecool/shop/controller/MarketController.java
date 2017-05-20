package com.codecool.shop.controller;

import com.codecool.shop.model.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class MarketController {

    public enum pageName {
        MARKET
    }

    public static ModelAndView all(Request req, Response res) {
        User user = new User();
        user.initFromSession(req.session());

        if (user.getId() == 0) {
            return IndexController.homePage(req, res);

        } else {
            Map params = new HashMap<>();
            ArrayList<Sharehold> shareholds = Market.findAll();

            params.put("page", pageName.MARKET.name());
            params.put("user", user);
            params.put("shareholds", shareholds);

            return new ModelAndView(params, "market/list");
        }
    }

    public static ModelAndView sell(Request req, Response res) {
        Market.insert(
                Integer.parseInt(req.params("id")),
                Integer.parseInt(req.queryParams("quantity")),
                Integer.parseInt(req.queryParams("quantity")) * 120000,
                new Date(System.currentTimeMillis()),
                null,
                true
        );

        return ShareholdController.list(req, res);
    }

}

package com.codecool.shop.controller;

import com.codecool.shop.model.Collection;
import com.codecool.shop.model.Investment;
import com.codecool.shop.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class InvestmentController {

    public enum pageName {
        INVESTMENTS
    }

    public static ModelAndView list(Request req, Response res) {
        User user = new User();
        user.initFromSession(req.session());

        if (user.getId() > 0) {
            Map params = new HashMap<>();
            ArrayList investments = Investment.findByUid(user.getId());

            params.put("user", user);
            params.put("page", pageName.INVESTMENTS.name());
            params.put("investments", investments);

            return new ModelAndView(params, "investments/list");
        } else {
            return UserController.loginGet(req,res);
        }
    }

}

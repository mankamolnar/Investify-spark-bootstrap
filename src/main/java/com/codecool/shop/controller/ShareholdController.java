package com.codecool.shop.controller;

import com.codecool.shop.model.Sharehold;
import com.codecool.shop.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class ShareholdController {

    public enum pageName {
        SHAREHOLDS
    }

    public static ModelAndView list(Request req, Response res) {
        User user = new User();
        user.initFromSession(req.session());

        if (user.getId() > 0) {
            Map params = new HashMap<>();
            ArrayList shareholds = Sharehold.findByUid(user.getId());
            List<Sharehold> shareholdsForSale = user.getShareholdsForSale();

            params.put("user", user);
            params.put("page", pageName.SHAREHOLDS.name());
            params.put("shareholds", shareholds);
            params.put("shareholdsForSale", shareholdsForSale);
            System.out.println(shareholds);

            return new ModelAndView(params, "shareholds/list");
        } else {
            return UserController.loginGet(req,res);
        }
    }

}

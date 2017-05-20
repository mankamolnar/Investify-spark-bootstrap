package com.codecool.shop.controller;

import com.codecool.shop.model.Collection;
import com.codecool.shop.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class IndexController {

    public enum pageName {
        INDEX, FAQ
    }

    public static ModelAndView homePage(Request req, Response res) {
        User user = new User();
        user.initFromSession(req.session());
        Collection collection = Collection.findActive();

        Map params = new HashMap<>();
        params.put("page",pageName.INDEX.name());
        params.put("collection", collection);
        params.put("user", user);

        if (user.getId() == 0) {
            return new ModelAndView(params, "index");
        } else {
            return new ModelAndView(params, "user/dashboard");
        }
    }

    public static ModelAndView faq(Request req, Response res) {
        User user = new User();
        user.initFromSession(req.session());

        Map params = new HashMap<>();
        params.put("page", pageName.FAQ.name());
        params.put("user", user);
        return new ModelAndView(params, "faq/index");
    }
}

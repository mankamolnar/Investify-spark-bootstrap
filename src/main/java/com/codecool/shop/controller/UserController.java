package com.codecool.shop.controller;

import com.codecool.shop.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 07..
 */
public class UserController {

    private enum pageName {
        LOGIN, REGISTRATION, PAYIN
    }

    public static ModelAndView loginGet(Request req, Response res) {
        User user = new User();
        user.initFromSession(req.session());

        Map params = new HashMap<>();
        params.put("page", pageName.LOGIN.name());
        params.put("user", user);
        return new ModelAndView(params, "user/login");
    }

    public static ModelAndView loginPost(Request req, Response res) {
        Map params = new HashMap<>();

        User user = User.auth(req.queryParams("user"), req.queryParams("password"));
        if (user.getId() != 0) {
            user.saveToSession(req.session());
        }

        params.put("page", pageName.LOGIN.name());
        params.put("user", user);
        return new ModelAndView(params, "user/dashboard");
    }

    public static ModelAndView registrationGet(Request req, Response res) {
        User user = new User();
        user.initFromSession(req.session());

        Map params = new HashMap<>();
        params.put("page", pageName.REGISTRATION.name());
        params.put("user", user);

        return new ModelAndView(params, "user/registration");
    }

    public static ModelAndView registrationPost(Request req, Response res) {
        User user = new User();
        User.insert(req.queryParams("user"), req.queryParams("password"), req.queryParams("email"), 0, 0);


        Map params = new HashMap<>();
        params.put("page", pageName.REGISTRATION.name());
        params.put("user", user);
        params.put("success", "ok");
        return new ModelAndView(params, "user/registration");
    }

    public static  ModelAndView logout(Request req, Response res) {
        User user = new User();
        user.destroyInSession(req.session(), "user");

        return IndexController.homePage(req, res);

    }

    public static ModelAndView payIn(Request req, Response res) {
        User user = new User();
        user.initFromSession(req.session());

        if (user.getId() != 0) {
            user.payIn(100000);
            user.saveToPsql();
            user.saveToSession(req.session());
        }

        Map params = new HashMap<>();
        params.put("page", pageName.PAYIN.name());
        params.put("user", user);

        return new ModelAndView(params, "user/dashboard");
    }
}

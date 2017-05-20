package com.codecool.shop.controller;

import com.codecool.shop.model.HousePicture;
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
public class ApiController {

    public static String getPhotos(Request req, Response res) {
        User user = new User();
        user.initFromSession(req.session());

        String pictures = HousePicture.findByHouseToJson(req.params("id"));
        System.out.println(pictures);

        return pictures;
    }

}

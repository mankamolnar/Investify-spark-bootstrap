import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import com.codecool.shop.controller.*;
import com.codecool.shop.model.Sharehold;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) {

        // default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(getHerokuAssignedPort());

        get("/", /*IndexController::homePage*/(req, res) -> "hello");
        get("/faq", IndexController::faq, new ThymeleafTemplateEngine());

        get("/login", UserController::loginGet, new ThymeleafTemplateEngine());
        post("/login", UserController::loginPost, new ThymeleafTemplateEngine());
        get("/registration", UserController::registrationGet, new ThymeleafTemplateEngine());
        post("/registration", UserController::registrationPost, new ThymeleafTemplateEngine());
        get("/payin", UserController::payIn, new ThymeleafTemplateEngine());
        get("/logout", UserController::logout, new ThymeleafTemplateEngine());

        get("/collections", CollectionController::collections, new ThymeleafTemplateEngine());
        post("/collections/payin/:cid", CollectionController::payIn, new ThymeleafTemplateEngine());

        get("/investments", InvestmentController::list, new ThymeleafTemplateEngine());
        get("/shareholds", ShareholdController::list, new ThymeleafTemplateEngine());

        get("/market", MarketController::all, new ThymeleafTemplateEngine());
        post("/market/sell/:id", MarketController::sell, new ThymeleafTemplateEngine());

        get("/api/get-pictures/:id", ApiController::getPhotos);

        // Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}

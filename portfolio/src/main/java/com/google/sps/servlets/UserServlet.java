package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;
import com.google.gson.Gson;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    /**
     * Checks state of user in this class.
     */
    private static UserService userService;

    @Override
    public void init() {
        userService = UserServiceFactory.getUserService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url;
        boolean loggedIn = userService.isUserLoggedIn();
        if(loggedIn){
            url = userService.createLogoutURL("/experiments.html");
        }
        else{
            url = userService.createLoginURL("/experiments.html");
        }
        String userResponseJSON = new UserResponse(url, loggedIn).getJson();
        response.setContentType("application/json;");
        response.getWriter().println(userResponseJSON);
    }

    private final class UserResponse {
        /**
         * URL for user to login/logout.
         */
        private String url;

        /**
         * True if user is logged in and false otherwise.
         */
        private boolean loggedIn;

        /**
         * Constructor for response to send back to user.
         * @param url URL for logging in or out.
         * @param loggedIn Whether user is logged in or out.
         */
        UserResponse(String url, boolean loggedIn){
            this.url = url;
            this.loggedIn = loggedIn;
        }

        public String getJson(){
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }
}

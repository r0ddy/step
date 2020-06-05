// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.Comment;
import com.google.sps.data.CommentUtil;
import com.google.gson.Gson;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/comments")
public class CommentServlet extends HttpServlet {

    /**
     * Logs any exceptions in this class.
     */
    private static Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger("com.google.sps.commentutil");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String numCommentsValue = request.getParameter("numComments");
        Integer numComments = null;
        try{
            numComments = (Integer) Integer.parseInt(numCommentsValue);
        }
        catch(NumberFormatException e){
            logger.log(Level.WARNING, "Cannot parse numComments", e);
        }
        finally{
            String json = getCommentsJSON(numComments);
            response.setContentType("application/json;");
            response.getWriter().println(json);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String parentIdValue = request.getParameter("parentId");
        String textValue = request.getParameter("text");
        if(parentIdValue == null){
            CommentUtil.addComment(textValue);
        }
        else{
            try{
                Long parentId = Long.parseLong(parentIdValue);
                CommentUtil.addResponse(textValue, parentId);
            } catch(NumberFormatException e) {
                logger.log(Level.WARNING, "Cannot parse parentId", e);
            }
        }
        response.sendRedirect("/experiments.html");
    }

    /**
     * Converts list of comments stored in Datastore into JSON.
     * Used for sending response to user.
     * @return JSON format of comments list as a String.
     */
    private static String getCommentsJSON(Integer numComments){
        List<Comment> comments = CommentUtil.getComments(numComments);
        Gson gson = new Gson();
        String json = gson.toJson(comments);
        return json;
    }
}

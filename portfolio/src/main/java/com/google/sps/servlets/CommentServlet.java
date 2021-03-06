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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import com.google.sps.data.CommentBuilder;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/comments")
public class CommentServlet extends HttpServlet {

    /**
     * Logs any exceptions in this class.
     */
    private static Logger logger;

    /**
     * Checks state of user in this class.
     */
    private static UserService userService;

    /**
     * Used to retrieves blobkeys from a request.
     */
    private static BlobstoreService blobstoreService;

    /**
     * Use get a URL that points to the uploaded file.
     */
    private static ImagesService imagesService;

    @Override
    public void init() {
        blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        imagesService = ImagesServiceFactory.getImagesService();
        logger = Logger.getLogger("com.google.sps.commentservlet");
        userService = UserServiceFactory.getUserService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String numCommentsValue = request.getParameter("numComments");
        String uploadUrl = blobstoreService.createUploadUrl("/comments");
        Integer numComments = null;
        try{
            numComments = (Integer) Integer.parseInt(numCommentsValue);
        }
        catch(NumberFormatException e){
            logger.log(Level.WARNING, "Cannot parse numComments", e);
        }
        finally{
            List<Comment> comments = CommentUtil.getComments(numComments);
            String commentsJson = new CommentsResponse(uploadUrl, comments).getJson();
            response.setContentType("application/json;");
            response.getWriter().println(commentsJson);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(userService.isUserLoggedIn()){
            User currentUser = userService.getCurrentUser();
            String userNickname = currentUser.getNickname();
            String parentIdValue = request.getParameter("parentId");
            String textValue = request.getParameter("text");
            String imageUrl = getUploadedFileUrl(request, "image");
            CommentBuilder commentBuilder = new CommentBuilder(userNickname, textValue);
            commentBuilder.setImageUrl(imageUrl);
            if(parentIdValue != null){
                try{
                    Long parentId = Long.parseLong(parentIdValue);
                    commentBuilder.setParentId(parentId);
                } catch(NumberFormatException e) {
                    logger.log(Level.WARNING, "Cannot parse parentId", e);
                }
            }
            Comment comment = commentBuilder.build();
            CommentUtil.storeComment(comment);
            logger.log(Level.INFO, "Comment stored");
        }
        response.sendRedirect("/experiments.html");
    }

    /**
     * Takes request with encoded image and returns its url.
     * @param request Request that image is encoded in.
     * @param formInputElementName Name of image input.
     * @return Image url as a String.
     */
    private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get(formInputElementName);

        // User submitted form without selecting a file, so we can't get a URL. (dev server)
        if (blobKeys == null || blobKeys.isEmpty()) {
            return null;
        }

        // Our form only contains a single file input, so get the first index.
        BlobKey blobKey = blobKeys.get(0);

        // User submitted form without selecting a file, so we can't get a URL. (live server)
        BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
        if (blobInfo.getSize() == 0) {
            blobstoreService.delete(blobKey);
            return null;
        }

        // We could check the validity of the file here, e.g. to make sure it's an image file
        // https://stackoverflow.com/q/10779564/873165

        ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);

        // To support running in Google Cloud Shell with AppEngine's devserver, we must use the relative
        // path to the image, rather than the path returned by imagesService which contains a host.
        try {
            URL url = new URL(imagesService.getServingUrl(options));
            return url.getPath();
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Cannot parse Blobstore URL", e);
            return imagesService.getServingUrl(options);
        }
    }

    private final class CommentsResponse {
        /**
         * The url for a form to POST to store a file in Blobstore.
         * Note: Must be generated by BlobstoreService
         */
        private String blobstoreUrl;

        /**
         * The comments to display to the user.
         */
        private List<Comment> comments;

        /**
         * Constructor to use when responding to request for comments on load.
         * @param blobstoreUrl Url where images are stored.
         * @param comments List of comments to display to user.
         */
        CommentsResponse(String blobstoreUrl, List<Comment> comments) {
            this.blobstoreUrl = blobstoreUrl;
            this.comments = comments;
        }

        public String getJson() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }
}

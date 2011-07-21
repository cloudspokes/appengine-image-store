package com.cloudspokes.gae.imagestorage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesServiceFactory;

/**
 * GAEISUploadResult servlet handles redirects received after the image has been uploaded,
 * and generates a JSON response indicating the success or the failure
 * of the upload process
 * 
 * @author mural
 * @version $Id$
 */
@SuppressWarnings("serial")
public class GAEISUploadResult extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("error") != null) {
            doErrorResponse(resp, req.getParameter("error"));
        } else if (req.getParameter("blobKey") != null) {
            doBlobKeyResponse(resp, req.getParameter("blobKey"));
        } else {
            doErrorResponse(resp, "Invalid call");
        }
    }

    private void doBlobKeyResponse(HttpServletResponse resp, String blobKey) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.write("{\"success\": true, \"blobKey\":\"" + blobKey + "\", \"servingURL\": \""+ ImagesServiceFactory.getImagesService().getServingUrl(new BlobKey(blobKey)) +"\"}");
    }

    private void doErrorResponse(HttpServletResponse resp, String message) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.write("{\"success\": false, \"message\":\"" + message + "\"}");
    }
}

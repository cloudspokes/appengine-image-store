package com.cloudspokes.gae.imagestorage;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/**
 * GAEISUpload servlet allows uploading Images into the Blobstore.<p/>
 * 
 * Additionally, all GET methods are responded with a new UploadURL  
 * 
 * @author mural
 * @version $Id$
 */
@SuppressWarnings("serial")
public class GAEISUpload extends HttpServlet {

    /**
     * The GET method is used to obtain a JSON with the BlobStore upload URL
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("{\"url\": \""+BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/upload")+"\"}");
    }
    
    
    /**
     * The POST is used to receive the call from GAE Blobstore service.<p/>
     * 
     * It redirects to the <code>upload-result</code> servlet, as required by the BlobStore upload service
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, BlobKey> blobs = blobStoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("image");
        if (blobKey == null) {
            resp.sendRedirect("upload-result?error=Image could not be loaded");
        } else {
            resp.sendRedirect("upload-result?blobKey=" + blobKey.getKeyString());
        }
    }
}

package com.cloudspokes.gae.imagestorage.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;

/**
 * The ImagesResource provides services which operates on the full set of Images
 * 
 * @author mural
 * @version $Id$
 */
@Path("/images")
public class ImagesResource {

    public ImagesResource() {
    }

    /**
     * Returns a {@link List<MyBlobInfo>}  of all the Blobs stored in the Datastore
     * 
     * @return the list of all images metadata
     */
    @GET
    @Produces("application/json")
    public GenericEntity<List<MyBlobInfo>> getImages() {
        ArrayList<MyBlobInfo> result = new ArrayList<MyBlobInfo>();
        Iterator<BlobInfo> blobInfos = new BlobInfoFactory().queryBlobInfos();
        while (blobInfos.hasNext()) {
            result.add(MyBlobInfo.from(blobInfos.next()));
        }
        Collections.sort(result, new Comparator<MyBlobInfo>() {

            @Override
            public int compare(MyBlobInfo o1, MyBlobInfo o2) {
                int compResult = o1.getFilename().compareTo(o2.getFilename());
                if (compResult == 0) {
                    compResult = o1.getBlobKey().compareTo(o2.getBlobKey());
                }
                return compResult;
            }
        });
        return new GenericEntity<List<MyBlobInfo>>(result) { };
    }
}

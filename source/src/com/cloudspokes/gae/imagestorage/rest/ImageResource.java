package com.cloudspokes.gae.imagestorage.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

/**
 * The {@link ImageResource} class provides REST services to allow users of it
 * to obtain images stored in BlobStore as well as transformed versions of
 * it.</p>
 * 
 * @author mural
 * @version $Id$
 */
@Path("/images/{imageKey}")
public class ImageResource {

    /**
     * The cache max age to set for all responses generated from this rest service
     */
    private static final int CACHE_MAX_AGE = 15 * 24 * 3600;

    /**
     * Obtains the image identified by the given key
     * 
     * @param imageKey
     *            The key of the image
     * 
     * @return The image
     */
    @GET
    @Produces("image/*")
    public Response getContent(@PathParam("imageKey") String imageKey) {
        return doTransform(imageKey, ImagesServiceFactory.makeCrop(0, 0, 1, 1));
    }

    /**
     * Obtains the image flipped Horizontally identified by the given key
     * 
     * @param imageKey
     *            The key of the image
     * 
     * @return The transformed image
     */
    @GET
    @Produces("image/*")
    @Path("flipH")
    public Response getHorizontalFlip(@PathParam("imageKey") String imageKey) {
        return doTransform(imageKey, ImagesServiceFactory.makeHorizontalFlip());
    }

    /**
     * Obtains the image flipped Vertically identified by the given key
     * 
     * @param imageKey
     *            The key of the image
     * 
     * @return The transformed image
     */
    @GET
    @Produces("image/*")
    @Path("flipV")
    public Response getVerticalFlip(@PathParam("imageKey") String imageKey) {
        return doTransform(imageKey, ImagesServiceFactory.makeVerticalFlip());
    }

    /**
     * Obtains a image resized version of the image identified by the given key
     * 
     * @param imageKey
     *            The key of the image
     * @param width
     *            The width in pixels
     * @param height
     *            The height in pixels
     * 
     * @return The transformed image
     */
    @GET
    @Produces("image/*")
    @Path("resized")
    public Response getResized(@PathParam("imageKey") String imageKey, @QueryParam("width") int width, @QueryParam("height") int height) {
        return doTransform(imageKey, ImagesServiceFactory.makeResize(width, height));
    }

    /**
     * Obtains a rotated version of the image identified by the given key
     * 
     * @param imageKey
     *            The key of the image
     * @param degrees
     *            The number of degrees to rotate the image
     * 
     * @return The transformed image
     */
    @GET
    @Produces("image/*")
    @Path("rotated")
    public Response getRotated(@PathParam("imageKey") String imageKey, @QueryParam("degrees") int degrees) {
        return doTransform(imageKey, ImagesServiceFactory.makeRotate(degrees));
    }

    /**
     * Obtains a cropped version of the image identified by the given key
     * 
     * @param imageKey
     *            The key of the image
     * 
     * @return The transformed image
     */
    @GET
    @Produces("image/*")
    @Path("cropped")
    public Response getCropped(@PathParam("imageKey") String imageKey, @DefaultValue("0") @QueryParam("leftX") double leftX,
            @DefaultValue("0") @QueryParam("topY") double topY, @DefaultValue("1") @QueryParam("rightX") double rightX,
            @DefaultValue("1") @QueryParam("bottomY") double bottomY) {
        return doTransform(imageKey, ImagesServiceFactory.makeCrop(leftX, topY, rightX, bottomY));
    }

    /**
     * Obtains a lucky version of the image identified by the given key, as
     * stated by the image service of GAE.
     * 
     * @param imageKey
     *            The key of the image
     * 
     * @return The transformed image
     */
    @GET
    @Produces("image/*")
    @Path("lucky")
    public Response getLuckyOne(@PathParam("imageKey") String imageKey) {
        return doTransform(imageKey, ImagesServiceFactory.makeImFeelingLucky());
    }

    /**
     * Obtains the serving URL for the given image
     * 
     * @param imageKey The image key
     * 
     * @return The serving URL for the image.
     */
    @GET
    @Produces("application/json")
    @Path("servingURL")
    public Response getServingURL(@PathParam("imageKey") String imageKey) {
        try {
            String servingUrl = ImagesServiceFactory.getImagesService().getServingUrl(new BlobKey(imageKey));
            return addCacheControlAndBuild(Response.ok("{\"url\": \"" + servingUrl + "\"}"));
        } catch (IllegalArgumentException e) {
            return Response.noContent().status(Status.NOT_FOUND).build();
        }
    }

    /**
     * Obtains metadata associated with the given image
     * 
     * @param imageKey The image key
     * 
     * @return The metadata
     */
    @GET
    @Produces("application/json")
    @Path("metadata")
    public Response getMetadata(@PathParam("imageKey") String imageKey) {
        try {
            BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(new BlobKey(imageKey));
            return addCacheControlAndBuild(Response.ok(MyBlobInfo.from(blobInfo)));
        } catch (IllegalArgumentException e) {
            return Response.noContent().status(Status.NOT_FOUND).build();
        }
    }

    /*
     * Apply the give transformation to the image identified by the given key
     */
    private Response doTransform(String imageKey, Transform transform) {
        Image image = ImagesServiceFactory.makeImageFromBlob(new BlobKey(imageKey));
        image = ImagesServiceFactory.getImagesService().applyTransform(transform, image);
        return buildResponseFromImage(image);
    }

    /*
     * Builds a response from the given image
     */
    private Response buildResponseFromImage(Image image) {
        ResponseBuilder response = Response.ok(image.getImageData(), MediaType.valueOf("image/" + image.getFormat().toString()));
        return addCacheControlAndBuild(response);
    }

    /*
     * Adds cache control header
     */
    private Response addCacheControlAndBuild(ResponseBuilder response) {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(CACHE_MAX_AGE);
        return response.cacheControl(cacheControl).build();
    }
}

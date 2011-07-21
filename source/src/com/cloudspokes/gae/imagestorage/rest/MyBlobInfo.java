package com.cloudspokes.gae.imagestorage.rest;

import java.io.Serializable;

import com.google.appengine.api.blobstore.BlobInfo;

/**
 *
 * @author mural
 * @version $Id$
 */
public class MyBlobInfo implements Serializable{
    private static final long serialVersionUID = -5806564008041647052L;
    private long size;
    private String blobKey;
    private String mimeType;
    private String filename;
    private long creationDate;

    public MyBlobInfo() {
    }
    
    public MyBlobInfo(long size, String blobKey, String mimeType, long creationDate, String filename) {
        super();
        this.size = size;
        this.blobKey = blobKey;
        this.mimeType = mimeType;
        this.filename = filename;
        this.creationDate = creationDate;
    }
    
    public static MyBlobInfo from(BlobInfo b) {
        return new MyBlobInfo(b.getSize(), b.getBlobKey().getKeyString(), b.getContentType(), b.getCreation().getTime(), b.getFilename());
    }
    
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public String getBlobKey() {
        return blobKey;
    }
    public void setBlobKey(String blobKey) {
        this.blobKey = blobKey;
    }
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    public long getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getFilename() {
        return filename;
    }
}
 
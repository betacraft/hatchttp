package com.rainingclouds.hatchttp.extentions;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;



/**
 * Created by akshay on 13/09/14.
 */

public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";
    public String getMethod() { return METHOD_NAME; }

    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }
    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }
    public HttpDeleteWithBody() { super(); }
}
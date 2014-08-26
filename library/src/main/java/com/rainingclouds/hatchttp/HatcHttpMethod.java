package com.rainingclouds.hatchttp;

import io.netty.handler.codec.http.HttpMethod;

/**
 * HatcHttp methods supported
 * Created by akshay on 25/08/14.
 */
public enum  HatcHttpMethod {
    POST(HttpMethod.POST),
    GET(HttpMethod.GET),
    PUT(HttpMethod.PUT),
    DELETE(HttpMethod.DELETE),
    PATCH(HttpMethod.PATCH);


    private HttpMethod mHttpMethod;

    HatcHttpMethod(final HttpMethod method){
        mHttpMethod = method;
    }

    HatcHttpMethod getByMethod(final HttpMethod method){
        for(HatcHttpMethod hatcHttpMethod : HatcHttpMethod.values()){
            if(hatcHttpMethod.getHttpMethod().equals(method)){
                return hatcHttpMethod;
            }
        }
        return null;
    }

    HttpMethod getHttpMethod(){
        return mHttpMethod;
    }


}

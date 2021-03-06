# HatcHttp

HatcHttp is a very simple and straightforward library for performing HTTP call inside your
Android app. ~~This library is based on Netty~~

### Removed netty based solution.

We were facing an issue related with 65k+ method issue. So now this library uses Volley.
 
## Features

~~1. Based on Java NIO (thanks to netty and asynchttp)~~

1. Asynchronous API calls, runs on a separate thread pool (ExecutorService)
2. Uses network library used inside the Android framework.

~~## What is different ?~~

~~### Based on Netty~~

~~This version of HatchHttp is based on Netty and leverage the advantage of NIO.~~

## How to use ?

1. Add the repository
```gradle
    maven {
            url 'https://github.com/RainingClouds/hatchttp_maven_repo/raw/master/'
    }
```
2. Add the dependency
```gradle
    compile 'hatchttp:library:1.3.7'
```
Add following permissions in your AndroidManifest.xml
```xml
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
 
### Code
Example code for getting Google Home Page
```java
    HatcHttpRequest.GET("http://www.google.com")
                .addHeader("Accept", "text/html")
                .execute(new HatcHttpRequestListener() {
            @Override
            public void onComplete(int status, final String response) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
```

If you want encoded json response use HatcHttpJSONListener which has following listener design.
```java
    public interface HatcHttpJSONListener {
        void onComplete(final int status, final JSONObject response);
        void onException(final Throwable throwable);
    }
```


# Developer

Akshay Deo (akshay@rainingclouds.com)
RainingClouds Technologies Pvt Ltd

# Licence 

The MIT License (MIT)

Copyright (c) 2014 Akshay Deo
Copyright (c) 2014 RainingClouds Technologies Private Limited

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

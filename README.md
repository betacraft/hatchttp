# HatcHttp

HatcHttp is a very simple and straightforward library for performing HTTP call inside your Android app. This library
is based on Netty
 
## Features

1. Based on Java NIO 
2. Asynchronous API calls, runs on a separate thread pool (ExecutorService)

## What is different ?

### Based on Netty

This version of HatchHttp is based on Netty and leverage the advantage of NIO.

 
## How to use ?

1. Clone this repo
2. Perform gradle build
3. Get corresponding .aar file in build folder of the Library project
4. Create a new folder in your project (on the level of assets) aars
5. Copy the built aar into this folder
6. Add following as repo in your build gradle
```gradle
 repositories {
        ...
        flatDir {
            dirs 'aars'
        }
    }
```
And add following dependency
```gradle
compile(name: 'hatchttp', ext: 'aar')
```
Add following permissions in your AndroidManifest.xml
```xml
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
 
## Architecture

### HatcHttpRequest

This is the main class which has to be used by the developers.

### HatcHttpClient

It's a netty client which connects to the server and does the socket level communication.

### HatcHttpExecutor

It's an executor service that maintains a thread pool for performing HatcHttp operations.

### HatcHttpRequestListener

This has to be passed while executing the HatcHttpRequest to receive the status of the operation.

```
public interface HatcHttpRequestListener {
    void onComplete(final HttpResponseStatus status, final HttpHeaders headers,final String response);
    void onException(final Throwable throwable);
}
```

### HatcHttpResponseHandler

This is a netty SimpleChannelInboundHandler to read the chunks of the data and construct the entire response to push
it to the app.

## Components


# Future scope

* [ ] Add inbuilt JSON parser (for now we can use GSON or Jackson)
* [ ] Request Pooling
* [ ] Connection Pooling


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

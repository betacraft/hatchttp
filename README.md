# HatcHttp 

HatcHttp is a very simple and straightforward library for performing HTTP call inside your Android app. Architecture 
is very similar to async tasks, but HatcHttp would make your code modular and easier to maintain.
 
## Features

1. Based on Java NIO 
2. Asynchronous API calls, runs on a separate thread pool (ExecutorService)
3. Supports Post, JSONPost, Put, Delete, Get (Patch and other methods are in next release)

## What is different ?

### Based on Netty

This version of HatchHttp is based on Netty and leverage the advantage of NIO.

### Easier way to cancel all running tasks : TaskMonitor

TaskMonitor does a very simple job, but does avoid a bit of headache of killing all futures of the running tasks when
 an activity goes in background. If you don't kill running tasks, then on return of the async http call, 
 it will try to execute some UI related functionality on the components which are no more available and results in 
 exception. Task monitor is brought in for avoiding the same thing. You will have to push TaskMonitor for each of the
  async call, or Task and add TaskMonitor.cancelRunningTasks() in onPause of the activity, and you are done !! 

### Better way to avoid redundancy 

Most of the available libraries go with a basic Http library approach where you get helper methods to make different 
HTTP calls. So we end up writing some wrappers over these libraries so as to avoid redundant code. But HatcHttp 
approach is more like an generic abstract class which can return anything and entire logic is at one place. For 
example if you want to implement an API call that returns user information then 

1. Create a class that extends HatcHttpTask<User> name GetUserInfoTask
2. Implement task() method using HatcHttpCaller 
3. And parse the response returned using your favorite JSON parser

And you are ready to use this method everywhere. No extra architecture, wrappers are required.

### Different modes of use

#### Make one time synchronous http call

If you don't want to add any overhead of writing separate tasks and want to execute one time synchronous http call, 
just use HatcHttpCaller and you are done.

```java
HatcHttpCaller.getInstance().sendPostRequest(url,header,params)
```

#### Make one time asynchronous http call

And instead of synchronous call, you want to execute asynchronous call, instead of HatcHttpCaller use 
HatcHttpAsyncCaller. 

```java
HatcHttpAsyncCaller.getInstance().sendPostRequest(url,header,params)
```

 
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

```

 +-------------+      +------------+      +--------------+
 | TaskEvent   <------+            +      |              |
 |    Listener |      |  Task      +------> Task Executor|
 |             <------+            <------+              |
 +-------------+      +------------+      +----------+---+
                                                     |
                                                     |
                      +-------------+                |
                      |             |                |
                      | TaskMonitor +----------------+
                      |             |
                      +-------------+
```

## Components

### Task

Task is the core component of this library. Task is an abstract class with ```task()``` as abstract method. This 
class is templatized with the return type of the ```task()``` method. All the logic related with this task is put in 
this method by the child task. For example if you want to send a get call to google.com then you would extend Task as
 following.
 
```java
Task<String> getGoogleHomePageHtml = new Task<String>(getContext(), mTaskMonitor) {
    @Override
    protected String task() throws HatcHttpException {
        return HatcHttpCaller.sendGetRequest("http://google.com", null, null);
    }
};
getGoogleHomePageHtml.execute(new TaskEventListener<String>() {
    @Override
    public void onTaskExecutionComplete(final String response) {
        Log.i(TAG, response);

    }

    @Override
    public void onTaskExceptionEvent(final HatcHttpException exception) {
        Log.e(TAG, "Error happened while getting google homepage", exception);
   
    }
});
```

### TaskEventListener

TaskEventListener gives you status of your task asynchronously. If success then ```onTaskExecutionComplete``` callback 
will be called and in case of any exception ```onTaskExceptionEvent``` will be called with wrapped exception.

### TaskMonitor 

TaskMonitor is created keeping Activity and Application lifecycle of the Android. For background tasks (those should 
run across the activities) you should create one Application level TaskMonitor. For eg
 
```java
public class MyhApplication extends Application {

    /**
     * Background prepareRequest monitor
     */
    private static TaskMonitor mBackgroundTaskMonitor;
   
    @Override
    public void onCreate() {
       
        mBackgroundTaskMonitor = new TaskMonitor();
       
    }

  
    @Override
    public void onTerminate() {
        Log.d(TAG, "application onTerminate called");       
        if (mBackgroundTaskMonitor != null)
            mBackgroundTaskMonitor.cancelRunningTasks();
        super.onTerminate();
    }
}
```

Calling ```cancelRunningTasks``` makes sure that all the background tasks those were running are called off. Similar 
is the case of Activity level and Fragment level task. You should instantiate TaskMonitor in onCreate method and 
cancel all running task at the time of onPause/onDestroy depending upon the nature of your task.

### TaskExecutor

TaskExecutor is nothing but executor service, running all the tasks in a pool of separate threads.

### HatcHttpCaller

This includes all the basic http related methods ready to use. This executes any request at-least thrice before 
throwing the exception. Default TIMEOUT is 30 sec.

Supported methods:

1. Post
2. JSONPost
3. Put
4. Delete
5. Get

```java
public final class HatcHttpCaller {

    /**
     * Method to send a post request to the passed URL
     *
     * @param url    url to which post request has to be made
     * @param params params
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public static String sendPostRequest(final String url,
                                         final List<BasicNameValuePair> headers,
                                         List<BasicNameValuePair> params)
            throws HatcHttpException
    /**
     * Method to send a put request to the passed URL
     *
     * @param url    url to which post request has to be made
     * @param params params
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public static String sendPutRequest(final String url,
                                        final List<BasicNameValuePair> headers,
                                        List<BasicNameValuePair> params)
            throws HatcHttpException
    /**
     * Method to send a Delete request to the passed URL
     *
     * @param url    url to which post request has to be made
     * @param params params
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public static String sendDeleteRequest(final String url,
                                           final List<BasicNameValuePair> headers,
                                           List<BasicNameValuePair> params)
            throws HatcHttpException
    /**
     * Method to send a put request to the passed URL
     *
     * @param url url to which post request has to be made
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public static String sendPutRequest(final String url,
                                        List<BasicNameValuePair> headers,
                                        JSONObject json)
            throws HatcHttpException
    /**
     * Method to send a post request to the passed URL
     *
     * @param url url to which post request has to be made
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public static String sendJSONPostRequest(final String url,
                                             List<BasicNameValuePair> headers,
                                             final JSONObject json)
            throws HatcHttpException 

    /**
     * Method to send get request to the given URL with given headers and
     * parameters
     *
     * @param url     url to which get request has to be made
     * @param headers headers to be sent
     * @param params  params to be sent
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public static String sendGetRequest(final String url,
                                        final List<BasicNameValuePair> headers,
                                        List<BasicNameValuePair> params)
            throws HatcHttpException
}
```
  
# Future scope

* [ ] Add inbuilt JSON parser (for now we can use GSON or Jackson)
* [ ] Adding NIO support.
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

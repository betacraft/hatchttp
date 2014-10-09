package com.rainingclouds.hatchttp.sampleapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rainingclouds.hatchttp.HatcHttpRequest;
import com.rainingclouds.hatchttp.HatcHttpRequestListener;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;


public class MainActivity extends ActionBarActivity {


    private EditText mUrlEditText;
    private Button mGoButton;
    private TextView mResponseTextView;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        setContentView(R.layout.activity_my);
        mResponseTextView = (TextView) findViewById(R.id.response_text_view);
        HatcHttpRequest.GET("http://guarded-wave-7209.herokuapp.com")
                .addHeader("Content-Type","application/json")
                .addHeader("auth_key","801a32e3-2711-491c-51b8-c3daef868f0f")
                .addHeader("Accept","application/json").execute(new HatcHttpRequestListener() {
            @Override
            public void onComplete(HttpResponseStatus status, HttpHeaders headers, final String response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mResponseTextView.setText(response);
                    }
                });
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }
}

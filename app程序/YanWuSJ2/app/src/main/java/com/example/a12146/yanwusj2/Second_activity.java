package com.example.a12146.yanwusj2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
//import android.app.Activity;
import android.graphics.Bitmap;
//import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class Second_activity extends AppCompatActivity {
    private SendThread sendThread;


    TextView t;

    URL videoUrl;
    Bitmap bmp;
    private boolean isConnecting = false;
    private Thread mThreadClient = null;
    private Socket mSocketClient = null;
    static PrintWriter mPrintWriterClient = null;
    static BufferedReader mBufferedReaderClient	= null;
    public static String CameraIp;
    MySurfaceView2 r2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activity);

        //***************   实现视频的显示


        //获得MainActivity传递过来的intent数据
        Intent intent = getIntent();
        CameraIp= intent.getStringExtra("data");
        System.out.println("-----------------------------CameraIp2-"+CameraIp);

        //***************   实现视频的显示
        r2 = (MySurfaceView2) findViewById(R.id.MySurfaceView2) ;
         CameraIp ="http://192.168.1.1:8080/?action=stream";
        r2.GetCameraIP(CameraIp);
//        mThreadClient = new Thread(mRunnable);
//        mThreadClient.start();
        //****************

        //下面这些用来写小车的控制 和  摄像头的转向问题
        //向左转
        Button left = (Button) findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String zuo = "01020101";
                byte byt1[] = data_switch.hexString2Bytes(zuo);
                String zuo1 = null ;
                try {
                    zuo1 = data_switch.bytes2String(byt1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //   sendThread.send(zuo1);     这里直接发送是发送不了的，需要eventBUs
                EventBus.getDefault().post(new First_event(zuo1));

                //    Toast.makeText(Second_activity.this, "灯已经打开", Toast.LENGTH_SHORT).show();

            }

        });



        //向右转
        Button right = (Button) findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String you = "02020101";
                byte byt1[] = data_switch.hexString2Bytes(you);
                String you1 = null ;
                try {
                    you1 = data_switch.bytes2String(byt1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // sendThread.send(you1);
                EventBus.getDefault().post(new First_event(you1));

            }
        });

    }



    private OnClickListener button0ClickListener = new OnClickListener() {
        public void onClick(View arg0) {
            mPrintWriterClient.print("j");
            mPrintWriterClient.flush();

        }
    };

    private OnClickListener button1ClickListener = new OnClickListener() {
        public void onClick(View arg0) {
            mPrintWriterClient.print("m");
            mPrintWriterClient.flush();
        }
    };


    private OnClickListener button4ClickListener = new OnClickListener() {
        public void onClick(View arg0) {
            mPrintWriterClient.print("i");
            mPrintWriterClient.flush();
        }
    };
    private OnClickListener button5ClickListener = new OnClickListener() {
        public void onClick(View arg0) {
            mPrintWriterClient.print("l");
            mPrintWriterClient.flush();
        }
    };
    private OnClickListener button6ClickListener = new OnClickListener() {
        public void onClick(View arg0) {
            mPrintWriterClient.print("n");


            mPrintWriterClient.flush();
        }
    };

    private OnClickListener button9ClickListener = new OnClickListener() {
        public void onClick(View arg0) {
            mPrintWriterClient.print("k");
            mPrintWriterClient.flush();
        }
    };
}

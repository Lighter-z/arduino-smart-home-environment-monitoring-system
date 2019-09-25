package com.example.a12146.yanwusj2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.support.v7.app.AppCompatActivity;


import android.widget.Toast;

public class Enter_Activity extends AppCompatActivity {

    private EditText et1, et2,et3;  //把他们定义到外面那么这个文件就都可以引用
    private CheckBox rememberPass;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_);


        et1 = (EditText) findViewById(R.id.edit1);
        et2 = (EditText) findViewById(R.id.edit2);
        et3 = (EditText) findViewById(R.id.edit3);
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                et1 = (EditText) findViewById(R.id.edit1);
//                et2 = (EditText) findViewById(R.id.edit2);
                String s1 = et1.getText().toString();
                String s2 = et2.getText().toString();
                String s3 = et3.getText().toString();


                System.out.println("----------------------------------------s-----" + s1);
                System.out.println("----------------------------------------s2-----" + s2);
                System.out.println("----------------------------------------s3-----" + s3);

                Intent intent = new Intent(Enter_Activity.this,MainActivity.class);
                intent.putExtra("data", s1);
                intent.putExtra("data2", s2);
                intent.putExtra("data3", s3);
                startActivity(intent);


                //--------------下面这两句就是将输入到editTXT中保存
                String Ip = et1.getText().toString();
                save(Ip);
                String Port = et2.getText().toString();
                save2(Port);
                String Video = et3.getText().toString();
                save3(Video);
            }
        });


        Button bt_cl1 = (Button) findViewById(R.id.clear1);
        bt_cl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et2.setText("");

            }
        });

        Button bt_cl2 = (Button) findViewById(R.id.clear2);
        bt_cl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et1.setText("");

            }
        });

        Button bt_cl3 = (Button) findViewById(R.id.clear3);
        bt_cl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et3.setText("");

            }
        });

        //在这里先设置一下谁否记住IP和端口     但是这里我没有设置好  用不了
        pref = PreferenceManager.getDefaultSharedPreferences(this) ;
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        boolean isRemember = pref.getBoolean("remember_pass",false);
        System.out.println("----------------------------------------kaihguan-----" + isRemember);
        rememberPass.setChecked(true);
        System.out.println("----------------------------------------kaihguan-----" + isRemember);


        //-----------------  这里是读取文件的代码


        String inputText = load();
        String inputText2 = load2();
        String inputText3 = load3();
        System.out.println("---------t1-----"+inputText);
        System.out.println("----------t2----"+inputText2);
        System.out.println("----------t3----"+inputText3);
        Log.i("括号1",inputText);


        if (!TextUtils.isEmpty(inputText)) {
            et1.setText(inputText);
            et1.setSelection(inputText.length());

        }


        if (!TextUtils.isEmpty(inputText2)) {
            et2.setText(inputText2);
            et2.setSelection(inputText2.length());
            Toast.makeText(this, "成功记住IP和端口", Toast.LENGTH_SHORT).show();

            // rememberPass.setChecked(true);

        }

        if (!TextUtils.isEmpty(inputText3)) {
            et3.setText(inputText3);
            et3.setSelection(inputText3.length());

        }




    }


    ///-----------自定义保存代码
    //下面这些代码是将edit中的数据储存起来  然后在实现储存密码的功能
    protected void onDestroy(){

        super.onDestroy();
        String Ip = et1.getText().toString();
        save(Ip);
        String Port = et2.getText().toString();
        save2(Port);

    }

    public void save(String inputText) {

        FileOutputStream out = null;
        BufferedWriter writer = null;

        try {
            out = openFileOutput("Ip", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save2(String inputText) {

        FileOutputStream out = null;
        BufferedWriter writer = null;

        try {
            out = openFileOutput("Port", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void save3(String inputText) {

        FileOutputStream out = null;
        BufferedWriter writer = null;

        try {
            out = openFileOutput("Video", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //-------------读取文件的代码
    public String load(){

        FileInputStream in =null;
        BufferedReader reader =null;
        StringBuilder content = new StringBuilder();

        try {
            in =openFileInput("Ip");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine()) != null){
                content.append(line);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader != null ){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return  content.toString();
    }


    public String load2(){

        FileInputStream in =null;
        BufferedReader reader =null;
        StringBuilder content = new StringBuilder();

        try {
            in =openFileInput("Port");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine()) != null){
                content.append(line);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader != null ){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return  content.toString();
    }

    public String load3(){

        FileInputStream in =null;
        BufferedReader reader =null;
        StringBuilder content = new StringBuilder();

        try {
            in =openFileInput("Video");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine()) != null){
                content.append(line);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader != null ){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return  content.toString();
    }


}

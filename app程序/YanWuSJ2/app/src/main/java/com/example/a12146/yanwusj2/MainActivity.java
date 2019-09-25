package com.example.a12146.yanwusj2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;





public class MainActivity extends AppCompatActivity {

    private Context context;
    //数据项
    private List<String> data;

    private ListView mList;

    //  private TextView  wendu_1;
    private Handler mHander;

    private DrawerLayout mDrawerLayout;    //控制左端滑动页面的出现

    private   int light  = 0 ;  //用于辅助light按钮点击之后进行图片的更换

    /*接收发送定义的常量*/
    private String mIp; //= "192.168.1.1";
    private int mPort ;//= 9000;
    private SendThread sendthread;
    String receive_Msg;
    private String l;
    static PrintWriter mPrintWriterClient = null;
    static BufferedReader mBufferedReaderClient	= null;
    private Switch Switch1,Gongshui_1 ,Gongshui_2 ,Gongshui_3;
    private TextView Wendu_1 ,Shidu_1,Wendu_2,Shidu_2,Wendu_3,Shidu_3;
    private   TextView Shuiwei_1 ,Shuiwei_2,Shuiwei_3;
    private  TextView ttv1;
    /*****************************/

    private String s_video;    //用于intent向SecondActivity传递video的视频地址

    private Button shoudong_1, shoudong_2, shoudong_3;    //三个手动按钮的初始化
    int  shoudong_11 = 0 ,shoudong_22 = 0 ,shoudong_33 = 0;   //用于手动按钮和自动按钮之间的相互切换
    int   w1_0x,w2_0x,w3_0x;                            //用于在自定的时候获得湿度数据
    int sos1=0,sos2=0 ,sos3=0 ;                                //用于在点击手动按钮以后，控制发送线程开始发送
    private EditText dialog_et1,dialog_et2,dialog_et3;    //每一个对话框所对应的editText
    int        dialog_et1_int,dialog_et2_int,dialog_et3_int;  //每一个对话框里面的数据转化为int以后

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        android.support.v7.widget.Toolbar toolbar
                = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //这里要注意  在导包的时候并不是导包导的 Toolbar 而是 android.support.v7.widget.Toolbar toolbar

        mDrawerLayout = (DrawerLayout)  findViewById(R.id.drawer_layout);
        //  NavigationView navView = (NavigationView) findViewById(R.id.nav_view);  //左侧菜单栏的点击事件
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }



        //====这里就是ListView将布局引用过来
        //这个是关于ListView的问题
        data = new ArrayList<>();
        mList = (ListView) findViewById(R.id.mList);
        data.add("");
        MyAdaper adapter = new MyAdaper(data);
        //item = new ArrayList<HashMap<String, Object>>();
        mList.setAdapter(adapter);
        //---------

        //这里我要先把intent的东西获取到
        //-----
        //这里是从活动一获取到IP和端口
        final Intent intent = getIntent();
        mIp = intent.getStringExtra("data");
        String p2 = intent.getStringExtra("data2");
        s_video =  intent.getStringExtra("data3");
        mPort = Integer.parseInt(p2);
        //-------- 实现局域网的链接

        System.out.println("------------------------mIp_1-----"+mIp);
        System.out.println("------------------------p2_1-----"+p2);
        System.out.println("------------------------ s_video-----"+ s_video);


        /***************连接*****************/
        sendthread = new SendThread(mIp, mPort, mHandler);
        Thread1();
        new Thread().start();
        /**********************************/


        //在这里添加eventBUs的注册事件   在使用EventBus的时候一定要在build里面添加闭包
        EventBus.getDefault().register(MainActivity.this);

        //左侧菜单栏里面的点击事件
        Button l1 = (Button) findViewById(R.id.l_1);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this , "罗海洋" ,Toast.LENGTH_SHORT).show();

            }
        });

        Button l2 = (Button) findViewById(R.id.l_2);
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this ,Enter_Activity.class);
                startActivity(intent);
            }
        });

        Button l3 = (Button) findViewById(R.id.l_3);
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }


    ///局域网链接方法
    /*接收线程*******************************************************************************/
    /**
     * 开启socket连接线程
     */
    void Thread1(){
//        sendthread = new SendThread(mIp, mPort, mHandler);
        new Thread(sendthread).start();//创建一个新线程
    }

    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {

            //这里应该是从服务器接收到数据但是这个是用字符串接受的数据  在手机上如果出现汉字就会出线乱码
            super.handleMessage(msg);
            if (msg.what == 0x00) {

                Log.i("mr_收到的数据： ", msg.obj.toString());
                receive_Msg = msg.obj.toString();
                l = receive_Msg;
                System.out.println("--------------------------------"+l);

                //这里的i是可以传递到主函数里面去的
                // 、、并且我在这个里面是可以对碎片里面的TextView进行操作的额
                //数据是以16进制发送过来的  ，先将数据读取出来   用16进制将其读取出来的
                byte byt[] = l.getBytes();
                String str = data_switch.bytes2HexString(byt);
                System.out.println("--------------------------------转换以后是"+str);

                String source = str;
                String w1;

                if (source.length() >= 4){
                    w1 = source.substring(0, 2);
                    //使用subString方法截取的时候如果字符串的长度不够我们截取的话 就会报错
                    ttv1 = (TextView) findViewById(R.id.ttv1);
                    //将上面这些字符串转化为对应的16进制数字  由于失误这里的w1_0x 其实代表的都是湿度
                    w1_0x = (Integer.parseInt(w1, 16)); //因为下面自动的时候要货的湿度数据所以定义为全局变量
                    //   int s1_0x = (Integer.parseInt(s1, 4));
                    //这样直接放进settext里面好像不行

                    //我再把int转化为String 试试
                    String str_w1_0x = Integer.toString(w1_0x);
                    ttv1.setText(str_w1_0x);
                    System.out.println("--------------------------------" + w1_0x + "------" );

                }

            }
        }
    };


    //====

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {
//            case R.id.nav_call:
//                Toast.makeText(MainActivity.this, "点击了拨打电话", Toast.LENGTH_SHORT).show();
//                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.camera:
                //   Toast.makeText(MainActivity.this, "点击了返回", Toast.LENGTH_SHORT).show();
                //在主函数里面我已经获得到了S-video的视频地址，这里我们要将信息传递到video界面，就是第二个活动里面

                Intent intent = new Intent(MainActivity.this , Second_activity.class);
                intent.putExtra("data" ,s_video);
                startActivity(intent);
          //      Toast.makeText(MainActivity.this, "点击了拨打电话", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }


    private ArrayList<HashMap<String, Object>> item = null;

    public MainActivity() {
        // Required empty public constructor
    }

    private class MyAdaper extends BaseAdapter {

        //上下文
        private Context context;
        //数据项
        private List<String> data;

        public MyAdaper(List<String> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MainActivity.MyAdaper.ViewHolder viewHolder = null;
            if (context == null)
                context = viewGroup.getContext();
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list, null);
                viewHolder = new MainActivity.MyAdaper.ViewHolder();
                view.setTag(viewHolder);
            }
            //获取viewHolder实例
            viewHolder = (MainActivity.MyAdaper.ViewHolder) view.getTag();


            //设置事件
            // 注意这里在用到findViewByid的时候一定要加上view.findfindViewByid不然就会报错
            //**********************舵机********************************************************//

            Button button1 = (Button) view.findViewById(R.id.button1);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //上水开关----------
                    String up_open = "01020101";
                    byte byt1[] = data_switch.hexString2Bytes(up_open);

                    String up_open_0x = null;
                    try {
                        up_open_0x = data_switch.bytes2String(byt1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // TODO Auto-generated method stub
                    sendthread.send(up_open_0x);

                }

            });
            Button button2 = (Button) view.findViewById(R.id.button2);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //上水开关----------
                    String up_open = "02020101";
                    byte byt1[] = data_switch.hexString2Bytes(up_open);

                    String up_open_0x = null;
                    try {
                        up_open_0x = data_switch.bytes2String(byt1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // TODO Auto-generated method stub
                    sendthread.send(up_open_0x);




                }

            });
            //**********************控制方式********************************************************//


            Button button3 = (Button) view.findViewById(R.id.button3);
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String up_open = "00010000";
                    byte byt1[] = data_switch.hexString2Bytes(up_open);

                    String up_open_0x = null;
                    try {
                        up_open_0x = data_switch.bytes2String(byt1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // TODO Auto-generated method stub
                    sendthread.send(up_open_0x);

                }

            });





            Button button4 = (Button) view.findViewById(R.id.button4);
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String up_open = "00020101";
                    byte byt1[] = data_switch.hexString2Bytes(up_open);

                    String up_open_0x = null;
                    try {
                        up_open_0x = data_switch.bytes2String(byt1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // TODO Auto-generated method stub
                    sendthread.send(up_open_0x);
                }

            });


            // //************************************风扇******************************************//
            Gongshui_1 = (Switch) view.findViewById(R.id.gongshui_1);
            Gongshui_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override    //上水开关----------
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    String up_open = "00020201";
                    String up_close ="00020101";
                    byte byt1[] = data_switch.hexString2Bytes(up_open);
                    byte byt2[] = data_switch.hexString2Bytes(up_close);
                    String up_open_0x = null ; String  up_close_0x = null ;
                    try {
                        up_open_0x = data_switch.bytes2String(byt1);
                        up_close_0x = data_switch.bytes2String(byt2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    // TODO Auto-generated method stub
                    if (isChecked) {

                        sendthread.send(up_open_0x);

                    } else {
                        sendthread.send(up_close_0x);
                    }
                }

            });


            // *********************************蜂鸣器*********************************************//
            Gongshui_2 = (Switch) view.findViewById(R.id.gongshui_2);
            Gongshui_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override    //上水开关----------
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    String up_open = "00020102";
                    String up_close ="00020101";
                    byte byt1[] = data_switch.hexString2Bytes(up_open);
                    byte byt2[] = data_switch.hexString2Bytes(up_close);
                    String up_open_0x = null ; String  up_close_0x = null ;
                    try {
                        up_open_0x = data_switch.bytes2String(byt1);
                        up_close_0x = data_switch.bytes2String(byt2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    // TODO Auto-generated method stub
                    if (isChecked) {

                        sendthread.send(up_open_0x);

                    } else {
                        sendthread.send(up_close_0x);
                    }
                }

            });






            return view;
        }

        class ViewHolder {
        }
    } //baseAdapter



    //在这里添加EventBUs的取消注册事件


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //取消注册事件
        EventBus.getDefault().unregister(this);
    }

    //在这里添加EventBUs的消息处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)    //这里如果不添加注释就会闪退
    public void fasong(First_event first_event){

        sendthread.send(first_event.getMessage());

    }

    //要在这里写入一个返回操作，因为camera界面返回到这个界面以后，如果你再点击返回就又会返回到camera界面了
    public boolean onKeyDown (int keyCode , KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

}

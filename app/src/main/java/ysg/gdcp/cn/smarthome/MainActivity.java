package ysg.gdcp.cn.smarthome;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import ysg.gdcp.cn.smarthome.activity.AirActivity;
import ysg.gdcp.cn.smarthome.activity.CurtainActivity;
import ysg.gdcp.cn.smarthome.activity.DoorActivity;
import ysg.gdcp.cn.smarthome.activity.LampActivity;
import ysg.gdcp.cn.smarthome.utils.HomeConfig;
import ysg.gdcp.cn.smarthome.utils.NetworkUtil;

import static android.R.attr.port;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvDoor, tvAirTemperature, tvAirHumidity, tvCurtain, tvLivingLamp, tvRoomLamp;
    private  String controlIP ="192.168.0.23";//智能家居面板的IP地址
    private  String controlPort="1115";//智能家居面板的端口号
    private boolean   isOnStart ;//用来判断是否更新界面
    private BrdcstReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        connectToServer();
    }

    private void initViews() {
        findViewById(R.id.btn_door).setOnClickListener(this);
        findViewById(R.id.ibtn_air).setOnClickListener(this);
        findViewById(R.id.btn_curtain).setOnClickListener(this);
        findViewById(R.id.btn_lamp).setOnClickListener(this);
        findViewById(R.id.btn_config).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        tvDoor = (TextView) findViewById(R.id.tv_door);
        tvAirTemperature = (TextView) findViewById(R.id.tv_AirTemperature);
        tvAirHumidity = (TextView) findViewById(R.id.tv_AirHumidity);
        tvCurtain = (TextView) findViewById(R.id.tv_curtain);
        tvLivingLamp = (TextView) findViewById(R.id.tv_LivingLamp);    //客厅灯TextView
        tvRoomLamp = (TextView) findViewById(R.id.tv_RoomLamp);//房间灯TextView
    }

    /* 开启连接服务器线程, 避免卡死界面*/
    void connectToServer(){
        new Thread() {
            public void run() {
            	/*启动后台service服务, 接受网络数据*/
                MainActivity.this.startService(new Intent(MainActivity.this, ServiceSocket.class));

            	/*设置接收后台的广播信息*/

                receiver = new BrdcstReceiver();
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.intent.action.MY_RECEIVER");
                registerReceiver(receiver, filter); //注册

                /*连接服务器*/
                ////// TODO: 2017/4/5  没有服务器，所以没有连接 
//                int  port = Integer.parseInt(controlIP);
                try {
                    Socket socket = new Socket(controlPort, port);
                    /**
                     * 设置网络,输入流,输出流
                     */
                    NetworkUtil.socket = socket;
                    NetworkUtil.out = new PrintWriter(
                            new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream(),"UTF-8")), true);
                    NetworkUtil.br = new BufferedReader(
                            new InputStreamReader(socket.getInputStream(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /**************广播:接收后台的service发送的广播******************/
    private class BrdcstReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Bundle bundle = intent.getExtras();
            String stringValue=intent.getStringExtra("strRecvMsg");
            //Log.v("mainactivity", "onReceive" + stringValue);
            //   发送的测试码(windows平台) , 如果是linux平台请去掉前面四个点
            //....#SERVERSDATA#12C#35%#
            //....#SERVERSIGN#A1#B1#C0#D0#E1#F0#
            //....#SERVERSIGN#A0#B0#C0#D0#E1#F0#
            //....#SERVERSIGN#A1#B1#C1#D1#E1#F0#
            //....#SERVERSDATA#42C#41%#
            //....#SERVERSIGN#A0#B0#C0#D0#E0#F0#
            //....#SERVERSIGN#B#ON#
            if(stringValue.startsWith("#SERVERSDATA#")){// 温湿度
                String sub = stringValue.substring(stringValue.indexOf('#'), stringValue.lastIndexOf('#') + 1);
                String[] strs = sub.split("#");
                tvAirTemperature.setText("温：" + strs[2]);
                tvAirHumidity.setText("湿：" + strs[3]);
            }else{
                updateUI();
            }

        }
    }
    /*更新界面的各个图标的状态*/
    private void updateUI(){
        if(!isOnStart){
            return;
        }
        //更新门禁状态
        if(HomeConfig.RELAY_STATUE){
            tvDoor.setText("状态：开");
        }else{
            tvDoor.setText("状态：关");
        }
        //更新窗帘状态
        if(HomeConfig.CURTAIN_STATUE){
            tvCurtain.setText("状态：开");
        }else{
            tvCurtain.setText("状态：关");
        }
        //更新房间灯状态
        if(HomeConfig.ROOMLIGHT_STATUE){
            tvRoomLamp.setText("房：亮");
        }else{
            tvRoomLamp.setText("房：灭");
        }
        //更新客厅灯状态
        if(HomeConfig.CUSTOMERRIGHT_STATUE){
            tvLivingLamp.setText("厅：亮");
        }else{
            tvLivingLamp.setText("厅：灭");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        isOnStart = true;
        updateUI();  // 更新界面
        Log.e("setting", "start onResume~~~");
    }

    @Override
    protected void onStop() {
        isOnStart = false;
        Log.e("mainactivity ", "start onStop~~~");
        super.onStop();
    }

    @Override protected void onDestroy() {
        unregisterReceiver(receiver); //  注销
        this.stopService(new Intent(this, ServiceSocket.class));// 停止service
        Log.v("mainactivity ", "mainactivity onDestroy" );
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_door:
                startPage(DoorActivity.class);
                break;
            case R.id.ibtn_air:
                startPage(AirActivity.class);
                break;
            case R.id.btn_curtain:
                startPage(CurtainActivity.class);
                break;
            case R.id.btn_lamp:
                startPage(LampActivity.class);
                break;
            case R.id.btn_config:
                showAlertDilog();
                break;
            case R.id.btn_exit:
                showDilog();
                break;

        }
    }

    private void showAlertDilog() {
        View view = View.inflate(MainActivity.this, R.layout.setting_item, null);
         final EditText edIp = (EditText)view.findViewById(R.id.ed_connectionur);
         final EditText edPort = (EditText)view.findViewById(R.id.ed_controlur);
        edIp.setText(controlIP);		//初始值
        edPort.setText(controlPort);	//初始值
        new AlertDialog.Builder(MainActivity.this).setTitle("网络连接属性")
                .setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controlIP   = edIp.getText().toString();
                        controlPort = edPort.getText().toString();
                        Toast.makeText(MainActivity.this, "设置成功！", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }

    private void showDilog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("确认退出?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
    }

    private void startPage(Class clz) {
        Intent intent =new Intent(MainActivity.this,clz );
        startActivity(intent);
    }
}

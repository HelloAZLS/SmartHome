package ysg.gdcp.cn.smarthome.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.PrintWriter;
import java.net.Socket;

import ysg.gdcp.cn.smarthome.R;
import ysg.gdcp.cn.smarthome.utils.HomeConfig;
import ysg.gdcp.cn.smarthome.utils.NetworkUtil;

import static ysg.gdcp.cn.smarthome.R.id.btn_LivingLamp;
import static ysg.gdcp.cn.smarthome.R.id.btn_RoomLamp;

/**
 * Created by Administrator on 2017/4/5 09:04.
 *
 * @author ysg
 */

public class LampActivity extends AppCompatActivity implements View.OnClickListener {


    Socket socket = NetworkUtil.socket;
    private PrintWriter out = NetworkUtil.out;
    private Button btnRoomLamp;
    private Button btnLivingLamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        btnRoomLamp = (Button) findViewById(btn_RoomLamp);
        btnLivingLamp = (Button) findViewById(btn_LivingLamp);
        //更新房间灯状态
        if (HomeConfig.ROOMLIGHT_STATUE) {
            btnRoomLamp.setBackgroundResource(R.mipmap.room_lamp_on);
        } else {
            btnRoomLamp.setBackgroundResource(R.mipmap.room_lamp_off);
        }
        //更新客厅灯状态
        if (HomeConfig.CUSTOMERRIGHT_STATUE) {
            btnLivingLamp.setBackgroundResource(R.mipmap.living_lamp_on);
        } else {
            btnLivingLamp.setBackgroundResource(R.mipmap.living_lamp_off);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_LivingLamp:
                //客厅开关
                if ((socket==null) || (!(socket.isConnected()))) {
                 new AlertDialog.Builder(LampActivity.this).setTitle("网络连接").setMessage("未连接上服务器")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    return;
                }
                if (HomeConfig.CUSTOMERRIGHT_STATUE) {
                    out.println("#CTRL#C#OFF#");//假设只要发送命令，就认为已经控制成功
                    HomeConfig.CUSTOMERRIGHT_STATUE = false;
                    btnLivingLamp.setBackgroundResource(R.mipmap.living_lamp_off);
                } else {
                    System.out.println(socket);
                    out.println("#CTRL#C#ON#");
                    HomeConfig.CUSTOMERRIGHT_STATUE = true;
                    btnLivingLamp.setBackgroundResource(R.mipmap.living_lamp_on);
                }
                break;
            case R.id.btn_RoomLamp://房间开关
                if ((socket==null) || (!(socket.isConnected()))) {
                    new AlertDialog.Builder(LampActivity.this).setTitle("网络连接").setMessage("未连接上服务器")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    return;
                }
                if (HomeConfig.ROOMLIGHT_STATUE) {
                    out.println("#CTRL#B#OFF#");//假设只要发送命令，就认为已经控制成功
                    HomeConfig.ROOMLIGHT_STATUE = false;
                    btnRoomLamp.setBackgroundResource(R.mipmap.room_lamp_off);
                } else {
                    System.out.println(socket);
                    out.println("#CTRL#B#ON#");
                    HomeConfig.ROOMLIGHT_STATUE = true;
                    btnRoomLamp.setBackgroundResource(R.mipmap.room_lamp_on);
                }
                break;
        }
    }
}
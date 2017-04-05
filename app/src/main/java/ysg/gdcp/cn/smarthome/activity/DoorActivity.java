package ysg.gdcp.cn.smarthome.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.PrintWriter;
import java.net.Socket;

import ysg.gdcp.cn.smarthome.R;
import ysg.gdcp.cn.smarthome.utils.HomeConfig;
import ysg.gdcp.cn.smarthome.utils.NetworkUtil;

/**
 * Created by Administrator on 2017/4/5 09:04.
 *
 * @author ysg
 */

public class DoorActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvState;
    Socket socket = NetworkUtil.socket;
    PrintWriter out = NetworkUtil.out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_door).setOnClickListener(this);
        tvState =(TextView)findViewById(R.id.tv_DoorSate);
        if(HomeConfig.RELAY_STATUE){
            tvState.setText("开");
        }else{
            tvState.setText("关");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_door:
                /*判断网络是否连接上了,没有则提示,并返回*/
                if ((socket==null) || (!(socket.isConnected()))) {
                    new AlertDialog.Builder(DoorActivity.this).setTitle("网络连接").setMessage("未连接上服务器")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    return;
                }

		/*门禁控制的实现*/
                if (HomeConfig.RELAY_STATUE) {
                    out.println("#CTRL#A#OFF#");
                    HomeConfig.RELAY_STATUE = false;
                    tvState.setText("关");
                } else {
                    System.out.println(socket);
                    out.println("#CTRL#A#ON#");
                    HomeConfig.RELAY_STATUE = true;
                    tvState.setText("开");
                }
                break;
        }
    }
}

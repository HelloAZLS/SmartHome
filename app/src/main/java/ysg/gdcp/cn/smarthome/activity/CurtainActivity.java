package ysg.gdcp.cn.smarthome.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class CurtainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCurtainSate;
    PrintWriter out = NetworkUtil.out;
    Socket socket = NetworkUtil.socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_curtain).setOnClickListener(this);
        tvCurtainSate = (TextView) findViewById(R.id.tv_CurtainSate);
        if (HomeConfig.CURTAIN_STATUE) {
            tvCurtainSate.setText("开");
        } else {
            tvCurtainSate.setText("关");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_curtain:
                if ((socket == null) || (!(socket.isConnected()))) {
                    new AlertDialog.Builder(CurtainActivity.this).setTitle("网络连接").setMessage("未连接上服务器")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    return;
                }
                // isChecked就是按钮状态
                if (HomeConfig.CURTAIN_STATUE) {
                    out.println("#CTRL#D#OFF#");    //控制窗帘关
                    HomeConfig.CURTAIN_STATUE = false;
                    tvCurtainSate.setText("关");
                    Toast.makeText(CurtainActivity.this, "窗帘已关闭",
                            Toast.LENGTH_SHORT).show();
                } else {
                    out.println("#CTRL#D#ON#");        //控制窗帘开
                    tvCurtainSate.setText("开");
                    HomeConfig.CURTAIN_STATUE = true;
                    Toast.makeText(CurtainActivity.this, "窗帘已打开",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}

package ysg.gdcp.cn.smarthome.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class AirActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvAirSate;
    Handler handler = new Handler();
    private int TIME_OUT = 3000; //设置定时器时间
    private boolean isClickable = true;
    private ProgressDialog pd;//显示进度框
    Socket socket = NetworkUtil.socket;
    PrintWriter out = NetworkUtil.out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_air).setOnClickListener(this);
        tvAirSate = (TextView) findViewById(R.id.tv_AirSate);
        if (HomeConfig.AIRCONDITIONING_STATUE) {
            tvAirSate.setText("开");
        } else {
            tvAirSate.setText("关");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_air:
                /*判断网络是否连接上了,没有则提示,并返回*/
                if ((socket == null) || (!(socket.isConnected()))) {
                    new AlertDialog.Builder(AirActivity.this).setTitle("网络连接").setMessage("未连接上服务器")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
                    return;
                }
                // isChecked是按钮状态
                if (HomeConfig.AIRCONDITIONING_STATUE) {
                    if (!isClickable) {
                        if (pd == null) {
                            processThread();
                        }
                        return;
                    }
                    isClickable = false;
                    out.println("#CTRL#E#OFF#");
                    HomeConfig.AIRCONDITIONING_STATUE = false;
                    tvAirSate.setText("关");
                    Toast.makeText(AirActivity.this,
                            "空调已关闭", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(runnable, TIME_OUT); //每i_time_out秒执行一次runnable.
                } else {
                    if (!isClickable) {
                        if (pd == null) {
                            processThread();
                        }
                        return;
                    }
                    isClickable = false;
                    out.println("#CTRL#E#ON#");
                    HomeConfig.AIRCONDITIONING_STATUE = true;
                    tvAirSate.setText("开");
                    Toast.makeText(AirActivity.this,
                            "空调已打开", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(runnable, TIME_OUT); //每i_time_out秒执行一次runnable.
                }

                break;
        }
    }

    private void processThread() {
        //构建一个下载进度条
        pd = ProgressDialog.show(AirActivity.this, "空调还在操作中", "空调还在操作中...请稍后再试...");
        new Thread() {
            public void run() {
                //在这里执行长耗时方法
                while (!isClickable) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //执行完毕后给handler发送一个消息
                handler_pd.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler handler_pd = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pd.dismiss();//只要执行到这里就关闭对话框
            pd = null;
        }
    };
    /**
     * 定时器  响应函数      主要是下位机操作空调的时间比较慢, 所以必须加个延时, 每隔多久才能够再进行控制
     **/
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //要做的事情
            Log.v("timer", "timer isClickable");
            isClickable = true;
        }
    };
}

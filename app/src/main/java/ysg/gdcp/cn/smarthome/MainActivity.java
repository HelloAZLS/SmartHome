package ysg.gdcp.cn.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ysg.gdcp.cn.smarthome.activity.AirActivity;
import ysg.gdcp.cn.smarthome.activity.CurtainActivity;
import ysg.gdcp.cn.smarthome.activity.DoorActivity;
import ysg.gdcp.cn.smarthome.activity.LampActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvDoor, tvAirTemperature, tvAirHumidity, tvCurtain, tvLivingLamp, tvRoomLamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
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
                break;
            case R.id.btn_exit:
                break;

        }
    }

    private void startPage(Class clz) {
        Intent intent =new Intent(MainActivity.this,clz );
        startActivity(intent);
    }
}

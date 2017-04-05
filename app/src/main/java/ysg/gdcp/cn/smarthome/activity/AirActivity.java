package ysg.gdcp.cn.smarthome.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ysg.gdcp.cn.smarthome.R;

/**
 * Created by Administrator on 2017/4/5 09:04.
 *
 * @author ysg
 */

public class AirActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvAirSate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);
        initViews();
    }
    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_air).setOnClickListener(this);
        tvAirSate =(TextView)findViewById(R.id.tv_AirSate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_air:
                break;
        }
    }
}

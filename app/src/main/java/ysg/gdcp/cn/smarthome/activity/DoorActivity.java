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

public class DoorActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvState;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_door:
                break;
        }
    }
}

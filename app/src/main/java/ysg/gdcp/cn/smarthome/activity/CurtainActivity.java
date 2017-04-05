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

public class CurtainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvCurtainSate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_curtain).setOnClickListener(this);
        tvCurtainSate =(TextView)findViewById(R.id.tv_CurtainSate);
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

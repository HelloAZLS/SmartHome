package ysg.gdcp.cn.smarthome.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ysg.gdcp.cn.smarthome.MainActivity;
import ysg.gdcp.cn.smarthome.R;

/**
 * Created by Administrator on 2017/4/5 07:44.
 *
 * @author ysg
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userName, pwd;
    private SharedPreferences sp;
    private Button btnSet, btnLogin, btnCanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initData();
    }

    private void initData() {
        sp = getSharedPreferences("config", 0);
        String oldUserName = sp.getString("userName", "NO RECORE");
        String oldPwd = sp.getString("pwd", "");
        if (oldUserName != null && oldPwd != null) {
            userName.setText(oldUserName);
            pwd.setText(oldPwd);
        } else {
            userName.setText(oldUserName);
        }
    }

    private void initViews() {
        userName = (EditText) findViewById(R.id.et_user);
        pwd = (EditText) findViewById(R.id.et_pass);
        btnSet = (Button) findViewById(R.id.btn_set);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnCanel = (Button) findViewById(R.id.btn_canel);
        btnSet.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnCanel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set:
                String newName = userName.getText().toString().trim();
                String newPwd = pwd.getText().toString().trim();
                if (newName != null && newPwd != null) {
                    sp.edit()
                            .putString("userName", newName)
                            .putString("pwd", newPwd)
                            .commit();
                    Toast.makeText(LoginActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "你设置的帐号或者密码为空，请重新设置", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_login:
                String cUserName = sp.getString("userName", "");
                String cPwd = sp.getString("pwd", "");
                String inputUserName = userName.getText().toString().trim();
                String inputPwd = pwd.getText().toString().trim();
                if (cUserName.contentEquals(cUserName) && cPwd.contentEquals(inputPwd)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "你输入的帐号或密码有误，请重新输入", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_canel:
                finish();
                break;
        }
    }
}

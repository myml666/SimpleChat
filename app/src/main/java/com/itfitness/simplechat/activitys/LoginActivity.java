package com.itfitness.simplechat.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.itfitness.simplechat.R;

public class LoginActivity extends BaseActivity {
    private EditText mEtUserName,mEtPort,mEtAddress;
    private Button mBtLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username",getText(mEtUserName));
                bundle.putString("port",getText(mEtPort));
                bundle.putString("address",getText(mEtAddress));
                intent.putExtra("SocketInit",bundle);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mBtLogin = findViewById(R.id.activity_login_bt_login);
        mEtUserName = findViewById(R.id.activity_login_et_username);
        mEtAddress = findViewById(R.id.activity_login_et_address);
        mEtPort = findViewById(R.id.activity_login_et_port);
    }
}

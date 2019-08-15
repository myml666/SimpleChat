package com.itfitness.simplechat.activitys;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity {
    /**
     * 获取TextView的文字
     * @param textView
     * @return
     */
    protected String getText(TextView textView){
        return textView.getText().toString().trim();
    }
}

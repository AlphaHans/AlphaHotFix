package xyz.hans.alphahotfixdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Hans on 17/1/26.
 */

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_btn_start).setVisibility(View.GONE);
        TextView tv = (TextView) findViewById(R.id.main_tv_state);
        tv.setText("未修复");
    }
}

package com.acode.player.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.acode.player.R;

/**
 * user:yangtao
 * date:2018/5/171508
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class FuckActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test);
        TextView tv = findViewById(R.id.tv);
        String key = getIntent().getStringExtra("key");
        tv.setText(key);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("returnKey", "这是返回的数据");
                setResult(200, intent);
                FuckActivity.this.finish();
            }
        });
    }
}

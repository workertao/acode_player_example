package com.acode.player.base;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.acode.player.R;

/**
 * user:yangtao
 * date:2018/5/161409
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class TestFragment extends DBaseFragment {
    private TextView tv;

    @Override
    public int res() {
        return R.layout.fragment_test;
    }

    @Override
    public void initView() {
        tv = centerView.findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FuckActivity.class);
                intent.putExtra("key", "我是张三");
                startActivityForResult(intent, 1000);
            }
        });
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            Tab tab = (Tab) getArguments().getSerializable("tab");
            tv.setText(tab.getInfo());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        String returnKey = data.getStringExtra("returnKey");
        tv.setText(returnKey);
    }
}

package com.zfy.mantis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mmkv.MMKV;
import com.zfy.mantis.annotation.MView;
import com.zfy.mantis.library.Mantis;
import com.zfy.mantis.model.WxInfo;

/**
 * CreateAt : 2019/1/10
 * Describe :
 *
 * @author chendong
 */

public class MainActivity extends AppCompatActivity {


    @MView(R.id.tv)
    TextView tv;

    @MView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        MainActivity_MView.bindView(this);

        Mantis.initKv(MMKV.defaultMMKV(), new JsonAdapterImpl());
        try {
            UserInfoDao.putAge(10000);
            tv.setText("new text " + UserInfoDao.getAge(0));
            btn.setOnClickListener(v -> {
                UserInfoDao.putName("new name " + System.currentTimeMillis());
                tv.setText("click text " + UserInfoDao.getName(""));
                UserInfoDao.putWxInfo(new WxInfo(1000, "nickName"));
                Toast.makeText(this, UserInfoDao.getWxInfo().toString(), Toast.LENGTH_LONG).show();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

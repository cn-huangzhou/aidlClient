package com.fundrive.navaidlclient.modules;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import com.fundrive.navaidlclient.Constant;
import com.fundrive.navaidlclient.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetMuteActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.switch_mode)
    Switch switchMode;
    private String key;
    private int cmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mute);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        String str = intent.getStringExtra("title");
        title.setText(str);
        cmd = intent.getIntExtra(Constant.CMD_KEY,0);
    }

    @OnClick(R.id.switch_mode)
    public void onViewClicked() {
        if (switchMode.isChecked()){
            makeJson(true);
        } else {
            makeJson(false);
        }

    }
    private void makeJson(boolean on) {
        JSONObject cmdJson = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key, on);
            cmdJson.put(Constant.CMD_KEY,cmd);
            cmdJson.put(Constant.JSON_KEY, jsonObject);

            String message = cmdJson.toString();
            sendMessage(message);
            Log.d(TAG, "makeJson: "+message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

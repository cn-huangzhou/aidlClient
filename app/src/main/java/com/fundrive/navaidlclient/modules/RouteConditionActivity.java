package com.fundrive.navaidlclient.modules;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fundrive.navaidlclient.Constant;
import com.fundrive.navaidlclient.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouteConditionActivity extends BaseActivity {

    @BindView(R.id.sp_type)
    Spinner spType;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.sp_route_rule)
    Spinner spRouteRule;
    @BindView(R.id.sp_switch)
    Spinner spSwitch;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //避让类型取值范围
    private int[] types = {1, 1<<1 , 1<<8, 1<<9, 1<<10, 1<<11,1<<12,1<<13,1<<14, 1<<24,1<<25, 1<<26};
    //开启关闭
    private boolean enabled = false;

    private int type;
    private int rule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_condition);
        ButterKnife.bind(this);
        tvTitle.setText("算路规则");
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = types[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spSwitch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (0 == position) {
                    enabled = false;
                } else {
                    enabled = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick({R.id.btn_commit, R.id.btn_return})
    public void onViewClicked(View view) {
        switch (view.getId())
        {
            case R.id.btn_commit:
                makeJson(type,enabled);
                break;
            case R.id.btn_return:
                finish();
                break;
        }
    }

    private void makeJson(int type, boolean on) {
        JSONObject cmdJson = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("iaRoutePreference", type);
            jsonObject.put("enable", on);

            cmdJson.put(Constant.CMD_KEY, Constant.IA_CMD_SET_ROUTE_CONDITION);
            cmdJson.put(Constant.JSON_KEY, jsonObject);

            String message = cmdJson.toString();
            sendMessage(message);
            Log.d(TAG, "makeJson: " + message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

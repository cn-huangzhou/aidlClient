package com.fundrive.navaidlclient.modules;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fundrive.navaidlclient.Constant;
import com.fundrive.navaidlclient.R;
import com.fundrive.navaidlclient.Resource;
import com.fundrive.navaidlclient.adapter.AnimationListAdapter;
import com.fundrive.navaidlclient.bean.PageInfoBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListAnimationActivity extends BaseActivity {

    @BindView(R.id.lv_item)
    ListView lvItem;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private ArrayList<Integer> arrayList = new ArrayList<>();
    private AnimationListAdapter adapter;

    private String message;

    private PageInfoBean.Lists protocolData;
    private int lists_index;
    private JSONObject obj_sendJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_animation);
        ButterKnife.bind(this);
        tvTitle.setText("List动画");
        arrayList.add(1);
        adapter = new AnimationListAdapter(arrayList);
        lvItem.setAdapter(adapter);

        Intent intent = getIntent();
        protocolData = (PageInfoBean.Lists) intent.getSerializableExtra("PageInfoBean");
        lists_index = intent.getIntExtra("lists_index",0);
        if (protocolData.getSendJson()!=null && !protocolData.getSendJson().isEmpty()){
            try {
                obj_sendJson = new JSONObject(protocolData.getSendJson());
                JSONArray arr_iaListAnimaType = obj_sendJson.getJSONArray("iaListAnimaType");
                arrayList.clear();
                for (int i=0;i<arr_iaListAnimaType.length();i++){
                    arrayList.add(arr_iaListAnimaType.getInt(i));
                }
                adapter.setAnimations(arrayList);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            JSONObject obj_sendJson_m = new JSONObject();
            JSONArray arr_iaListAnimaType = new JSONArray();
            for (Integer i : arrayList) {
                arr_iaListAnimaType.put(i);
            }
            obj_sendJson_m.put("iaListAnimaType", arr_iaListAnimaType);
            protocolData.setSendJson(obj_sendJson_m.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Resource.pageInfoBean.getLists().remove(lists_index);
        Resource.pageInfoBean.getLists().add(lists_index, protocolData);
    }

    @OnClick({R.id.btn_add, R.id.btn_sub, R.id.btn_commit, R.id.btn_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                arrayList.add(1);
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_sub:
                if (arrayList.size() > 0) {
                    arrayList.remove(arrayList.size() - 1);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_commit:
                makeJson();
                showSendDialog(message);
                break;
            case R.id.btn_return:
                finish();
                break;
        }
    }

    //组装json
    private void makeJson() {
        JSONObject cmdJson = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i = 0; i < arrayList.size(); i++) {
            array.put(arrayList.get(i));
        }
        try {
            jsonObject.put("iaListAnimaType", array);
            cmdJson.put(Constant.CMD_KEY, Constant.IA_CMD_CURRENT_UI_LIST_ANIMATION);
            cmdJson.put(Constant.JSON_KEY, jsonObject);

            message = cmdJson.toString();
            sendMessage(message);
            Log.d(TAG, "makeJson: " + message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.fundrive.navaidlclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.fundrive.andrive.INavRemoteRequest;
import com.fundrive.navaidlclient.bean.CmdBean;
import com.fundrive.navaidlclient.bean.Observer;
import com.fundrive.navaidlclient.bean.PageInfoBean;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangjianghua on 2017/8/31.
 */

public class Resource {
    public static PageInfoBean pageInfoBean;
    private static final int PORT = 8888;
    private static final int CONNECTED_SEVER_STATE = 1; //连接服务器状态
    private static final int RECIVEDATA_STATE = 2;      //服务端接收数据
    private static final int CLIENTRECIVEDATA_STATE = 3;//客户端接收数据
    private static EditText meditText = null;
    public static String data = null;
    private static boolean StartServer = false;
    private static boolean StartClient = false;
    private static ShareConfiguration mShareConfiguration = null;
    public static int device_model = 0;
    private static String serverIpaddress = null;


    public static CmdBean[] beans = {
            new CmdBean(Constant.IA_CMD_SET_AUTHORIZE_SERIAL_NUMBER, "授权序列号",
                    Constant.IA_CMD_SET_AUTHORIZE_SERIAL_NUMBER_CONTENT),
            new CmdBean(Constant.IA_CMD_SET_MAP_DISPLAY_MODE, "地图显示模式：白天/黑夜",
                    Constant.IA_CMD_SET_MAP_DISPLAY_MODE_DAY),
            new CmdBean(Constant.IA_CMD_SET_SOUND_VOLUME, "音量",
                    Constant.IA_CMD_SET_SOUND_VOLUME_SIZE),
            new CmdBean(Constant.IA_CMD_SET_MUTE_SWITCH, "静音开/关",
                    Constant.IA_CMD_SET_MUTE_SWITCH_ON),
            new CmdBean(Constant.IA_CMD_SET_ROUTE_CONDITION, "算路规则",
                    Constant.IA_CMD_SET_ROUTE_CONDITION_CONTENT),
            new CmdBean(Constant.IA_CMD_SET_TIME_INFOMATION, "时间信息",
                    Constant.IA_CMD_SET_TIME_INFOMATION_24),
            new CmdBean(Constant.IA_CMD_SET_TYPE_WRITING, "输入法",
                    ""),
            new CmdBean(Constant.IA_CMD_SET_LANGUAGE, "语言",
                    Constant.IA_CMD_SET_LANGUAGE_CHINASE),
            new CmdBean(Constant.IA_CMD_SET_MULTIMEDIA_INFOMATION, "多媒体信息",
                    Constant.IA_CMD_SET_MULTIMEDIA_INFOMATION_FM),
            new CmdBean(Constant.IA_CMD_SET_TBT_SWITCH_STATUS, "交互目标接收引导信息的开关状态",
                    Constant.IA_CMD_SET_TBT_SWITCH_STATUS_ON),
            new CmdBean(Constant.IA_CMD_SET_GUIDENCE_SOUND_TYPE, "导航播报语音类型",
                    Constant.IA_CMD_SET_GUIDENCE_SOUND_TYPE_),
            new CmdBean(Constant.IA_CMD_UPDATE_IATARGET_WRITING_STATUS, "交互目标的写状态",
                    Constant.IA_CMD_UPDATE_IATARGET_WRITING_STATUS_YES),
            new CmdBean(Constant.IA_CMD_UPDATE_IATARGET_ROUTE_STATUS, "交互目标的路径状态",
                    Constant.IA_CMD_UPDATE_IATARGET_ROUTE_STATUS_YES),
            new CmdBean(Constant.IA_CMD_SAVE_DATA, "保存NavApp数据", ""),
            new CmdBean(Constant.IA_CMD_SHOW_OR_HIDE, "显示/隐藏NavApp",
                    Constant.IA_CMD_HIDE),
            new CmdBean(Constant.IA_CMD_ZOOM_MAP, "缩小/放大地图",
                    Constant.IA_CMD_ZOOM_MAP_IN_1),
            new CmdBean(Constant.IA_CMD_GET_NAVI_INFO, "获取NavApp的导航引导信息",
                    Constant.IA_CMD_GET_NAVI_INFO_CONTENT),
            new CmdBean(Constant.IA_CMD_REPEAT_NAVI_SOUND, "重播当前导航语音提示语句",
                    Constant.IA_CMD_REPEAT_NAVI_SOUND_CONTENT),
            new CmdBean(Constant.IA_CMD_MOVE_UI, "画面迁移",
                    Constant.IA_CMD_MOVE_UI_CONTENT),
            new CmdBean(Constant.IA_CMD_CHANGE_NAVI_UI_VISUAL_ATTRIBUTES, "改变NavApp的UI视觉属性(如 导航背景)",
                    Constant.IA_CMD_CHANGE_NAVI_UI_VISUAL_ATTRIBUTES_CONTENT),
            new CmdBean(Constant.IA_CMD_SHOW_DANAMIC_INFOMATION, "显示动态信息(如动态停车场)",
                    Constant.IA_CMD_SHOW_DANAMIC_INFOMATION_CONTENT),
            new CmdBean(Constant.IA_CMD_SART_OR_STOP_NAVI_GUIDE, "启动/停止导航",
                    Constant.IA_CMD_SART_OR_STOP_NAVI_GUIDE_START),
            new CmdBean(Constant.IA_CMD_GET_NAVI_STATUAS, "获取NavApp的当前状态",
                    Constant.IA_CMD_GET_NAVI_STATUAS_CONTENT),
            new CmdBean(Constant.IA_CMD_SHOW_TARGET_SOUND_VOLUME, "显示交互目标的音量",
                    Constant.IA_CMD_SHOW_TARGET_SOUND_VOLUME_CONTENT),
            new CmdBean(Constant.IA_CMD_DELET_NAVI_ROUTE, "删除NavApp的当前路线",
                    ""),
            new CmdBean(Constant.IA_CMD_GET_GPS_INFO, "获取GPS信息",
                    ""),
            new CmdBean(Constant.IA_CMD_SET_MAP_VIEW_MODE, "切换地图视图模式",
                    Constant.IA_CMD_SET_MAP_VIEW_MODE_1SCREEN_3D),
            new CmdBean(Constant.IA_CMD_ROUTE_BY_CONDITION, "条件算路",
                    Constant.IA_CMD_ROUTE_BY_CONDITION_1),
            new CmdBean(Constant.IA_CMD_SET_DISPLAY_SCREEN_FOR_NAVAPP, "设置NavApp画面所的在显示屏幕",
                    Constant.IA_CMD_SET_DISPLAY_SCREEN_FOR_NAVAPP_SHOW2),
            new CmdBean(Constant.IA_CMD_CURRENT_UI_LIST_ANIMATION, "List动画操作",
                    Constant.IA_CMD_CURRENT_UI_LIST_ANIMATION_TWO),
            new CmdBean(Constant.IA_CMD_FAVORITE_GUIDANCE, "收藏点导航",
                    Constant.IA_CMD_FAVORITE_GUIDANCE_HOME),
            new CmdBean(Constant.IA_CMD_LOCATE_THE_CAR, "定位自车",
                    ""),
            new CmdBean(Constant.IA_CMD_NAVAPP_CONTROL_MULTIMEDIA, "执行多媒体控制",
                    Constant.IA_CMD_NAVAPP_CONTROL_MULTIMEDIA_OPEN_BLUETOOTH),
            new CmdBean(Constant.IA_CMD_BROWSE_HELP_INFORMATION, "查看帮助信息",
                    Constant.IA_CMD_BROWSE_HELP_INFORMATION_USE_NAV),
            new CmdBean(Constant.IA_CMD_START_GUIDING_WITH_ROUTE, "指定线路导航",
                    Constant.IA_CMD_START_GUIDING_WITH_ROUTE_CONTENT),
            new CmdBean(Constant.IA_CMD_CHANGE_NAVAPP_WINDOW_MODE, "切换NavApp窗口模式",
                    Constant.IA_CMD_CHANGE_NAVAPP_WINDOW_MODE_CONTENT),
            new CmdBean(Constant.IA_CMD_GET_NAVAPP_WINDOW_MODE, "获取NavApp窗口模式",
                    ""),
            new CmdBean(Constant.IA_CMD_GET_FAVORITE_POINT, "获取收藏点",
                    Constant.IA_CMD_GET_FAVORITE_POINT_CONTENT),
            new CmdBean(Constant.IA_CMD_UPDATE_FAVORITE_POINT, "更新收藏点",
                    Constant.IA_CMD_UPDATE_FAVORITE_POINT_CONTENT),
            new CmdBean(Constant.IA_CMD_UPDATE_FAVORITE_POINT_AND_GUESS, "猜测家和公司",
                    Constant.IA_CMD_UPDATE_FAVORITE_POINT_CONTENT),
            new CmdBean(Constant.IA_CMD_UPDATE_FAVORITE_POINT_AND_NAVI, "更新收藏并导航",
                    Constant.IA_CMD_UPDATE_FAVORITE_POINT_CONTENT),
            new CmdBean(Constant.IA_CMD_UPDATE_KEYBOARD_INPUT, "键盘输入字符串",
                    Constant.IA_CMD_UPDATE_KEYBOARD_INPUT_CONTENT),
            new CmdBean(Constant.IA_CMD_SEARCH_POI_BY_CONDITION, "条件搜索POI",
                    Constant.IA_CMD_SEARCH_POI_BY_CONDITION_CONTETN),
            new CmdBean(Constant.IA_CMD_SELECT_POI_SEARCH_CENTER, "选择POI条件搜索时的搜索中心点",
                    Constant.IA_CMD_SELECT_POI_SEARCH_CENTER_CONTENT),
            new CmdBean(Constant.IA_CMD_GET_POI_PAGE_DATA, "获取给定页面的POI页数据",
                    Constant.IA_CMD_GET_POI_PAGE_DATA_CONTENT),
            new CmdBean(Constant.IA_CMD_GET_SPECIFIC_POINT_INFO, "获取指定经纬度位置详细信息",
                    Constant.IA_CMD_GET_SPECIFIC_POINT_INFO_CONTENT),
            new CmdBean(Constant.IA_CMD_ENABLE_SPEEDLIMIT_WARNING, "开启/关闭超速提醒",
                    Constant.IA_CMD_ENABLE_SPEEDLIMIT_WARNING_CONTENT),
            new CmdBean(Constant.IA_CMD_ENABLE_CAMERA_WARNING, "开启/关闭电子警察",
                    Constant.IA_CMD_ENABLE_CAMERA_WARNING_CONTENT),
            new CmdBean(Constant.IA_CMD_ENABLE_TMC, "开启/关闭实时路况",
                    Constant.IA_CMD_ENABLE_TMC_CONTENT),
            new CmdBean(Constant.IA_CMD_SET_SOUND_CRUISE, "巡航播报开关",
                    Constant.IA_CMD_ENABLE_TMC_CONTENT),
            new CmdBean(Constant.IA_CMD_BATTERY_LOW, "低电量通知", ""),
            new CmdBean(Constant.IA_CMD_SET_ROUTE_VIEW_MODE, "路径浏览模式",
                    Constant.IA_CMD_SET_ROUTE_VIEW_MODE_CONTENT),
            new CmdBean(Constant.IA_CMD_SET_BOARDCAST_MODE, "导航播报频率",
                    Constant.IA_CMD_SET_BOARDCAST_MODE_CONSTANT),
            new CmdBean(Constant.IA_CMD_GET_REMAINING_ROUTEINFO, "获取剩余路线信息",
                    Constant.IA_CMD_GET_REMAINING_ROUTEINFO_CONTANT),
            new CmdBean(Constant.IA_CMD_ENABLE_AVOID_RESTRICTION_ROADS, "开启/关闭避开限行道路功能",
                    Constant.IA_CMD_ENABLE_AVOID_RESTRICTION_ROADS_CONTENT),
            new CmdBean(Constant.IA_CMD_TMC_BROADCAST, "TMC查询", ""),
            new CmdBean(0, "自定义消息", ""),
            new CmdBean(Constant.IA_CMD_GO_PARKING_INFO,"停车无忧","")


    };

    public static void showInfo(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static Context ctx;
    static INavRemoteRequest navService;
    static boolean bind;

    public static void init(Context context) {
        ctx = context;
        mShareConfiguration = new ShareConfiguration(ctx, ShareConfiguration.SETTING_INFOS);
        initWlan(device_model = mShareConfiguration.getDeviceModel());
    }

    public static void changeWlan(){
        initWlan(0);
    }

    public static void initRequest(INavRemoteRequest mNavService, boolean mBind) {
        navService = mNavService;
        bind = mBind;
    }

    private static MyHandler myHandler = new MyHandler();

    public static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CONNECTED_SEVER_STATE:
                    switch (msg.arg1) {
                        case 0:
                            Resource.showInfo(ctx, "未找到服务器.");
                            break;
                        case 1:
                            Resource.showInfo(ctx, "服务端连接失败.");
                            break;
                        case 2:
                            Resource.showInfo(ctx, "发送消息成功");
                            break;
                        case 3:
                            Resource.showInfo(ctx, "发送消息失败");
                            break;
                        default:
                            break;
                    }
                    break;
                case RECIVEDATA_STATE:
                    String data = msg.obj.toString();
                    Resource.showInfo(ctx, data);
                    Resource.callAidlFun(data);
                    break;
                case CLIENTRECIVEDATA_STATE://客户端收到返回数据
                    notifyObserver(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }

    public static List<Observer> list_observer = new ArrayList<>();

    public static void registerObserver(Observer o) {
        list_observer.add(o);
    }

    public static void removeObserver(Observer o) {
        if (list_observer.contains(o)){
            list_observer.remove(o);
        }
    }

    private static void notifyObserver(String data) {
        try {
            JSONObject object = new JSONObject(data);
            int cmd = object.getInt(Constant.CMD_KEY);
            String message = object.getString(Constant.JSON_KEY);
            for (int i = 0; i < list_observer.size(); i++) {
                list_observer.get(i).update(cmd,message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static DatagramSocket client_dsocket = null;
    public static void udpClient(final String data) {
        if (serverIpaddress == null || serverIpaddress == "") {
            showInfo(ctx, "IP地址不正确.");
            return;
        }
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                InetAddress addr = null;
                ByteArrayOutputStream byteArrayOutputStream = null;
                DataOutputStream dataOutputStream = null;
                try {
                    addr = InetAddress.getByName(serverIpaddress);
                } catch (UnknownHostException e) {
                    msg.what = CONNECTED_SEVER_STATE;
                    msg.arg1 = 0;
                    myHandler.sendMessage(msg);
                    e.printStackTrace();
                }
                try {
                    if (client_dsocket == null){
                        client_dsocket = new DatagramSocket();
                    }
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                    dataOutputStream.writeUTF(data);
                    dataOutputStream.close();

                } catch (SocketException e) {
                    e.printStackTrace();
                    msg.what = CONNECTED_SEVER_STATE;
                    msg.arg1 = 1;
                    myHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] wrapData = byteArrayOutputStream.toByteArray();
                DatagramPacket dpacket = new DatagramPacket(wrapData, wrapData.length, addr, PORT);
                try {
                    client_dsocket.send(dpacket);
                    msg.what = CONNECTED_SEVER_STATE;
                    msg.arg1 = 2;
                    myHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = CONNECTED_SEVER_STATE;
                    msg.arg1 = 3;
                    myHandler.sendMessage(msg);
                }
//                client_dsocket.close();
            }
        }.start();
    }

    public static void callAidlFun(String cmd) {
        if (navService == null) {
            Log.e("wjh", "navService == null");
            return;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(cmd);
            int intType = jsonObject.getInt(Constant.CMD_KEY);
            String strJson = jsonObject.getString(Constant.JSON_KEY);
            if (bind) {
                navService.request(intType, strJson);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void initWlan(int device_model) {
        switch (device_model) {
            case ShareConfiguration.MODEL_NONE_KOWN:
                ModelChooseDialog();
                break;
            case ShareConfiguration.MODEL_CLIENT:
                getIpaddress();
                break;
            case ShareConfiguration.MODEL_SERVER:
                StartServer = true;
                startUdpServer();
                break;
            default:
                break;
        }
    }

    /**
     * 客户端从服务端接收返回数据
     */
    private static void receiveFromServer(){
        new Thread(){
            @Override
            public void run() {
                byte[] buf = new byte[1024 * 1024];
                while (StartClient) {
                    DatagramPacket dp = new DatagramPacket(buf, 1024 * 1024);
                    try {
                        if (client_dsocket!=null){
                            client_dsocket.receive(dp);
                            String data;
                            try (DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(dp.getData()))) {
                                data = dataInputStream.readUTF();
                            }
                            Log.e("zzz","client receive:"+data);
                            Message msg = new Message();
                            msg.what = CLIENTRECIVEDATA_STATE;
                            msg.obj = data;
                            myHandler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 服务端发返回数据给客户端
     * 此方法不要再主线程调用
     * @param data
     */
    static void sendFromServerToClient(String data){
        try {
            DatagramSocket socket = new DatagramSocket();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF(data);
            dataOutputStream.close();
            byte[] wrapData = byteArrayOutputStream.toByteArray();
            DatagramPacket packet = null;
            if (client_address != null && client_port != 0){
                packet = new DatagramPacket(wrapData,wrapData.length,client_address,client_port);
                socket.send(packet);
            } else {
                Log.e("zzz","客户端未知");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void ModelChooseDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder ModelChooseDialog =
                new AlertDialog.Builder(ctx);
        ModelChooseDialog.setTitle("软件模式选择");
        ModelChooseDialog.setMessage("选择客户端还是服务端模式?");
        ModelChooseDialog.setPositiveButton("服务端",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mShareConfiguration.setDeviceModel(device_model = ShareConfiguration.MODEL_SERVER);
                        StartServer = true;
                        startUdpServer();
                    }
                });
        ModelChooseDialog.setNegativeButton("客户端",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mShareConfiguration.setDeviceModel(device_model = ShareConfiguration.MODEL_CLIENT);
                        getIpaddress();
                    }
                });
        // 显示
        ModelChooseDialog.show();
    }

    private static InetAddress client_address;
    private static int client_port;

    private static void startUdpServer() {
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                DatagramSocket ds = null;
                byte[] buf = new byte[1024 * 2];
                try {
                    ds = new DatagramSocket(PORT);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                while (StartServer) {
                    DatagramPacket dp = new DatagramPacket(buf, 1024 * 2);
                    try {
                        if (ds == null) {
                            return;
                        }
                        ds.receive(dp);
                        client_address = dp.getAddress();
                        client_port = dp.getPort();
                        String data;
                        try (DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(dp.getData()))) {
                            data = dataInputStream.readUTF();
                        }
                        if (data != null || data != "") {
                            Message msg = new Message();
                            msg.what = RECIVEDATA_STATE;
                            msg.obj = data;
                            myHandler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private static void getIpaddress() {
        meditText = new EditText(ctx);
        SharedPreferences sp = ctx.getSharedPreferences("data", Activity.MODE_PRIVATE);
        meditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        meditText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        String ip = sp.getString("ip", "");
        meditText.setText(ip);
        meditText.setSelection(ip.length());
        final AlertDialog dialog = new AlertDialog.Builder(ctx).setTitle("输入IP地址")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(meditText)
                .setCancelable(false)
                .setPositiveButton("确定", null)
                .show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = meditText.getText().toString().trim();
                if (input.equals("")) {
                    Toast.makeText(ctx, "IP地址不能为空！", Toast.LENGTH_LONG).show();
                    return;
                } else if (!isIP(input)) {
                    Toast.makeText(ctx, "IP地址不正确！", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    serverIpaddress = input;
                    SharedPreferences sp = ctx.getSharedPreferences("data", Activity.MODE_PRIVATE);
                    sp.edit().putString("ip", input).commit();
                }
                StartClient = true;
                dialog.dismiss();
                receiveFromServer();
            }
        });
    }

    private static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();

        return ipAddress;
    }

}

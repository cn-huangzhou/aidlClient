package com.fundrive.navaidlclient.bean;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class PageInfoBean {
    private String name;
    private String type;
    private List<Item> item;

    public class Item{
        private List<SecondKey> secondKey;
        private List<ThirdKey> thirdKey;
        private List<Page> page;
    }

    public static List<PageInfoBean> getPageInfoBeanList(String strJson){
        List<PageInfoBean> list = new ArrayList<>();
        Gson gson = new Gson();//创建Gson对象
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = jsonParser.parse(strJson).getAsJsonArray();//获取JsonArray对象
        for (JsonElement bean : jsonElements) {
            PageInfoBean bean1 = gson.fromJson(bean, PageInfoBean.class);//解析
            list.add(bean1);
        }
        return list;
    }

    public class SecondKey{
        private String nama;

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        @Override
        public String toString() {
            return "SecondKey{" +
                    "nama='" + nama + '\'' +
                    '}';
        }
    }

    public class ThirdKey{
        private String nama;

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        @Override
        public String toString() {
            return "ThirdKey{" +
                    "nama='" + nama + '\'' +
                    '}';
        }
    }

    public class Page{
        private String name;//"白天模式",
        private String value;//"2",
        private String key;//"mode",
        private String type;//"singlebutton",
        private String floor;//"2",
        private String grandParentKey;//"aaa",
        private String parentKey;//"iaAudio",
        private String titleName;//"---",
        private String lineNum;//"1"

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public String getGrandParentKey() {
            return grandParentKey;
        }

        public void setGrandParentKey(String grandParentKey) {
            this.grandParentKey = grandParentKey;
        }

        public String getParentKey() {
            return parentKey;
        }

        public void setParentKey(String parentKey) {
            this.parentKey = parentKey;
        }

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

        public String getLineNum() {
            return lineNum;
        }

        public void setLineNum(String lineNum) {
            this.lineNum = lineNum;
        }

        @Override
        public String toString() {
            return "Page{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    ", key='" + key + '\'' +
                    ", type='" + type + '\'' +
                    ", floor='" + floor + '\'' +
                    ", grandParentKey='" + grandParentKey + '\'' +
                    ", parentKey='" + parentKey + '\'' +
                    ", titleName='" + titleName + '\'' +
                    ", lineNum='" + lineNum + '\'' +
                    '}';
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "PageInfoBean{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", item=" + item +
                '}';
    }
}

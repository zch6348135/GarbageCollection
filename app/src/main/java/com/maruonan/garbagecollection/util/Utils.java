package com.maruonan.garbagecollection.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.maruonan.garbagecollection.bean.TextBean;
import com.maruonan.garbagecollection.bean.TypeBean;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author yf_zch
 * @version 1.0.0
 */
public class Utils {
    public String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public ArrayList<TextBean> parseData(String result) {//Gson 解析
        ArrayList<TextBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                TextBean entity = gson.fromJson(data.optJSONObject(i).toString(), TextBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
}

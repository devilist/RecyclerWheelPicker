/*
 * Copyright  2017  zengp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devilist.recyclerwheelpicker.parser;

import android.content.Context;
import android.support.annotation.RawRes;

import com.devilist.recyclerwheelpicker.bean.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zengp on 2017/11/27.
 */

public class DataParser {

    public static List<Data> parserData(Context context, @RawRes int res, boolean all) {
        InputStream in = context.getResources().openRawResource(res);
        String array = read(in, "utf-8");
        return parse(array, all);
    }

    private static List<Data> parse(String array, boolean all) {
        List<Data> result = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(array);
            return parse(jsonArray, all);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<Data> parse(JSONArray jsonArray, boolean all) {
        List<Data> result = new ArrayList<>();
        if (null == jsonArray)
            return result;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            Data data = new Data();
            data.id = jsonObject.optInt("id");
            if (!all && data.id == -1) continue;
            data.data = jsonObject.optString("data");
            JSONArray subArray = jsonObject.optJSONArray("items");
            data.items = parse(subArray, all);
            result.add(data);
        }
        return result;
    }

    private static String read(InputStream is, String encode) {
        if (is != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, encode));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                return sb.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "[]";
    }
}

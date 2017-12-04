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

package com.devilist.recyclerwheelpicker;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.devilist.recyclerwheelpicker.bean.Data;
import com.devilist.recyclerwheelpicker.dialog.WheelPicker;
import com.devilist.recyclerwheelpicker.widget.IDecoration;
import com.devilist.recyclerwheelpicker.widget.RecyclerWheelPicker;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zengp on 2017/11/30.
 */

@SuppressLint("ValidFragment")
public class PasswordPicker extends WheelPicker {

    protected PasswordBuilder passwordBuilder;
    protected List<RecyclerWheelPicker> pickerList = new ArrayList<>();
    protected String[] pickResult;
    protected int length, itemW, itemH;

    protected PasswordPicker(PasswordBuilder builder) {
        super(builder);
        builder.dataRelated = false;
        passwordBuilder = builder;
        passwordBuilder.gravity = Gravity.CENTER;
    }

    public static PasswordBuilder instance() {
        return new PasswordBuilder(PasswordPicker.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return createLayout();
    }

    protected View createLayout() {

        length = passwordBuilder.length;
        if (length <= 0) length = 6;
        itemW = passwordBuilder.itemW;
        itemH = passwordBuilder.itemH;
        if (itemW <= 0) itemW = 150;
        if (itemW > width / length) itemW = (int) (0.9f * width / length);
        if (itemH <= 0) itemH = 150;

        IDecoration decoration = new IDecoration() {
            @Override
            public void drawDecoration(RecyclerWheelPicker picker, Canvas c, Rect decorationRect, Paint decorationPaint) {
                decorationPaint.setColor(Color.BLACK);
                decorationPaint.setStrokeWidth(2);
                decorationRect.set(10, 10, picker.getWidth() - 10, picker.getHeight() - 10);
                c.drawRect(decorationRect, decorationPaint);
            }
        };

        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                height / 3);
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.WHITE);

        for (int i = 0; i < length; i++) {
            RecyclerWheelPicker picker = new RecyclerWheelPicker(getContext());
            linearLayout.addView(picker);
            picker.getLayoutParams().width = itemW;
            picker.getLayoutParams().height = itemH;
            picker.setDecorationSize(itemH);
            picker.setDecoration(decoration);
            picker.setTextSize(28 * getResources().getDisplayMetrics().scaledDensity);
            pickerList.add(picker);
        }
        return linearLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(width, height / 3);
    }

    @Override
    protected void initView() {
        for (int i = 0; i < length; i++) {
            pickerList.get(i).setOnWheelScrollListener(this);
        }
    }

    @Override
    protected List<Data> parseData() {
        List<Data> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Data data = new Data();
            data.id = i;
            data.data = i + "";
            datas.add(data);
        }
        if (!passwordBuilder.onlyNumber) {
            for (char i = 'a'; i <= 'z'; i++) {
                Data data = new Data();
                data.id = Integer.valueOf(i);
                data.data = i + "";
                datas.add(data);
            }
            for (char i = 'A'; i <= 'Z'; i++) {
                Data data = new Data();
                data.id = Integer.valueOf(i);
                data.data = i + "";
                datas.add(data);
            }
        }
        return datas;
    }

    @Override
    protected void inflateData(List<Data> datas) {
        pickResult = new String[length];
        for (int i = 0; i < length; i++) {
            pickerList.get(i).setData(datas);
            pickResult[i] = "0";
        }
    }

    @Override
    public void onWheelScrollChanged(RecyclerWheelPicker wheelPicker, boolean isScrolling, int position, Data data) {
        super.onWheelScrollChanged(wheelPicker, isScrolling, position, data);
        for (int i = 0; i < length; i++) {
            if (pickerList.get(i) == wheelPicker) {
                if (!isScrolling && null != data) pickResult[i] = data.data;
                else pickResult[i] = "";
            }
        }
    }

    @Override
    protected void pickerClose() {
        if (builder.pickerListener != null) {
            builder.pickerListener.onPickResult(tag, pickResult);
        }
        for (int i = 0; i < length; i++) {
            pickerList.get(i).release();
        }
    }

    public static class PasswordBuilder extends Builder {

        public int length;
        public boolean onlyNumber = true;
        public int itemW = 150, itemH = 150;

        public PasswordBuilder(Class clazz) {
            super(clazz);
        }

        public PasswordBuilder length(int length) {
            this.length = length;
            return this;
        }

        public PasswordBuilder onlyNumber(boolean only) {
            this.onlyNumber = only;
            return this;
        }

        public PasswordBuilder itemSize(int itemW, int itemH) {
            this.itemW = itemW;
            this.itemH = itemH;
            return this;
        }

        @Override
        public WheelPicker build() {
            return new PasswordPicker(this);
        }
    }
}

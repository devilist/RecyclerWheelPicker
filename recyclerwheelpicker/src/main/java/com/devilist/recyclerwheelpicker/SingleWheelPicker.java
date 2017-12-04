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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.devilist.recyclerwheelpicker.bean.Data;
import com.devilist.recyclerwheelpicker.dialog.WheelPicker;
import com.devilist.recyclerwheelpicker.parser.DataParser;
import com.devilist.recyclerwheelpicker.widget.RecyclerWheelPicker;

import java.util.List;


/**
 * Created by zengp on 2017/11/26.
 */

@SuppressLint("ValidFragment")
public class SingleWheelPicker extends WheelPicker {

    private TextView tv_cancel, tv_ok;
    protected RecyclerWheelPicker rv_picker1;
    protected String pickData1 = "";
    protected String unit1 = "";

    protected SingleWheelPicker(Builder builder) {
        super(builder);
    }

    public static Builder instance() {
        return new Builder<SingleWheelPicker>(SingleWheelPicker.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (builder.gravity == Gravity.BOTTOM) window.setGravity(Gravity.BOTTOM);
        View contentView = inflater.inflate(R.layout.dialog_wheel_picker_single, container, false);
        return contentView;
    }

    @Override
    protected void initView() {
        tv_ok = (TextView) getView().findViewById(R.id.tv_ok);
        tv_cancel = (TextView) getView().findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        rv_picker1 = (RecyclerWheelPicker) getView().findViewById(R.id.rv_picker1);
        rv_picker1.setOnWheelScrollListener(this);
    }

    @Override
    protected List<Data> parseData() {
        // parse data
        return DataParser.parserData(getContext(), builder.resInt, builder.isAll);
    }

    @Override
    protected void inflateData(List<Data> datas) {
        // units
        String[] units = builder.units;
        if (null != units) {
            if (units.length > 0) unit1 = units[0];
        }
        // default position. find by defPosition firstly, then defValues
        int defP1 = 0;
        if (datas.size() > 0) {
            int[] defPosition = builder.defPosition;
            if (null != defPosition) {
                if (defPosition.length > 0) defP1 = defPosition[0];
                defP1 = Math.min(Math.max(0, defP1), datas.size() - 1);
            } else {
                String[] defValues = builder.defValues;
                if (datas.size() > 0 && null != defValues) {
                    if (defValues.length > 0) {
                        for (int i = 0; i < datas.size(); i++) {
                            if (defValues[0].equals(datas.get(i).data)) {
                                defP1 = i;
                                break;
                            }
                        }
                    }
                }
            }
        }
        rv_picker1.setUnit(datas.get(defP1).id == -1 ? "" : unit1);
        rv_picker1.setData(datas);
        rv_picker1.scrollTargetPositionToCenter(defP1);
    }

    @Override
    public void onWheelScrollChanged(RecyclerWheelPicker wheelPicker, boolean isScrolling, int position, Data data) {
        super.onWheelScrollChanged(wheelPicker, isScrolling, position, data);
        if (!isScrolling && null != data) {
            pickData1 = data.data;
            rv_picker1.setUnit(data.id == -1 ? "" : unit1);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_ok) {
            if (!rv_picker1.isScrolling() && null != builder.pickerListener) {
                builder.pickerListener.onPickResult(tag, pickData1);
                dismiss();
            }
        } else {
            dismiss();
        }
    }

    @Override
    protected void pickerClose() {
        super.pickerClose();
        rv_picker1.release();
    }
}

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
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zengp on 2017/11/26.
 */
@SuppressLint("ValidFragment")
public class DoubleWheelPicker extends WheelPicker {

    private TextView tv_cancel, tv_ok;
    protected RecyclerWheelPicker rv_picker1, rv_picker2;
    protected String pickData1 = "", pickData2 = "";
    protected String unit1 = "", unit2 = "";
    protected List<Data> dataList2;

    protected DoubleWheelPicker(Builder builder) {
        super(builder);
    }

    public static Builder instance() {
        return new Builder<DoubleWheelPicker>(DoubleWheelPicker.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (builder.gravity == Gravity.BOTTOM) window.setGravity(Gravity.BOTTOM);
        View contentView = inflater.inflate(R.layout.dialog_wheel_picker_double, container, false);
        return contentView;
    }

    @Override
    protected void initView() {
        tv_ok = (TextView) getView().findViewById(R.id.tv_ok);
        tv_cancel = (TextView) getView().findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        rv_picker1 = (RecyclerWheelPicker) getView().findViewById(R.id.rv_picker1);
        rv_picker2 = (RecyclerWheelPicker) getView().findViewById(R.id.rv_picker2);
        rv_picker1.setOnWheelScrollListener(this);
        rv_picker2.setOnWheelScrollListener(this);
    }

    @Override
    protected List<Data> parseData() {
        // parse data
        return DataParser.parserData(getContext(), builder.resInt, builder.isAll);
    }

    @Override
    protected void inflateData(List<Data> datas) {
        // setview
        List<Data> datas2 = new ArrayList<>();
        // units
        String[] units = builder.units;
        if (null != units) {
            if (units.length > 0) unit1 = units[0];
            if (units.length > 1) unit2 = units[1];
        }
        //  parse default position. find by defValues firstly, then defPosition
        int defP1 = 0, defP2 = 0;
        if (datas.size() > 0) {
            // parser by defValues
            if (null != builder.defValues) {
                String defV1 = "", defV2 = "";
                if (builder.defValues.length > 0 && null != builder.defValues[0])
                    defV1 = builder.defValues[0];
                if (builder.defValues.length > 1 && null != builder.defValues[1])
                    defV2 = builder.defValues[1];
                if (!builder.dataRelated) {
                    // data is not related among each other
                    if (datas.size() > 1) datas2 = datas.get(1).items;
                    datas = datas.get(0).items;
                    if (null != datas && datas.size() > 0 && !TextUtils.isEmpty(defV1)) {
                        for (int i = 0; i < datas.size(); i++) {
                            if (defV1.equals(datas.get(i).data)) {
                                defP1 = i;
                                pickData1 = defV1;
                                break;
                            }
                        }
                    } else defP1 = 0;
                    if (null != datas2 && datas2.size() > 0 && !TextUtils.isEmpty(defV2)) {
                        for (int i = 0; i < datas2.size(); i++) {
                            if (defV2.equals(datas2.get(i).data)) {
                                defP2 = i;
                                pickData2 = defV2;
                                break;
                            }
                        }
                    } else defP2 = 0;
                } else {
                    if (!TextUtils.isEmpty(defV1)) {
                        for (int i = 0; i < datas.size(); i++) {
                            if (defV1.equals(datas.get(i).data)) {
                                defP1 = i;
                                pickData1 = datas.get(defP1).data;
                                break;
                            }
                        }
                    }
                    datas2 = datas.get(defP1).items;
                    if (null != datas2 && datas2.size() > 0 && !TextUtils.isEmpty(defV2)) {
                        for (int i = 0; i < datas2.size(); i++) {
                            if (defV2.equals(datas2.get(i).data)) {
                                defP2 = i;
                                pickData2 = datas2.get(defP2).data;
                                break;
                            }
                        }
                    }
                }
            } else {
                // parser by defPosition
                if (null != builder.defPosition) {
                    if (builder.defPosition.length > 0) defP1 = builder.defPosition[0];
                    if (builder.defPosition.length > 1) defP2 = builder.defPosition[1];
                }
                if (!builder.dataRelated) {
                    // data is not related among each other
                    if (datas.size() > 1) datas2 = datas.get(1).items;
                    datas = datas.get(0).items;
                    if (null != datas && datas.size() > 0) {
                        defP1 = Math.min(Math.max(0, defP1), datas.size() - 1);
                        pickData1 = datas.get(defP1).data;
                    } else defP1 = 0;
                    if (null != datas2 && datas2.size() > 0) {
                        defP2 = Math.min(Math.max(0, defP2), datas2.size() - 1);
                        pickData2 = datas2.get(defP2).data;
                    } else defP2 = 0;
                } else {
                    // data is related
                    defP1 = Math.min(Math.max(0, defP1), datas.size() - 1);
                    pickData1 = datas.get(defP1).data;
                    datas2 = datas.get(defP1).items;
                    if (null != datas2 && datas2.size() > 0) {
                        defP2 = Math.min(Math.max(0, defP2), datas2.size() - 1);
                        pickData2 = datas2.get(defP2).data;
                    }
                }
            }
        }

        rv_picker1.setUnit(datas.get(defP1).id == -1 ? "" : unit1);
        rv_picker2.setUnit(datas.get(defP1).id == -1 ? "" : unit2);
        if (builder.dataRelated) {
            rv_picker2.setData(datas2);
            rv_picker1.setData(datas);
        } else {
            dataList2 = datas2;
            rv_picker2.setData(datas.get(defP1).id == -1 ? null : datas2);
            rv_picker1.setData(datas);
        }
        rv_picker2.scrollTargetPositionToCenter(defP2);
        rv_picker1.scrollTargetPositionToCenter(defP1);
    }

    @Override
    public void onWheelScrollChanged(RecyclerWheelPicker wheelPicker, boolean isScrolling, int position, Data data) {
        super.onWheelScrollChanged(wheelPicker, isScrolling, position, data);
        if (!rv_picker1.isInitFinish() || !rv_picker2.isInitFinish()) return;
        if (wheelPicker == rv_picker1) {
            if (!isScrolling && null != data) {
                pickData1 = data.data;
                rv_picker1.setUnit(data.id == -1 ? "" : unit1);
                rv_picker2.setUnit(data.id == -1 ? "" : unit2);
                if (builder.dataRelated) {
                    rv_picker2.setData(data.items);
                } else {
                    rv_picker2.setData(data.id == -1 ? null : dataList2);
                }
            } else {
                pickData1 = "";
            }
        } else if (wheelPicker == rv_picker2) {
            if (!isScrolling && null != data) {
                pickData2 = data.data;
            } else {
                pickData2 = "";
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_ok) {
            if (!rv_picker1.isScrolling() && !rv_picker2.isScrolling() && null != builder.pickerListener) {
                builder.pickerListener.onPickResult(tag, pickData1, pickData2);
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
        rv_picker2.release();
    }
}

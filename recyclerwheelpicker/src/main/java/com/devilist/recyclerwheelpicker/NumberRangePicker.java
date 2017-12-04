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
import android.view.View;
import android.view.ViewGroup;

import com.devilist.recyclerwheelpicker.bean.Data;
import com.devilist.recyclerwheelpicker.dialog.WheelPicker;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zengp on 2017/11/30.
 */

@SuppressLint("ValidFragment")
public class NumberRangePicker extends DoubleWheelPicker {

    private NumberBuilder numberBuilder;

    protected NumberRangePicker(NumberBuilder builder) {
        super(builder);
        builder.dataRelated = true;
        numberBuilder = builder;
    }

    public static NumberBuilder instance() {
        return new NumberBuilder(DoubleWheelPicker.class);
    }

    @Override
    protected void initView() {
        super.initView();
        if (numberBuilder.single) {
            rv_picker2.setOnWheelScrollListener(null);
            rv_picker2.setVisibility(View.GONE);
            rv_picker1.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            int padding = (int) (getResources().getDisplayMetrics().density * 100);
            rv_picker1.setPadding(padding, 0, padding, 0);
        }
    }

    @Override
    protected List<Data> parseData() {
        List<Data> datas = new ArrayList<>();
        int[] ranges = numberBuilder.range;
        int from = 0, to = 100;
        if (null != ranges) {
            if (ranges.length > 0) from = ranges[0];
            if (ranges.length > 1) to = ranges[1];
        }
        if (builder.isAll) {
            Data data = new Data();
            data.data = "不限";
            data.id = -1;
            data.items = new ArrayList<>();
            datas.add(data);
        }
        if (from <= to) {
            for (int i = from; i <= to; i++) {
                Data d1 = new Data();
                d1.data = i + "";
                d1.id = i == -1 ? 0 : i;
                d1.items = new ArrayList<>();
                if (!numberBuilder.single && i <= to) {
                    for (int j = i; j <= to; j++) {
                        Data d2 = new Data();
                        d2.data = j + "";
                        d2.id = j;
                        d1.items.add(d2);
                    }
                }
                datas.add(d1);
            }
        } else {
            for (int i = from; i >= to; i--) {
                Data d1 = new Data();
                d1.data = i + "";
                d1.id = i == -1 ? -1 : i;
                d1.items = new ArrayList<>();
                if (!numberBuilder.single && i >= to) {
                    for (int j = i; j >= to; j--) {
                        Data d2 = new Data();
                        d2.data = j + "";
                        d2.id = j;
                        d1.items.add(d2);
                    }
                }
                datas.add(d1);
            }
        }
        return datas;
    }

    public static class NumberBuilder extends Builder {

        public int[] range;
        public boolean single = false;

        public NumberBuilder(Class clazz) {
            super(clazz);
        }

        public NumberBuilder range(int... range) {
            this.range = range;
            return this;
        }

        public NumberBuilder single(boolean single) {
            this.single = single;
            return this;
        }

        @Override
        public WheelPicker build() {
            return new NumberRangePicker(this);
        }
    }

}

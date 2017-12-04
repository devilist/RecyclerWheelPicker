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

import com.devilist.recyclerwheelpicker.bean.Data;
import com.devilist.recyclerwheelpicker.dialog.WheelPicker;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zengp on 2017/12/1.
 */

@SuppressLint("ValidFragment")
public class TimeWheelPicker extends TripleWheelPicker {

    protected TimeBuilder timeBuilder;

    protected TimeWheelPicker(TimeBuilder builder) {
        super(builder);
        builder.dataRelated = false;
        timeBuilder = builder;
    }

    public static TimeBuilder instance() {
        return new TimeBuilder(TimeWheelPicker.class);
    }

    @Override
    protected void initView() {
        super.initView();
        if (timeBuilder.noHour || timeBuilder.noSecond) {
            rv_picker3.setOnWheelScrollListener(null);
            rv_picker3.setVisibility(View.GONE);
            int padding = (int) (getResources().getDisplayMetrics().density * 50);
            rv_picker1.setPadding(padding, 0, 0, 0);
            rv_picker2.setPadding(0, 0, padding, 0);
        }
    }

    @Override
    protected List<Data> parseData() {
        builder.setDataRelated(false);
        List<Data> datas = new ArrayList<>();
        Data data1 = new Data();
        Data data2 = new Data();
        data1.items = new ArrayList<>();
        data2.items = new ArrayList<>();
        if (builder.isAll) {
            Data data = new Data();
            data.id = -1;
            data.data = "不限";
            data1.items.add(data);
        }
        if (timeBuilder.noHour) {
            for (int i = 0; i < 60; i++) {
                Data data = new Data();
                data.id = i;
                data.data = (i < 10 ? "0" : "") + i;
                data1.items.add(data);
                data2.items.add(data);
            }
            datas.add(data1);
            datas.add(data2);
        } else if (timeBuilder.noSecond) {
            for (int i = 0; i < 60; i++) {
                Data data = new Data();
                data.id = i;
                data.data = (i < 10 ? "0" : "") + i;
                if (i < 25)
                    data1.items.add(data);
                data2.items.add(data);
            }
            datas.add(data1);
            datas.add(data2);
        } else {
            for (int i = 0; i < 60; i++) {
                Data data = new Data();
                data.id = i;
                data.data = (i < 10 ? "0" : "") + i;
                if (i < 25)
                    data1.items.add(data);
                data2.items.add(data);
            }
            datas.add(data1);
            datas.add(data2);
            datas.add(data2);
        }
        return datas;
    }


    public static class TimeBuilder extends Builder {

        public boolean noHour = false;
        public boolean noSecond = false;

        public TimeBuilder(Class clazz) {
            super(clazz);
        }

        public TimeBuilder setNoHour(boolean noHour) {
            this.noHour = noHour;
            return this;
        }

        public TimeBuilder setNoSecond(boolean noSecond) {
            this.noSecond = noSecond;
            return this;
        }

        @Override
        public WheelPicker build() {
            return new TimeWheelPicker(this);
        }
    }
}

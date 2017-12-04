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

import com.devilist.recyclerwheelpicker.bean.Data;
import com.devilist.recyclerwheelpicker.dialog.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by zengp on 2017/11/29.
 */

@SuppressLint("ValidFragment")
public class DateWheelPicker extends TripleWheelPicker {

    private DateBuilder dateBuilder;
    private Calendar calendar;

    protected DateWheelPicker(DateBuilder builder) {
        super(builder);
        builder.dataRelated = true;
        dateBuilder = builder;
    }

    public static DateBuilder instance() {
        return new DateBuilder(DateWheelPicker.class);
    }

    @Override
    protected List<Data> parseData() {
        // create data
        List<Data> datas = new ArrayList<>();
        calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        int[] limit = dateBuilder.limit;
        int maxYear = currentYear, maxMonth = currentMonth, maxDay = currentDay;
        if (null != limit) {
            if (limit.length > 0) maxYear = limit[0];
            if (limit.length > 1) maxMonth = limit[1];
            maxMonth = Math.max(1, Math.min(maxMonth, 12));
            if (limit.length > 2) maxDay = limit[2];
            maxDay = Math.max(1, Math.min(maxDay, 31));
        }

        if (builder.isAll) {
            Data data = new Data();
            data.id = -1;
            data.data = "不限";
            data.items = new ArrayList<>();
            data.items.add(new Data());
            datas.add(data);
        }
        int startYear = maxYear;
        int endYear = currentYear - 100;
        if (startYear < endYear) startYear = endYear;
        for (int year = startYear; year >= endYear; year--) {
            Data data_year = new Data();
            data_year.data = year + "";
            data_year.id = year;
            // month
            List<Data> months = new ArrayList<>();
            int startMonth = year == maxYear ? maxMonth : 12;
            for (int month = startMonth; month >= 1; month--) {
                Data data_month = new Data();
                data_month.data = (month < 10 ? "0" : "") + month;
                data_month.id = month;
                // day
                List<Data> days = new ArrayList<>();
                calendar.set(year, month, 0);
                int start_day = calendar.get(Calendar.DAY_OF_MONTH);
                if (year == maxYear && month == maxMonth)
                    start_day = Math.min(start_day, maxDay);
                for (int day = start_day; day >= 1; day--) {
                    Data data_day = new Data();
                    data_day.data = (day < 10 ? "0" : "") + day;
                    data_day.id = day;
                    days.add(data_day);
                }
                data_month.items = days;
                months.add(data_month);
            }
            data_year.items = months;
            datas.add(data_year);
        }
        return datas;
    }

    public static class DateBuilder extends Builder {

        public int[] limit;

        public DateBuilder(Class clazz) {
            super(clazz);
        }

        public DateBuilder limit(int... limit) {
            this.limit = limit;
            return this;
        }

        @Override
        public WheelPicker build() {
            return new DateWheelPicker(this);
        }
    }
}

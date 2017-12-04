package com.zp.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.devilist.recyclerwheelpicker.DateWheelPicker;
import com.devilist.recyclerwheelpicker.DoubleWheelPicker;
import com.devilist.recyclerwheelpicker.NumberRangePicker;
import com.devilist.recyclerwheelpicker.PasswordPicker;
import com.devilist.recyclerwheelpicker.SingleWheelPicker;
import com.devilist.recyclerwheelpicker.TimeWheelPicker;
import com.devilist.recyclerwheelpicker.TripleWheelPicker;
import com.devilist.recyclerwheelpicker.dialog.WheelPicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        WheelPicker.OnPickerListener {

    private TextView tv_single, tv_double, tv_number_range_single, tv_number_range_double,
            tv_triple, tv_triple_date, tv_triple_time_3, tv_triple_time_2, tv_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_picker);

        tv_single = findViewById(R.id.tv_single);
        tv_double = findViewById(R.id.tv_double);
        tv_number_range_single = findViewById(R.id.tv_number_range_single);
        tv_number_range_double = findViewById(R.id.tv_number_range_double);
        tv_triple = findViewById(R.id.tv_triple);
        tv_triple_date = findViewById(R.id.tv_triple_date);
        tv_triple_time_3 = findViewById(R.id.tv_triple_time_3);
        tv_triple_time_2 = findViewById(R.id.tv_triple_time_2);
        tv_pw = findViewById(R.id.tv_pw);

        tv_single.setOnClickListener(this);
        tv_double.setOnClickListener(this);
        tv_number_range_single.setOnClickListener(this);
        tv_number_range_double.setOnClickListener(this);
        tv_triple.setOnClickListener(this);
        tv_triple_date.setOnClickListener(this);
        tv_triple_time_3.setOnClickListener(this);
        tv_triple_time_2.setOnClickListener(this);
        tv_pw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_single:
                SingleWheelPicker.instance()
                        .setGravity(Gravity.BOTTOM)
                        .setDefPosition(0)
                        .setDefValues("兔")
                        .setUnits("属相")
                        .showAllItem(true)
                        .setResource(R.raw.picker_zodiac)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "single");
                break;
            case R.id.tv_double:
                DoubleWheelPicker.instance()
                        .setGravity(Gravity.BOTTOM)
                        .setDefPosition(10, 9)
                        .setDefValues("浙江", "杭州")
                        .setUnits("", "")
                        .showAllItem(true)
                        .setResource(R.raw.picker_location)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "double");
                break;
            case R.id.tv_number_range_single:
                NumberRangePicker.instance()
                        .range(30, 200)
                        .single(true)
                        .showAllItem(true)
                        .setUnits("kg")
                        .setGravity(Gravity.BOTTOM)
                        .setPickerListener(this).build().
                        show(getSupportFragmentManager(), "number_range_single");
                break;
            case R.id.tv_number_range_double:
                NumberRangePicker.instance()
                        .range(100, 200)
                        .showAllItem(true)
                        .setUnits("cm", "cm")
                        .setGravity(Gravity.BOTTOM)
                        .setPickerListener(this).build().
                        show(getSupportFragmentManager(), "number_range_double");
                break;
            case R.id.tv_triple:
                TripleWheelPicker.instance()
                        .setDefPosition(19, 8, 5)
                        .setGravity(Gravity.BOTTOM)
                        .setResource(R.raw.picker_location_3)
                        .showAllItem(true)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "triple_city");
                break;
            case R.id.tv_triple_date:
                DateWheelPicker.instance()
                        .limit(2017)
                        .showAllItem(true)
                        .setUnits("年", "月", "日")
                        .setDefPosition(4, 8, 13)
                        .setGravity(Gravity.BOTTOM)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "triple_date");
                break;
            case R.id.tv_triple_time_3:
                TimeWheelPicker.instance()
                        .setDataRelated(false)
                        .showAllItem(true)
                        .setUnits("时", "分", "秒")
                        .setDefPosition(13, 30, 30)
                        .setGravity(Gravity.BOTTOM)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "triple_time_3");
                break;
            case R.id.tv_triple_time_2:
                TimeWheelPicker.instance()
                        .setNoSecond(true)
                        .showAllItem(true)
                        .setUnits("时", "分")
                        .setDefPosition(13, 30)
                        .setGravity(Gravity.BOTTOM)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "triple_time_2");
                break;
            case R.id.tv_pw:
                PasswordPicker.instance()
                        .itemSize(160, 180)
                        .length(6)
                        .onlyNumber(false)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "password");
                break;
        }
    }

    @Override
    public void onPickResult(String tag, String... result) {
        switch (tag) {
            case "single":
            case "number_range_single":
                Log.d("RecyclerWheelPicker", "single result " + result[0]);
                Toast.makeText(this,result[0], Toast.LENGTH_SHORT).show();
                break;
            case "double":
            case "number_range_double":
            case "triple_time_2":
                Log.d("RecyclerWheelPicker", "double city result " + result[0] + "-" + result[1]);
                Toast.makeText(this, result[0] + "-" + result[1], Toast.LENGTH_SHORT).show();
                break;
            case "triple_city":
            case "triple_date":
            case "triple_time_3":
                Log.d("RecyclerWheelPicker", "triple result " + result[0] + "-" + result[1] + "-" + result[2]);
                Toast.makeText(this, result[0] + "-" + result[1] + "-" + result[2], Toast.LENGTH_SHORT).show();
                break;
            case "password":
                String s = " ";
                for (int i = 0; i < result.length; i++)
                    s += result[i] + " ";
                Log.d("RecyclerWheelPicker", "password result " + s);
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}


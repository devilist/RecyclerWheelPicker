# RecyclerWheelPicker

这是一个用Recyclerview实现的模仿ios风格的滚轮选择器。

![image](https://github.com/devilist/RecyclerWheelPicker/raw/master/images/image.gif)

提供了一下几种选择器，可以直接调用：

```
  SingleWheelPicker   // 单滚轮
  DoubleWheelPicker   // 双滚轮
  TripleWheelPicker   // 三滚轮
  DateWheelPicker     // 日期选择
  TimeWheelPicker     // 时间选择
  NumberRangePicker   // 数字区间选择
  PasswordPicker      // 密码盘
```

# 调用方法
调用方法很很简单，采用链式调用：

单滚轮调用
```
SingleWheelPicker.instance()
                        .setGravity(Gravity.BOTTOM)
                        .setDefPosition(0)
                        .setDefValues("兔")
                        .setUnits("属相")
                        .showAllItem(true)
                        .setResource(R.raw.picker_zodiac)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "single");
```
双滚轮调用
```
DoubleWheelPicker.instance()
                        .setGravity(Gravity.BOTTOM)
                        .setDefPosition(10, 9)
                        .setDefValues("浙江", "杭州")
                        .setUnits("", "")
                        .showAllItem(true)
                        .setResource(R.raw.picker_location)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "double");

 NumberRangePicker.instance()
                        .range(30, 200)
                        .single(true)
                        .showAllItem(true)
                        .setUnits("kg")
                        .setGravity(Gravity.BOTTOM)
                        .setPickerListener(this).build().
                        show(getSupportFragmentManager(), "number_range_single");

```

三滚轮调用
```
TripleWheelPicker.instance()
                        .setDefPosition(19, 8, 5)
                        .setGravity(Gravity.BOTTOM)
                        .setResource(R.raw.picker_location_3)
                        .showAllItem(true)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "triple_city");

DateWheelPicker.instance()
                        .limit(2017)
                        .showAllItem(true)
                        .setUnits("年", "月", "日")
                        .setDefPosition(4, 8, 13)
                        .setGravity(Gravity.BOTTOM)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "triple_date");

 TimeWheelPicker.instance()
                        .setDataRelated(false)
                        .showAllItem(true)
                        .setUnits("时", "分", "秒")
                        .setDefPosition(13, 30, 30)
                        .setGravity(Gravity.BOTTOM)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "triple_time_3");

```

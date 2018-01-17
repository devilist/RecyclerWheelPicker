# RecyclerWheelPicker

这是一个用Recyclerview实现的模仿ios风格的滚轮选择器。

![image](https://github.com/devilist/RecyclerWheelPicker/raw/master/images/image.gif)

# 目前提供的可直接调用的控件
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

# 链式调用的方法说明
上面列出的XXPicker全部继承自WheelPicker,因此它们既有从WheelPicker继承来的基本方法，也有各自扩展的方法，下面分别说明：

1 所有XXPicker都有的方法：
```
setResource(@RawRes int resInt)：   // 数据源。 数据采用json格式，放在raw文件夹里。数据格式注意事项稍后会讲到
setGravity(int gravity)：           // 滚轮显示的位置。默认底部
showAllItem(boolean all)：          // 是否显示“不限”选项（稍后数据格式部分会将到）
setUnits(String... units)：         // 滚轮单位
setDefValues(String... values)：    // 滚轮启动时默认显示的值
setDefPosition(int... defPosition)： // 滚轮启动时默认显示的位置
setDataRelated(boolean dataRelated)： // 各滚轮数据之间是否关联，默认关联（稍后数据格式部分会讲到这个方法）

```

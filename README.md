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

```
XXXWheelPicker.instance()
                        .setGravity(Gravity.BOTTOM)
                        .setDefPosition(10, 9)
                        .setDefValues("浙江", "杭州")
                        .setUnits("", "")
                        .showAllItem(true)
                        .setResource(R.raw.picker_location)
                        .setPickerListener(this).build()
                        .show(getSupportFragmentManager(), "double");
```
XXXWheelPicker为上述控件中的任何一个。

# 链式调用的方法说明
上面列出的XXPicker全部继承自WheelPicker,因此它们既有从WheelPicker继承来的基本方法，也有各自扩展的方法，下面分别说明：

1 所有XXPicker都有的方法：
```
setResource(@RawRes int resInt)：    数据源。 数据采用json格式，放在raw文件夹里。数据格式注意事项稍后会讲到
setGravity(int gravity)：            滚轮显示的位置。默认底部
showAllItem(boolean all)：           是否显示“不限”选项（稍后数据格式部分会将到）
setUnits(String... units)：          滚轮单位
setDefValues(String... values)：     滚轮启动时默认显示的值
setDefPosition(int... defPosition)：  滚轮启动时默认显示的位置
setDataRelated(boolean dataRelated)：  各滚轮数据之间是否关联，默认关联（稍后数据格式部分会讲到这个方法）
setPickerListener(OnPickerListener pickerListener)： 选择结果的监听

```

2.其他属于子类的链式调用方法

数字区间滚轮：NumberRangePicker
```
single(boolean single)：  显示单滚轮还是双滚轮
range(int... range)：     数字区间 例如 range(5,100)

```

日期滚轮：DateWheelPicker

```
limit(int... limit)  最大日期限制
```
该方法用于限制最大的日期，不传默认为今天的年月日。
如果需要只显示到某年某月某日，例如年龄必须大于18岁，可这样处理：
```
 Calendar calendar = Calendar.getInstance();
 int maxYear = calendar.get(Calendar.YEAR) - 18;
 int currentMonth = calendar.get(Calendar.MONTH) + 1;
 int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
 DateWheelPicker.instance()
                 .limit(maxYear, currentMonth, currentDay)
                 .setUnits("年", "月", "日")
```

时间滚轮：TimeWheelPicker

```
setNoHour(boolean noHour):    只显示 分秒
setNoSecond(boolean noSecond) 只显示 时分
```

密码滚轮：PasswordPicker
```
length(int length)                  密码长度
onlyNumber(boolean only)            密码是否只能是数字
itemSize(int itemW, int itemH)      每个滚轮的尺寸
```

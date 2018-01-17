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

# 滚轮选择结果的监听
```
interface OnPickerListener {

 void onPickResult(String tag, String... result);

 }

 参数tag用于滚轮的唯一标记，数组result为选择的结果

```

# 数据格式
加载到滚轮中的数据采用json格式，例如，一个三级滚轮的json数据必须满足如下格式：
```
[
  { "data": "广东",
    "items": [
      { "data": "全省", "id" : -1 },
      { "data": "广州",
        "items": [ { "data": "全市", "id" : -1 } , { "data": "越秀区" } ],
        "id": 0 },
      { "data": "深圳",
        "items": [ { "data": "全市", "id" : -1 } , { "data": "福田区" }  ],
        "id": 0 },
      { "data": "珠海",
        "items": [ { "data": "全市", "id" : -1 } , { "data": "香洲区" }  ],
        "id": 0 }
    ],
    "id": 1 },
    {.................},
    {.................},
    {.................},
    {.................}
]
```
其中 data为滚轮选择的value值，item为次级滚轮的内容。各级数据按照相同的格式依次嵌套。

注意：
  自定义数据id时不要占用-1这个值，-1用来标记数据中的“全部”item，当你的业务需求需要让用户
选择“全部”(或者叫“不限”，“全省”，“全市”诸如此类)时，请将该item的id设为-1；
showAllItem(boolean all)这个方法可以用来控制是否显示“全部”item。
所以当业务需求没有“全部”item，而此方法被误调用(false)后,会将数据中所有id=-1的项目过滤掉。

  setDataRelated(boolean dataRelated) 这个方法用于设置数据是否按照上面严格的嵌套结构解析。
默认是true。当你想按照自己的数据结构解析数据时，应将此方法置为false，不然将会引发数据解析错误。
如何按照自定义的json结构解析将在接下来讲到。

# 实现自定义的滚轮选择

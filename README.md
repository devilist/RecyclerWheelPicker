# RecyclerWheelPicker

这是一个用Recyclerview实现的模仿ios风格的滚轮选择器。

![image](https://github.com/devilist/RecyclerWheelPicker/raw/master/images/image.gif)

# 目前提供的可直接调用的控件
提供了以下几种选择器，可以直接调用：

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
该方法用于限制最大的日期，不传则默认为今天的年月日。
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
加载到滚轮中的数据采用json格式，并放在raw资源文件夹里。例如，一个三级滚轮的json数据必须满足如下格式：
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
        "id": 0 } ],
    "id": 1 },
    {.................},
    {.................},
    {.................},
    {.................}
]
```
数据解析类
```
public class Data {
    public int id;
    public String data;
    public List<Data> items;
}
```
其中 data为滚轮选择的value值，items为次级滚轮的内容。各级数据按照相同的格式依次嵌套。

注意：
  自定义数据id时不要占用-1这个值，-1用来标记数据中的“全部”item，当你的业务需求需要让用户
选择“全部”(或者叫“不限”，“全省”，“全市”诸如此类)时，请将该item的id设为-1；
showAllItem(boolean all)这个方法可以用来控制是否显示“全部”item。
所以当业务需求没有“全部”item，而此方法被误调用(false)后,会将数据中所有id=-1的项目过滤掉。

  setDataRelated(boolean dataRelated) 这个方法用于设置数据是否按照上面严格的嵌套结构解析。
默认是true。当你想按照自己的数据结构解析数据时，应将此方法置为false，不然将会引发数据解析错误。
如何按照自定义的json结构解析将在接下来讲到。

# 实现自定义的滚轮选择器
本库当然提供了实现自定义滚轮选择的方式，有两种方式可以选择：

  1. 基于核心控件RecyclerWheelPicker进行自定义封装；

  本库所有的滚轮选择器都是基于RecyclerWheelPicker这个自定义的滚轮控件封装而成。
  如果本库已经提供的封装好的滚轮控件不能满足业务需求，可按照自己的喜好用此控件封装。
  RecyclerWheelPicker的一些用法：

a. xml属性attr:

```
      rwp_textSize              // 文字大小
      rwp_textColor             // 文字颜色
      rwp_unitSize              // 单位大小
      rwp_unitColor             // 单位颜色
      rwp_decorationColor       // 选中的item区域样式
      rwp_decorationSize        // 选中的item区域的高度
```


b.一些需要说明的方法：

```
      scrollTargetPositionToCenter(int position)                将目标item滚动到控件中间
      setOnWheelScrollListener(OnWheelScrollListener listener)  设置滚动监听
      isInitFinish()                                            滚轮初始化是否完成
      setPickerSoundEnabled(boolean enabled)                    滚动过程是否允许播放声音
      setData(List<Data> data)                                  加载数据
      setDecoration(IDecoration mDecoration)                    设置选中区域样式
      release()                                                 释放音频资源
```

c. 滚动监听

```
    interface OnWheelScrollListener {
            void onWheelScrollChanged(RecyclerWheelPicker wheelPicker, boolean isScrolling, int position, Data data);
        }
```
参数说明：

    isScrolling：是否正在滚动
    position：选中位置，正在滚动时返回无效位置-1
    data：选中位置的数据，正在滚动时返回null

b.选中区域样式装饰接口

选中区域的样式可以自定义，通过实现以下接口完成自定义工作：
```
    interface IDecoration {
        void drawDecoration(RecyclerWheelPicker picker, Canvas c, Rect decorationRect, Paint decorationPaint);
    }
```
参数说明：

    decorationRect：选中区域的rect
    decorationPaint：用于绘制选中区域的paint

本库提供了一个默认的实现DefaultDecoration，样式为上下两条细线。
在PasswordPicker中提供了另一种实现，代码如下，以作参考：

```
     public void drawDecoration(RecyclerWheelPicker picker, Canvas c,
                                            Rect decorationRect, Paint decorationPaint) {
      decorationPaint.setColor(Color.BLACK);
      decorationPaint.setStrokeWidth(2);
      decorationRect.set(10, 10, picker.getWidth() - 10, picker.getHeight() - 10);
      c.drawRect(decorationRect, decorationPaint);
    }
```


  2 通过继承抽象类WheelPicker来实现自定义滚轮选择器


  这是一种比较简单的自定义方法，需要做两件事：

  a.自定义布局

  b.实现WheelPicker中的抽象方法

  自定义布局很简单，这里不做说明了，布局定义好后只需inflate到WheelPicker中即可。

  WheelPicker其实是一个DialogFragment，因此有以下几个方法需要重载：

```
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
      }
```
布局需要在这个方法中inflate，这个很简单


```
       abstract protected List<Data> parseData();              // 解析数据
       abstract protected void initView();                     // 初始化布局
       abstract protected void inflateData(List<Data> datas);  // 填充数据
       public void onWheelScrollChanged(RecyclerWheelPicker wheelPicker, boolean isScrolling, int position, Data data)
```
这四个方法必须重载

 parseData()方法里，你可以解析自己的数据，这里不关心具体是什么格式的数据，可以是json格式，xml格式等等，
不管如何解析，最后返回一个List<Data>即可。
当然，也可以用默认的数据解析方式，调用如下：

```
      DataParser.parserData(getContext(), builder.resInt, builder.isAll);
```

需要注意的是，采用默认解析方式，传入的数据必须严格按照前面介绍的json格式。

initView()方法里，可以完成一些view的初始化工作，比如滚轮的样式，滚动监听等设置。

inflateData(List<Data> datas)方法里进行数据的填充，可参考本库提供的已经封装好的滚轮器里的实现。

onWheelScrollChanged方法是滚动接口回调方法，也是必须要重写的，可参考本库提供的已经封装好的滚轮器里的实现。




其他一些方法：
```
      pickerClose()
```
这个方法在滚轮关闭后会被调用，如果需要释放资源，可以在这个方法里完成。这不是一个必须重载的方法。




3 最后的一些彩蛋：

  在抽象类WheelPicker里，还提供了另外两个用以控制滚轮选择器弹出和弹入动画的方法，这是我在我另一个专门针对
  自定义dialog动画的库里研究的一些东西，通过这两个方法，可以自由的高度的对dialogFragment的弹出和弹入动画进
  行自定义，而不是受限于只能用系统提供的通过WindowsManager设置动画的方式。如果你感兴趣可以尝试进行自定义：

  相关方法

```
  setEnterAnimDuration(long duration)          设置进入动画时长
  setExitAnimDuration(long duration)           设置退出动画时长
  doEnterAnim(final View contentView, long animDuration)  开始进入动画
  doExitAnim(final View contentView, long animDuration)   开始退出动画
```

看一下doEnterAnim的默认的实现：

```
  public void doEnterAnim(final View contentView, long animDuration) {
          if (builder.gravity == Gravity.BOTTOM) {
              ValueAnimator enterAnimator = ValueAnimator.ofFloat(contentView.getHeight(), 0);
              enterAnimator.setDuration(animDuration);
              enterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                  @Override
                  public void onAnimationUpdate(ValueAnimator animation) {
                      float value = (float) animation.getAnimatedValue();
                      contentView.setTranslationY(value);
                  }
              });
              enterAnimator.start();
          } else {
              ScaleAnimation scaleAnimation = new ScaleAnimation(0F, 1.0F, 0F, 1.0F,
                      Animation.RELATIVE_TO_PARENT, 0.5F, Animation.RELATIVE_TO_PARENT, 0.5F);
              scaleAnimation.setDuration(animDuration);
              scaleAnimation.setFillAfter(true);
              contentView.startAnimation(scaleAnimation);
          }
      }
```
简单说明：当滚轮选择器显示在底部时，进入动画为从底部向上弹出，采用的是属性动画；
如果不是显示在底部，则进入动画为逐渐放大的补间动画。
为什么说是高度自由的自定义动画方式呢？
因为你可以在这两个方法里使用系统提供的任何一种动画方式来满足你的奇思妙想！



# License

 Apache License

 Version 2.0, January 2004

 http://www.apache.org/licenses/







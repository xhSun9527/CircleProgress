# CircleProgress 圆形进度条
## 实现

### 1. CircleProgressByArcView

以画圆弧的形式实现进度条，相对完善

### 2. CircleProgressView

以画两扇形的方式，结合蒙版实现进度条，缺点是进度条不够圆润

## 使用方法

1. 在xml文件中引入View，设置相应属性，可设置属性值如下：

   ```xml
   <declare-styleable name="CirclePercentView">
           <attr name="txtSize" />
           <attr name="stripWidth" />
           <attr name="percent" />
           <attr name="radius" />
           <attr name="bigColor" />
           <attr name="littleColor" />
           <attr name="contentText" />
           <attr name="txtColor"/>
           <attr name="bgColor"/>
       </declare-styleable>
   ```

   

2. 在Activity中获取到相应View后，调用其 **setPercent(int percent)** 方法即可。


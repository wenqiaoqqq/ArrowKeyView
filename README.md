# MyCustomView
some customview 

## 截图
![image](https://github.com/wenqiaoqqq/MyCustomView/blob/master/raw/view.gif)


# Gradle引用

```
 implementation 'com.github.wenqiaoqqq:MyCustomView:v1.4'
```

* 布局文件使用
```xml
<com.wqiao.view.ArrowKeyView
            android:layout_width="180dp"
            android:layout_height="180dp"
            app:outerRingColor="#469eff"
            app:innerRingColor="#999999"
            app:outerRingWidth="2dp"
            app:innerRingWidth="1dp"
            app:leftArrowIcon="@mipmap/icon_ptz_left"
            app:upArrowIcon="@mipmap/icon_ptz_up"
            app:rightArrowIcon="@mipmap/icon_ptz_right"
            app:downArrowIcon="@mipmap/icon_ptz_down"/>
```

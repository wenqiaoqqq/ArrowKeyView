# ArrowKeyView
  customview to handle direction 

## 截图
![image](https://github.com/wenqiaoqqq/MyCustomView/blob/master/raw/view.gif)


# Usage

## Gradle
* Add it in your root build.gradle at the end of repositories:
```
 allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
* Add the dependency
```
 implementation 'com.github.wenqiaoqqq:ArrowKeyView:v1.5'
```

## 布局文件使用
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
```java
ArrowKeyView arrowKeyView = findViewById(R.id.arrowKeyView);
arrowKeyView.setOnclickPtzListener(new ArrowKeyView.OnclickPtzListener() {
    @Override
    public void clickLeft() {

    }

    @Override
    public void clickUp() {

    }

    @Override
    public void clickRight() {

    }

    @Override
    public void clickDown() {

    }
});

```
### 属性说明
| 属性名 | 说明 | 单位 | 
| ------ | ------ | ------ | 
| outerRingColor | 外围两个圆环颜色 | reference|color | 
| innerRingColor | 里面小圆环颜色 | reference|color | 
| outerRingWidth | 外围两个圆环宽度 | dimension |
| innerRingWidth | 里面小圆环宽度 | dimension |
| leftArrowIcon | 左方向键 | reference |
| upArrowIcon | 上方向键 | reference |
| rightArrowIcon | 右方向键 | reference |
| downArrowIcon | 下方向键 | reference |


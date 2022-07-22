# 親のViewを遡ってViewを取得

```java
//  親のViewを取得
ViewGroup vg = (ViewGroup) checkBox.getParent();
//  このviewをすべて列挙
for(int i = 0;i < vg.getChildCount();i++) {
    View v = vg.getChildAt(i);
    //  TextViewすべてを取得
    if( v instanceof TextView){
        TextView tv= (TextView)v;
        //  IDで更に分岐
        if(tv.getId() == R.id.tvTitle) {
            tv.setText("終了");
        }
    }
}
```

# SimpleViewのレイアウトファイルのウィジェットの設定を行う方法

SimpleAdapterクラスのViewBinderをオーバーライドすることでデータとviewとの紐付け処理を自作できる

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    ...

    SimpleAdapter adapter = new SimpleAdapter(
            MainActivity.this,
            d,              //  上で定義
            R.layout.cell,  //  自作レイアウト
            from,           //  上で定義
            to              //  上で定義
    );

    //  カスタマイズバインダーを設定
    adapter.setViewBinder(new CustomViewBinder());
}

//  自分で定義
private static class CustomViewBinder implements SimpleAdapter.ViewBinder{
  //  ウィジェットの数だけ呼ばれて、すべてのウィジェットが1回ずつviewとして渡ってくる
    @Override
    public boolean setViewValue(View view, Object o, String s) {
        //  どのウィジェットかチェック
        if(view instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) view;
        }

        //  どのウィジェットかチェック
        if(view instanceof TextView) {
            TextView textView = (TextView) view;
            //  複数同じウィジェットがある場合は、idで分岐
            if(textView.getId() == R.id.xxx){

            }
        }

        //  自分でバインディング処理を書いているのでtureを返す
        //  falseを返すと、この処理が適応されない可能性がある
        return true;
    }
}
```

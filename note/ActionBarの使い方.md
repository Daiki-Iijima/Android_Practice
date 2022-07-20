# ActionBarの使い方

ActionBarは拡張性はそこまでないが、簡単に使用することができる(通常のテーマを使用しているのであれば、デフォルトで表示されているはず)

## ActionBarの表示非表示の切り替え

### 事前に非表示にする

themes.xmlの以下の箇所を`NoActionBar`に書き換える

- 書き換え前

  ```xml
  <style name="Theme.ToDoApp" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
  ```

- 書き換え後

  ```xml
  <style name="Theme.ToDoApp" parent="Theme.MaterialComponents.DayNight.NoActionBar">
  ```

### コードでActivityごとに表示に表示にする

```java
//  アクションバーを取得
//  アクションバーはハードコートされているので、findViewByIdでは取得できない
ActionBar bar = getSupportActionBar();

//  非表示
bar.hide();

//  表示
bar.show();
```

## アクションバーの設定

ActionBarをActivityから取得する必要がある。`通常の画面部品と違い、findViewByIdメソッドでレイアウトファイルから取得する方法はないので注意`

```java
//  アクションバーを取得
ActionBar bar = getSupportActionBar();
```

以降の設定では、ここで取得した`bar変数`に対して設定を当てていく

- タイトル

    - タイトルテキスト設定

        ```java
        bar.setTitle("タイトル");
        ```

    - タイトルの表示非表示切り替え

        ```java
        bar.setDisplayShowTitleEnabled(true or false);
        ```

- サブタイトル

    サブタイトルを表示非表示の設定がなかった
    ```java
    bar.setSubTitle("サブタイトル");
    ```
- アイコン

    ```java
    bar.setDisplayShowHomeEnabled(true);  //  アイコンの表示を有効化
    bar.setIcon(android.R.drawable.ic_media_next);  //  アイコンを設定
    ```
- ロゴ

    アイコンとロゴの差があんまりわからないので、とりあえずはアイコンを使うようにすればOK

- カスタムビュー

    AppBar部分に表示する画面部品をもっと柔軟に配置することができる様になる

    以下の手順でカスタムビューを生成して、レイアウトファイルにレイアウトを記述。同時に生成される、javaファイルの方は追加するだけであればいじる必要はない。

    [カスタムビュー作成手順](https://youtu.be/3vnoEYJ0BLU)

    ActionBar部分に作成したレイアウトを表示するには、以下のように記述する
      - タイトルテキストは表示されなくなる

          ```java
          bar.setCustomView(R.layout.作成したレイアウトファイル);
          bar.setDisplayShowCustomEnabled(true);
          ```

    Activityでカスタムビュー内の画面部品を取得するには

    ```java
    //  カスタムビューの部品を取得
    View v = bar.getCustomView();
    Button btn1 = v.findViewById(R.id.button1);
    btn1.setOnClickListener(new OnClickButton());
    ```

- オプションメニュー(オーバーフローメニュー)

    オプションメニューの追加には、レイアウトファイルが必要になる。

    メニュー用のレイアウトファイルを入れるファルダが用意されていないので、自分で作る必要がるので、以下の動画の手順で作成する。

    メニューレイアウトファイルは、以下の公式のドキュメントがわかりやすい

    [オプションメニュー作成](https://developer.android.com/guide/topics/ui/menus?hl=ja#java)

    ActionBarの`戻るボタン`は`onOptionsItemSelected`メソッドの中で`android.R.id.home`で判別できる

```java
//  メニューの生成
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    //  インフレーターを取得する
    MenuInflater inflater = getMenuInflater();

    //  引数でうけとったMenuにレイアウトファイルをインフレート
    inflater.inflate(R.layout.レイアウトファイル, menu);

    //  オーバーライドしたらtrueを返す決まり
    return true;
}
```

```java
//  オプションメニュークリック時のイベント
@Override
public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id =item.getItemId();

    //  2つ目以降のアクティビティの戻るボタンクリック時の処理
    if(id == android.R.id.home) {
        //  遷移元のActivityに戻る
        finish();
    }

    return true;
}
```

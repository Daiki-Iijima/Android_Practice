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

- タイトル
- サブタイトル
- アイコン
- 
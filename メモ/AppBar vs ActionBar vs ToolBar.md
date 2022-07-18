# AppBar vs ActionBar vs ToolBar

## AppBar

`Android 3.0（Honeycomb）以前に使用されていたが、現在は使用されておらず、現在はActionBarに置き換えられている`

### 含まれるコンポーネント

- タイトル
- メニューボタン

## ActionBar

アクティビティ画面の上部にあるバー

- このバーの位置は上部から変更できない
- テーマによって、ハードコートされるでxmlで追加する必要はない

Android 3.0からそれまで使用された`AppBarを置き換える`形で実装された

- ActionBarにはAppBarと違い、ユーザーがよく使うボタンなどを配置できるようになった

### 含まれているコンポーネント

- ナビゲーションコントロールボタン(ドロワー)
- アイコン
- タイトル,サブタイトル
- アクションボタン
- アクションオーバーフローメニュー

## Toolbar

ActionBarの高度な後継者です。(ActionBarはテーマレベルで追加していたが、ToolbarはViewGroupコンポーネント)

- ActionBarと違い、xmlで追加できる
- ActionBarより柔軟にカスタマイズできる
- 位置もハードコートされていないので、好きな位置に置くことができる

Android Lollipop(API 21)で追加された

Androidのマテリアルデザインテーマに対応しているが、それ以前のバージョンへの後方互換性も持っている。

### 含まれているコンポーネント

- ActionBarと同じコンポーネント
- 複数のカスタムビュー

## 参考

[geeksforgeeks](https://www.geeksforgeeks.org/difference-between-appbar-actionbar-and-toolbar-in-android/)
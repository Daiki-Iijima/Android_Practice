# Android_Practice

# 開発環境
- Android Studio : 2021.2.1 Patch 1
- 動作確認済み
    - M1 MacBookAir
    - IntelCPU Windows10

# メモ
## 自動ダークモードを解除する
プロジェクトを作成すると、ダークモードと通常モード用のthemes.xmlが生成され、OS側でユーザーの設定によって自動で切り替えて使うようになっているらしい

実験的に作っているアプリだと色合いのことを気にしていられないので、とりあえず`ライトモード(通常モード)`で固定にしたい

その場合は、以下のファイルを書き換える

`res->values->themes->themes.xml(night)`

![ダークモードのテーマ](./Images/ダークモードのテーマ.png)

#### 書き換える箇所
```xml
<style name="Theme.ViewSample" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
```

これを、以下のように書き変える

```xml
<style name="Theme.ViewSample" parent="Theme.MaterialComponents.Light.DarkActionBar">
```

ちなみに、layoutファイルをデザインモードで開いているときに、以下の部分のドロップダウンでテーマごとの色合いの変化を見ることができる

![テーマ切り替えプレビュー](./Images/テーマ切り替えプレビュー.png)

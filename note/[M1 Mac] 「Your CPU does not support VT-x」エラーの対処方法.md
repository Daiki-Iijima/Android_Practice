## 環境

- M1 MacBook Air : Big Sur 11.6.1
- Android Studio : Android Studio Chipmunk | 2021.2.1 Patch 1

## エラー内容

Android Emulatorを作成するために、Android StudioのDivice Managerから、Create Deviceして、いつもどおりの手順で、OSのバージョンを選択するときに、以下のエラーが発生して、作成したエミュレーターが起動しなかった

```none
Your CPU does not support VT-x
```

## 解決策

以下のスタックオーバーフローの回答で解決する事ができた。

[Android Studio/Emulator on macOS with ARM CPU M1](https://stackoverflow.com/questions/64907154/android-studio-emulator-on-macos-with-arm-cpu-m1)

デフォルトで、インストールされているAndroid Emulatorをアンインストールして、Create Deviceのときにインストールすることで自動で最新かつ今の環境にあったものを・インストールしてくれるらしい。


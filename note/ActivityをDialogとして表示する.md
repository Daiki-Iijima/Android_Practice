# ActivityをDialogとして表示する

少し凝ったレイアウトのダイアログを作りたい場合にいいのかもしれない

## 設定

通常のEmpty Activityの作成手順でAcitivityを作成して、自動で追記されたAndroidManifest.xmlの追加されたActivity部分を書き換える

AndroidManifest.xmlに追加するActivityのスタイルを`@style/customDialogTheme`にすることで、ActivityをDialogのように表示することができる

```xml
<activity android:name=".DialogActivity" android:theme="@style/customDialogTheme" android:exported="false" />
```

## 結果の受け取り方

startAcitivyでは、ただボタンが押せるだけのダイアログになってしまうので、結果を受け取れるようにしたい

その場合は、ActivityResultLauncherクラスを使用して、結果を受け取れるようにする。

以下のコードは、大まかな流れのコード

### コード

- MainAtivity

  ```java
  public class MainActivity extends AppCompatActivity {
      //  ランチャー用のフィールドを確保
      private ActivityResultLauncher<Intent> mResultLauncher;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          ...

          //  このアクティビティに戻ってきたときの処理を記述
          mResultLauncher = registerForActivityResult(
                  new ActivityResultContracts.StartActivityForResult(),
                  result -> {
                      //  キャンセルされていたら処理を停止
                      if(result.getResultCode() == RESULT_CANCELED) {return;}

                      if(result.getResultCode() == RESULT_OK){
                      }
                  }
          );

          //  ダイアログの表示
          Intent intent = new Intent(MainActivity.this,ダイアログ用のActivity.class);
          //  引数を渡す場合は、intentにput
          //  intent.putExtra();
          //  表示
          mResultLauncher.launch(intent);
      }


  }
  ```

- DialogActivity

  戻る処理だけ抜粋

  ```java
  Intent intent = new Intent();
  if(id == R.id.Ok){
      //  情報を追加する場合
      //intent.putExtra();
      setResult(RESULT_OK,intent);
  }else if(id == R.id.Cancel){
      //  情報を追加する場合
      //intent.putExtra();
      setResult(RESULT_CANCELED,intent);
  ```

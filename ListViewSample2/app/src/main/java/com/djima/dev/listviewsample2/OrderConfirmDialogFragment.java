package com.djima.dev.listviewsample2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class OrderConfirmDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState){
        //  ダイアログビルダーを生成
        //  生成するアクティビティが決まっていないので、このクラスを生成したアクティビティを取得するようにする
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //  ダイアログのタイトルを設定
        builder.setTitle(R.string.dialog_title);

        //  ダイアログメッセージを設定
        builder.setMessage(R.string.dialog_msg);

        //  Positive Buttonを設定
        builder.setPositiveButton(R.string.dialog_btn_ok,new DialogButtonClickListener());

        //  Negative Buttonを設定
        builder.setNegativeButton(R.string.dialog_btn_ng,new DialogButtonClickListener());

        // Neutral Buttonを設定
        builder.setNeutralButton(R.string.dialog_btn_nu,new DialogButtonClickListener());

        //  ダイアログオブジェクトを生成して返す
        return builder.create();
    }

    //  ダイアログがクリックされたときの処理が記述されたメンバクラス
    private class DialogButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){
            //  トーストで表示するクラス
            String msg = "";

            //  タップされたアクションボタンで分岐
            switch (which){
                case Dialog.BUTTON_POSITIVE:    //  Positive Button
                    msg = getString(R.string.dialog_ok_toast);
                    break;
                case Dialog.BUTTON_NEGATIVE:    //  Negative Button
                    msg = getString(R.string.dialog_ng_toast);
                    break;
                case Dialog.BUTTON_NEUTRAL:     //  Neutral Button(OKとNG以外の問い合わせみたいなボタン)
                    msg = getString(R.string.dialog_nu_toast);
                    break;
            }

            //  トーストを表示
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}

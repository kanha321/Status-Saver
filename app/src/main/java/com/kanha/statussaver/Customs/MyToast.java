package com.kanha.statussaver.Customs;

import android.content.Context;
import android.widget.Toast;

public class MyToast {

    static Toast toast;

    public static void showToast(Context context, String message, int duration) {

        if (MyToast.toast != null) {
            MyToast.toast.cancel();
        }

        MyToast.toast = Toast.makeText(context, String.valueOf(message), duration);
        MyToast.toast.show();
    }

    public static void showToast(Context context, String message) {

        if (MyToast.toast != null) {
            MyToast.toast.cancel();
        }

        MyToast.toast = Toast.makeText(context, String.valueOf(message), Toast.LENGTH_SHORT);
        MyToast.toast.show();
    }
}

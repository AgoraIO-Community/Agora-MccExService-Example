package io.agora.mccex_example.utils

import android.content.Context
import android.widget.Toast

class ToastUtils {

    companion object {

        fun showShortToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun showLongToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}
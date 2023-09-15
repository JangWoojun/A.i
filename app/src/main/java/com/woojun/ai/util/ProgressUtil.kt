package com.woojun.ai.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.woojun.ai.R

object ProgressUtil {

    fun createLoadingDialog(context: Context): Dialog {
        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.loading_dialog)

        return dialog
    }

    fun createDialog(context: Context, dialogType: Boolean, mainText: String, subText: String, function: (dialog: Dialog) -> Unit) {
        val dialogSource: Int = R.layout.success_and_failure_dialog

        val dialogView = LayoutInflater.from(context).inflate(dialogSource, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.success_failure_imageview)
        val mainTextView = dialogView.findViewById<TextView>(R.id.main_text)
        val subTextView = dialogView.findViewById<TextView>(R.id.sub_text)
        val dialogOkButton = dialogView.findViewById<CardView>(R.id.finish_button)

        if (dialogType) {
            imageView.setImageResource(R.drawable.success_icon)
            mainTextView.setTextColor(Color.parseColor("#4894FE"))
            dialogOkButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4894FE"))
        } else {
            imageView.setImageResource(R.drawable.fail_icon)
            mainTextView.setTextColor(Color.parseColor("#FF6666"))
            dialogOkButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF6666"))
        }

        mainTextView.text = mainText
        subTextView.text = subText

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()

        val layoutParams = WindowManager.LayoutParams()
        val window = dialog.window
        layoutParams.copyFrom(window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = layoutParams

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        dialogOkButton.setOnClickListener {
            function(dialog)
        }
    }

}

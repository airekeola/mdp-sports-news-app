package com.mariofc.sportsnewsapp.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.mariofc.sportsnewsapp.R

class AppDialogBuilder {
    companion object {
        fun createDialog(
            context: Context,
            layout: Int,
            title: String,
            positiveBtnText: String,
            initAction: (View) -> Unit
        ): AlertDialog {
            val dialogView = LayoutInflater.from(context).inflate(layout, null)
            dialogView.findViewById<TextView>(R.id.dialogTitle).text = title
            initAction(dialogView)

            // Build the AlertDialog
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setView(dialogView)
                .setPositiveButton(positiveBtnText, null)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

            // Create and show the dialog
            return dialogBuilder.create()
        }
    }
}
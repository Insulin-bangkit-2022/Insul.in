package com.insulin.app.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat

object Helper {

    fun notifyGivePermission(context: Context, message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
//        val dialog = dialogInfoBuilder(context, message)
//        val button = dialog.findViewById<Button>(R.id.button_ok)
//        button.setOnClickListener {
//            dialog.dismiss()
//            openSettingPermission(context)
//        }
//        dialog.setCancelable(false)
//        dialog.show()
    }

    fun isPermissionGranted(context: Context, permission: String) =
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    fun openSettingPermission(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(intent)
    }

}
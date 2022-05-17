package com.insulin.app.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.insulin.app.R

object Helper {

    fun notifyGivePermission(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

    /* -------------------------
       * GEOLOCATION & GEOCODER
       * ------------------------- */

    /* parse lat lon coordinate into readable address */
    fun parseAddressLocation(
        context: Context,
        lat: Double,
        lon: Double
    ): String {
        val geocoder = Geocoder(context)
        val geoLocation =
            geocoder.getFromLocation(lat, lon, 1)
        return if (geoLocation.size > 0) {
            val location = geoLocation[0]
            location.getAddressLine(0)
        } else {
            "Location Unknown"
        }
    }

    /* -------------------------
    *  CUSTOM DIALOG
    * ------------------------- */

    /* custom dialog info builder -> reuse to another invocation with custom ok button action */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun showDialogDiagnoseResult(
        context: Context,
        isDiabetes: Boolean
    ) {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.window!!.apply {
            attributes.windowAnimations = android.R.transition.fade
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        val layout =
            if (isDiabetes) R.layout.custom_dialog_detection_result_1 else R.layout.custom_dialog_detection_result_0
        dialog.setContentView(layout)
        val btnClose: Button = dialog.findViewById(R.id.btn_close_dialog)
        val resultToggle: RelativeLayout = dialog.findViewById(R.id.result_toggle)
        val resultDetail: TableLayout = dialog.findViewById(R.id.result_detail)
        val iconToggle: ImageView = dialog.findViewById(R.id.icon_toggle)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        /* init layout -> hide data */
        resultDetail.isVisible = false
        iconToggle.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_down))

        /* if toggle show data clicked */
        resultToggle.setOnClickListener {
            /* if detail expanded */
            if (resultDetail.isVisible) {
                resultDetail.isVisible = false
                iconToggle.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_down))
            } else {
                resultDetail.isVisible = true
                iconToggle.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_up))
            }
        }

        dialog.show()
    }

}
package com.insulin.app.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.insulin.app.R
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

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

    /* dialog info builder for dialog instance invocation -> with customized ok button action*/
    fun dialogInfoBuilder(
        context: Context,
        title: String,
        body: String
    ): Dialog {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.window!!.apply {
            attributes.windowAnimations = android.R.transition.fade
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        dialog.setContentView(R.layout.custom_dialog_info)
        val dialogTitle: TextView = dialog.findViewById(R.id.dialog_title)
        val dialogBody: TextView = dialog.findViewById(R.id.dialog_body)
        dialogTitle.text = title
        dialogBody.text = body
        return dialog
    }

    /* -------------------------
    *  STRING MANIPULATION
    * ------------------------- */


    /* generate some random string for general purposes */
    private fun getRandomString(len: Int = 20): String {
        val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(len) { alphabet.random() }.joinToString("")
    }

    /* -------------------------
    *  DATE FORMAT
    * ------------------------- */
    private const val simpleDatePattern = "dd MMM yyyy HH.mm"
    private var defaultDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
    private var diagnoseIdDate = SimpleDateFormat("yyMMdd_HHmmssSSSS", Locale.getDefault())

    /*
    * DATE INSTANCE
    * */

    /* current date as Date format */
    private fun getCurrentDate(): Date {
        return Date()
    }

    /* current date as String Format */
    fun getCurrentDateString(): String = defaultDate.format(getCurrentDate())


    /* build date format for diagnose ID purposes */
    private fun getDiagnoseIdDateString(): String = diagnoseIdDate.format(getCurrentDate())

    /* build diagnose ID format to Firebase DB (save history) */
    fun getDiagnoseId(uid: String): String {
        // Format Diagnose ID -> FirebaseUID_220530_1205001234_AbCDeFgHIj
        return StringBuilder(uid).append("_").append(getDiagnoseIdDateString()).append("_")
            .append(getRandomString(10)).toString()
    }
}
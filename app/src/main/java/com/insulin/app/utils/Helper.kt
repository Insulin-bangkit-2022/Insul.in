package com.insulin.app.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.insulin.app.R
import com.insulin.app.adapter.article.AffiliationProductAdapterHorizontal
import com.insulin.app.adapter.article.AffiliationProductAdapterVertical
import com.insulin.app.adapter.article.ArticleAdapter
import com.insulin.app.data.model.AffiliationProduct
import com.insulin.app.data.model.Article
import com.insulin.app.data.model.Detection
import com.insulin.app.databinding.CustomDialogDetectionResult0Binding
import com.insulin.app.databinding.CustomDialogDetectionResult1Binding
import com.insulin.app.ui.webview.WebViewActivity
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

    fun parseBooleanAnswerDetection(answer: Boolean): String {
        return if (answer) "Ya" else "Tidak"
    }

    /* custom dialog info builder -> reuse to another invocation with custom ok button action */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun showDialogDiagnoseResult(
        context: Context,
        isDiabetes: Boolean,
        data: Detection
    ) {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.window!!.apply {
            attributes.windowAnimations = android.R.transition.fade
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        if (isDiabetes) {
            val binding = CustomDialogDetectionResult1Binding.inflate(LayoutInflater.from(context))
            dialog.setContentView(binding.root)
            binding.btnCloseDialog.setOnClickListener { dialog.dismiss() }

            /* init layout -> hide table data */
            binding.resultDetail.root.isVisible = false
            binding.iconToggle.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_down))
            binding.resultToggle.setOnClickListener {
                /* if detail expanded */
                if (binding.resultDetail.root.isVisible) {
                    binding.resultDetail.root.isVisible = false
                    binding.iconToggle.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_down))
                } else {
                    binding.resultDetail.root.isVisible = true
                    binding.iconToggle.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_up))
                }
            }

            binding.diagnoseTime.text =
                StringBuilder("Deteksi pada ").append(reformatDateToSimpleDate(data.detectionTime))

            /* init table data */
            binding.resultDetail.isPolyuria.text = parseBooleanAnswerDetection(data.isPolyuria)
            binding.resultDetail.isPolydipsia.text = parseBooleanAnswerDetection(data.isPolydipsia)
            binding.resultDetail.isWeightLoss.text = parseBooleanAnswerDetection(data.isWeightLoss)
            binding.resultDetail.isWeakness.text = parseBooleanAnswerDetection(data.isWeakness)
            binding.resultDetail.isPolyphagia.text = parseBooleanAnswerDetection(data.isPolyphagia)
            binding.resultDetail.isGenitalThrus.text =
                parseBooleanAnswerDetection(data.isGenitalThrus)
            binding.resultDetail.isItching.text = parseBooleanAnswerDetection(data.isItching)
            binding.resultDetail.isIrritability.text =
                parseBooleanAnswerDetection(data.isIrritability)
            binding.resultDetail.isDelayedHealing.text =
                parseBooleanAnswerDetection(data.isDelayedHealing)
            binding.resultDetail.isPartialParesis.text =
                parseBooleanAnswerDetection(data.isPartialParesis)
            binding.resultDetail.isMuscleStiffness.text =
                parseBooleanAnswerDetection(data.isMuscleStiffness)
            binding.resultDetail.isAlopecia.text = parseBooleanAnswerDetection(data.isAlopecia)
            binding.resultDetail.isObesity.text = parseBooleanAnswerDetection(data.isObesity)
            binding.resultDetail.isDiabetes.text = parseBooleanAnswerDetection(data.isDiabetes)
        } else {
            val binding = CustomDialogDetectionResult0Binding.inflate(LayoutInflater.from(context))
            dialog.setContentView(binding.root)
            binding.btnCloseDialog.setOnClickListener { dialog.dismiss() }

            /* init layout -> hide table data */
            binding.resultDetail.root.isVisible = false
            binding.iconToggle.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_down))
            binding.resultToggle.setOnClickListener {
                /* if detail expanded */
                if (binding.resultDetail.root.isVisible) {
                    binding.resultDetail.root.isVisible = false
                    binding.iconToggle.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_down))
                } else {
                    binding.resultDetail.root.isVisible = true
                    binding.iconToggle.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_up))
                }
            }

            binding.diagnoseTime.text =
                StringBuilder("Deteksi pada ").append(reformatDateToSimpleDate(data.detectionTime))

            /* init table data */
            binding.resultDetail.isPolyuria.text = parseBooleanAnswerDetection(data.isPolyuria)
            binding.resultDetail.isPolydipsia.text = parseBooleanAnswerDetection(data.isPolydipsia)
            binding.resultDetail.isWeightLoss.text = parseBooleanAnswerDetection(data.isWeightLoss)
            binding.resultDetail.isWeakness.text = parseBooleanAnswerDetection(data.isWeakness)
            binding.resultDetail.isPolyphagia.text = parseBooleanAnswerDetection(data.isPolyphagia)
            binding.resultDetail.isGenitalThrus.text =
                parseBooleanAnswerDetection(data.isGenitalThrus)
            binding.resultDetail.isItching.text = parseBooleanAnswerDetection(data.isItching)
            binding.resultDetail.isIrritability.text =
                parseBooleanAnswerDetection(data.isIrritability)
            binding.resultDetail.isDelayedHealing.text =
                parseBooleanAnswerDetection(data.isDelayedHealing)
            binding.resultDetail.isPartialParesis.text =
                parseBooleanAnswerDetection(data.isPartialParesis)
            binding.resultDetail.isMuscleStiffness.text =
                parseBooleanAnswerDetection(data.isMuscleStiffness)
            binding.resultDetail.isAlopecia.text = parseBooleanAnswerDetection(data.isAlopecia)
            binding.resultDetail.isObesity.text = parseBooleanAnswerDetection(data.isObesity)
            binding.resultDetail.isDiabetes.text = parseBooleanAnswerDetection(data.isDiabetes)
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
    private var simpletDate = SimpleDateFormat("dd MMM yyyy | HH:mm", Locale.getDefault())
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

    fun reformatDateToSimpleDate(dateString: String): String {
        val dateValue = defaultDate.parse(dateString) as Date
        return simpletDate.format(dateValue)
    }


    /* build date format for diagnose ID purposes */
    private fun getDiagnoseIdDateString(): String = diagnoseIdDate.format(getCurrentDate())

    /* build diagnose ID format to Firebase DB (save history) */
    fun getDiagnoseId(uid: String): String {
        // Format Diagnose ID -> FirebaseUID_220530_1205001234_AbCDeFgHIj
        return StringBuilder(uid).append("_").append(getDiagnoseIdDateString()).append("_")
            .append(getRandomString(10)).toString()
    }


    fun loadArticleData(
        context: Context,
        rv: RecyclerView,
        articleList: ArrayList<Article>,
        reference: Query,
        reversed: Boolean? = false,
        progressBar: ProgressBar? = null,
    ) {
        progressBar?.isVisible = true
        val TAG = "FIREBASE"
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                articleList.clear()
                for (article in dataSnapshot.children) {
                    val data = article.getValue<Article>()
                    data?.let {
                        articleList.add(it)
                    }
                }
                rv.let {
                    it.setHasFixedSize(true)
                    it.layoutManager = LinearLayoutManager(context)
                    it.isNestedScrollingEnabled = false
                    if (reversed == true) {
                        articleList.reverse()
                    }
                    it.adapter = ArticleAdapter(articleList)
                    progressBar?.isVisible = false
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    fun loadAffiliationProductData(
        context: Context,
        rv: RecyclerView,
        affiliationProductList: ArrayList<AffiliationProduct>,
        reference: Query,
        reversed: Boolean? = false,
        progressBar: ProgressBar? = null,
        horizontalMode: Boolean? = true
    ) {
        progressBar?.isVisible = true
        val TAG = "FIREBASE"
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                affiliationProductList.clear()
                for (article in dataSnapshot.children) {
                    val data = article.getValue<AffiliationProduct>()
                    data?.let {
                        affiliationProductList.add(it)
                    }
                }
                rv.let {
                    it.setHasFixedSize(true)
                    it.layoutManager = if (horizontalMode == true) LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    ) else LinearLayoutManager(context)
                    it.isNestedScrollingEnabled = false
                    if (reversed == true) {
                        affiliationProductList.reverse()
                    }
                    it.adapter = if (horizontalMode == true) AffiliationProductAdapterHorizontal(
                        affiliationProductList
                    ) else AffiliationProductAdapterVertical(affiliationProductList)
                    progressBar?.isVisible = false
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    @SuppressLint("NewApi")
    fun getRupiahFormat(value: Int): String {
        val formatter: NumberFormat = DecimalFormat("###,###,###")
        return StringBuilder("Rp ").append(formatter.format(value).replace(",", ".")).toString()
    }

    fun openLinkInBrowser(context: Context, url: String) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    }

    fun openLinkInWebView(context: Context, url: String) {
        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.EXTRA_WEBVIEW, url)
        context.startActivity(intent)
    }

    fun versionCompare(v1: String, v2: String): Int {
        /* sources : https://www.geeksforgeeks.org/compare-two-version-numbers/ */
        var vnum1 = 0
        var vnum2 = 0
        var i = 0
        var j = 0
        while (i < v1.length
            || j < v2.length
        ) {
            while (i < v1.length
                && v1[i] != '.'
            ) {
                vnum1 = (vnum1 * 10
                        + (v1[i] - '0'))
                i++
            }
            while (j < v2.length
                && v2[j] != '.'
            ) {
                vnum2 = (vnum2 * 10
                        + (v2[j] - '0'))
                j++
            }
            if (vnum1 > vnum2) return 1
            if (vnum2 > vnum1) return -1
            vnum2 = 0
            vnum1 = vnum2
            i++
            j++
        }
        return 0
    }

}
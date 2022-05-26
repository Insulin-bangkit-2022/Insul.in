package com.insulin.app.ui.login

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.insulin.app.R
import com.insulin.app.databinding.ActivityLoginBinding
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.ui.onBoarding.OnBoardingActivity
import com.insulin.app.utils.Helper

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.btnLogin.setOnClickListener {
            signIn()
        }

        loginBinding.btnBack.setOnClickListener {
            startActivity(Intent(this@LoginActivity, OnBoardingActivity::class.java))
            finish()
        }

        /* init login property */
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(user: FirebaseUser?) {
        user?.let { data ->
            /* update data user to server */
            val dataUser = HashMap<String, String>()
            dataUser["user_id"] = data.uid
            dataUser["user_name"] = data.displayName ?: "Name Not Set"
            dataUser["user_email"] = data.email ?: "Email Not Set"
            dataUser["user_phone"] = data.phoneNumber ?: "Phone Number Not Set"
            dataUser["user_avatar"] = data.photoUrl.toString()
            dataUser["user_login_at"] = Helper.getCurrentDateString()
            dataUser["session_login_device_brand"] = Build.BRAND.uppercase()
            dataUser["session_login_device_time"] = Build.TIME.toString()
            dataUser["session_login_device_model"] = Build.DEVICE.uppercase()
            dataUser["session_login_login_provider"] = data.providerId
            dataUser["session_login_application_time"] = Helper.getCurrentDateString()
            dataUser["session_login_device_OS_version_sdk"] = VERSION.SDK_INT.toString()
            dataUser["session_login_application_version"] =
                packageManager.getPackageInfo(packageName, 0).versionName
            FirebaseDatabase.getInstance().reference.child("users").child(user.uid)
                .setValue(dataUser).addOnSuccessListener {
                    Log.i("FIREBASE", "Success Update Login Data")
                }.addOnFailureListener { e ->
                    Log.e(
                        TAG,
                        "Failed Update Login Data : $e",
                        e.cause
                    )
                }

            /* move to home */
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}
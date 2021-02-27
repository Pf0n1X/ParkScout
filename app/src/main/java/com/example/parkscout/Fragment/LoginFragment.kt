package com.example.parkscout.Fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.parkscout.MainActivity

import com.example.parkscout.R
import com.example.parkscout.ui.login.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*

class LoginFragment1 : Fragment() {

    private var locationPermissionGranted = false
    private lateinit var loginViewModel: LoginViewModel

    private fun Login() {
        val email = logemail.text.toString();
        val password = logpass.text.toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Email or password are empty", Toast.LENGTH_SHORT)
                .show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener({
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Main", "Successfully signed in with uid: ${it.result?.user?.uid}")
                loggedIn()
            })
            .addOnFailureListener {
                Toast.makeText(getActivity(), "Sign in failed: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun googleLogin() {
        lateinit var mGoogleSignInClient: GoogleSignInClient
        lateinit var mGoogleSignInOptions: GoogleSignInOptions
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val activity = this.activity as Activity
        mGoogleSignInClient = GoogleSignIn.getClient(activity, mGoogleSignInOptions)
        mGoogleSignInClient.signOut()
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener({
//                Log.d("Main", "Successfully Logged in with google uid: ${it.result?.user?.uid.toString()}")
//                loggedIn()
                checkIfUserExists(it.result?.user?.uid.toString())
            })
            .addOnFailureListener {
                Toast.makeText(getActivity(), "Register failed: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun checkIfUserExists(uid: String) {
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("uid", uid)
            .limit(1).get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    Log.d("Main", "Successfully Logged in with google uid: ${uid}")
                    loggedIn()
                } else {
                    Toast.makeText(getActivity(), "Google user doesn't exists, please register first", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately

                Log.d("Main", "Google sign in failed", e)
            }
        }
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            moveToMainActivity()
        } else {
            ActivityCompat.requestPermissions(
                this.requireContext() as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                    moveToMainActivity()
                }
            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(getActivity(), MainActivity::class.java)
        getActivity()?.startActivity(intent)
    }

    private fun loggedIn() {
        getLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            loggedIn()
        }

        val registerText: TextView = view?.findViewById(R.id.moveToReg) as TextView
        var loginBtn = view.findViewById(R.id.loginbtn) as Button
        val googleSignUpBtn: ImageButton = view?.findViewById(R.id.googleSignIn) as ImageButton

        registerText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.registerFragment)
        }

        loginBtn.setOnClickListener {
            Login();
        }

        googleSignUpBtn.setOnClickListener {
            googleLogin();
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

//    private fun updateUiWithUser(model: LoggedInUserView) {
//        val welcome = getString(R.string.welcome) + model.displayName
//        // TODO : initiate successful logged in experience
//        val appContext = context?.applicationContext ?: return
//        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
//    }
//
//    private fun showLoginFailed(@StringRes errorString: Int) {
//        val appContext = context?.applicationContext ?: return
//        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
//    }
}
package com.example.parkscout.Fragment

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation

import com.example.parkscout.R
import com.example.parkscout.ui.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

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
            })
            .addOnFailureListener {
                Toast.makeText(getActivity(), "Sign in failed: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
//        val registerText: TextView = view?.findViewById(R.id.moveToReg) as TextView
//        registerText.setOnClickListener {
//            Log.d("Main", "click")
//            Navigation.findNavController(view).navigate(R.id.registerFragment)
//        }
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val registerText: TextView = view?.findViewById(R.id.moveToReg) as TextView
        var loginBtn = view.findViewById(R.id.loginbtn) as Button

        registerText.setOnClickListener {
            Log.d("Main", "click")
            Navigation.findNavController(view).navigate(R.id.registerFragment)
        }

        loginBtn.setOnClickListener {
            Login();
        }
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
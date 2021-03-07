package com.example.parkscout.Fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.parkscout.MainActivity
import com.example.parkscout.R
import com.example.parkscout.Repository.User
import com.example.parkscout.ViewModel.RegisterFragmentViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_register.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null
    var selectedPhotoUri: Uri? = null;
    private var locationPermissionGranted = false
    lateinit var facebookSignInButton: ImageButton
    var profilePic: ImageView? = null

    // Data Members
    private lateinit var viewModel: RegisterFragmentViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
    }

    private fun Register() {
        val email = regemail.text.toString();
        val password = regpass.text.toString();
        val con_password = regconpass.text.toString();
        val name = regname.text.toString();
        profilePic = profilepic

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Name or email or password are empty", Toast.LENGTH_SHORT)
                .show()
            return
        } else if (!password.equals(con_password)) {
            Toast.makeText(getActivity(), "Incompatible passwords", Toast.LENGTH_SHORT).show()
            return
        } else if (profilepic.getDrawable() == null) {
            Toast.makeText(getActivity(), "Please add profile picture", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener({
                if (!it.isSuccessful) return@addOnCompleteListener
                UplaodImage(selectedPhotoUri.toString(), regname.text.toString())
                Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
            })
            .addOnFailureListener {
                Toast.makeText(getActivity(), "Register failed: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    public fun UplaodImage(uriString: String, name: String) {

        if (uriString == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        val myUri = Uri.parse(uriString)
        ref.putFile(myUri)
            .addOnSuccessListener {
                Log.d("Main", "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Main", "File location: $it")
                    SaveUserInformationToFirebase(it.toString(), name)
                }
            }
            .addOnFailureListener {
                Toast.makeText(getActivity(), "Upload image failed: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    public fun googleSignUp() {
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

    public fun getImageUriFromBitmap(bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(getActivity()?.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    private fun facebookSignUp() {

    }

    private fun firebaseAuthWithFacebook(token: String) {
        val credential = FacebookAuthProvider.getCredential(token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener({
                Log.d("Main", "Successfully created google user with uid: ${it.result?.user?.uid}")
                val user = FirebaseAuth.getInstance().currentUser
            })
            .addOnFailureListener {
                Toast.makeText(getActivity(), "Register failed: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            profilepic.setImageURI(selectedPhotoUri)
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getActivity(), "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener({
                Log.d("Main", "Successfully created google user with uid: ${it.result?.user?.uid}")
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
                if (documents.size() > 0)
                    Toast.makeText(getActivity(), "Google user already exists, please login", Toast.LENGTH_SHORT)
                        .show()
                else {
                    val acct: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(activity)
                    if (acct != null) {
//                        SaveUserInformationToFirebase("google", acct.displayName.toString())
                        val downImage = DownloadImage(profilepic, this, acct.displayName.toString())
                        downImage.execute(acct.photoUrl.toString())
//                        val bitmap = downloadImage(acct.photoUrl.toString())
//                        profilePic?.setImageBitmap(bitmap)
                    }
                }
            }
    }

    public fun SaveUserInformationToFirebase(profileImageUri: String, name: String) {
        val uid = FirebaseAuth.getInstance().uid
        var user: User = User(uid.toString(), name, profileImageUri, 5 ,"");

        val newUser: MutableMap<String, Any> = HashMap()
        newUser["uid"] = uid.toString()
        newUser["name"] = name
        newUser["profilePic"] = profileImageUri
        newUser["distance"] = 5

        viewModel.addUser(user, {});
        Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show()
        registered()
    }

    private fun moveToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish();
    }

    private fun registered() {
        getLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_register, container, false)
        var signupbtn = view.findViewById(R.id.signupbtn) as Button
        var selectPhotoBtn =
            view.findViewById(R.id.reg_select_photo) as Button

        viewModel = ViewModelProvider(this).get(RegisterFragmentViewModel::class.java);

//        FirebaseAuth.getInstance().signOut()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        FirebaseAuth.getInstance().signOut()

        val googleSignUpBtn: ImageButton = view?.findViewById(R.id.googleSignIn) as ImageButton
//        val facebookSignUpBtn: ImageButton = view?.findViewById(R.id.facebookSignIn) as ImageButton
        val loginText: TextView = view?.findViewById(R.id.moveToReg) as TextView

        loginText.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.loginFragment)
        }
// set on-click listener
        signupbtn.setOnClickListener {
            Register()
        }

        selectPhotoBtn.setOnClickListener {
            selectPhoto()
        }

        googleSignUpBtn.setOnClickListener {
            googleSignUp()
        }

//        facebookSignUpBtn.setOnClickListener {
//            facebookSignUp()
//        }
        return view
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    private class DownloadImage(
        profilepic: ImageView,
        registerFragment: RegisterFragment,
        name: String
    ) : AsyncTask<String, Bitmap, Bitmap>() {

        var profilePic: ImageView? = profilepic
        var name: String? = name
        var registerFragment: RegisterFragment? = registerFragment

        override fun doInBackground(vararg URL: String?): Bitmap? {
            val imageURL = URL[0]
            var bitmap: Bitmap? = null
            try {
                // Download Image from URL
                val input: InputStream = java.net.URL(imageURL).openStream()
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bitmap
        }

        override fun onPostExecute(result: Bitmap) {
            super.onPostExecute(result)
            profilePic?.setImageBitmap(result)
            val path = registerFragment?.getImageUriFromBitmap(result)
            registerFragment?.UplaodImage(path.toString(), name.toString())

        }
    }
}


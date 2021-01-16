package com.example.parkscout

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mAuth: FirebaseAuth? = null
    var selectedPhotoUri: Uri? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun Register() {
        val email = regemail.text.toString();
        val password = regpass.text.toString();
        val con_password = regconpass.text.toString();
        val name = regname.text.toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Name or email or password are empty", Toast.LENGTH_SHORT).show()
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
                UplaodImage()
                Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
            })
            .addOnFailureListener {
                Toast.makeText(getActivity(), "Register failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun UplaodImage() {

        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Main", "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Main", "File location: $it")
                    SaveUserInformationToFirebase(it.toString())
                }
            }
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            profilepic.setImageURI(selectedPhotoUri)
//            val bitmap = MediaStore.Images.Media.getBitmap(con, uri)
//            var bitmapDrawable = BitmapDrawable(bitmap)
//            reg_select_photo.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun SaveUserInformationToFirebase(profileImageUri: String) {
        val uid = FirebaseAuth.getInstance().uid
        val db = FirebaseFirestore.getInstance()
        val newUser: MutableMap<String, Any> = HashMap()
        newUser["uid"] = uid.toString()
        newUser["name"] = regname.text.toString()
        newUser["profilePic"] = profileImageUri

        FirebaseFirestore.getInstance().collection("users").add(newUser)
            .addOnSuccessListener {
                Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d("Main", "Info failed: ${it.message}")
            }

//        val user = User(uid.toString(), regname.text.toString(), profileImageUri)
//        ref.setValue(user)
//            .addOnSuccessListener {
//                Log.d("Main", "Info saved")
//            }
//            .addOnFailureListener {
//                Log.d("Main", "Info failed: ${it.message}")
//            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(com.example.parkscout.R.layout.fragment_register, container, false)
        var signupbtn = view.findViewById(com.example.parkscout.R.id.signupbtn) as Button
        var selectPhotoBtn =
            view.findViewById(com.example.parkscout.R.id.reg_select_photo) as Button
// set on-click listener
        signupbtn.setOnClickListener {
            Register()
        }

        selectPhotoBtn.setOnClickListener {
            selectPhoto()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class User(val uid: String, val username: String, val profilePic: String)
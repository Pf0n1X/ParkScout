package com.example.parkscout.Fragment

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.example.parkscout.R
import com.example.parkscout.Repository.User
import com.example.parkscout.ViewModel.SettingsFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    // Data Members
    private lateinit var viewModel: SettingsFragmentViewModel;

    // Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View?
            = super.onCreateView(inflater, container, savedInstanceState)?.apply {
            this.background = resources.getDrawable(R.drawable.buttonshape);
        }

        viewModel = ViewModelProvider(this).get(SettingsFragmentViewModel::class.java);
//        viewModel =
//            activity?.let { ViewModelProvider(it).get(SettingsFragmentViewModel::class.java) }!!;

        var user: User? = viewModel.user.value;
        viewModel.user.observe(viewLifecycleOwner, Observer { obUser: User? ->
            if (obUser != null) {
                val settings: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context);
                val editor: SharedPreferences.Editor = settings.edit();
                editor.putInt("search_range", obUser.distance);
                editor.putString("name", obUser.name);
                editor.putString("description", obUser.description);
                var profileImg: CircleImageView? = view?.findViewById<CircleImageView>(R.id.profileImage);
                if (profileImg != null) {
                    Glide.with(this).load(obUser.profilePic).into(profileImg)
                };
                editor.commit();
            }
        });

        return view;
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // Handle the image preference.
        val imgPreference: Preference? = findPreference("image");
        imgPreference?.setOnPreferenceClickListener{ it ->
            selectPhoto();
            return@setOnPreferenceClickListener true;
        }

        // Handle the name preference.
        val namePreferences: Preference? = findPreference("name");
        namePreferences?.setOnPreferenceChangeListener{ preference, newValue ->
            var curUser: User? = viewModel.user.value;

            if (curUser != null) {
                curUser.name = newValue as String;
                viewModel.setUser(curUser, {

                });
            }

            true;
        };

        // Handle the name preference.
        val descPreferences: Preference? = findPreference("description");
        descPreferences?.setOnPreferenceChangeListener{ preference, newValue ->
            var curUser: User? = viewModel.user.value;

            if (curUser != null) {
                curUser.description = newValue as String;
                viewModel.setUser(curUser, {

                });
            }

            true;
        };

        // Handle the Search range preference.
        val rangePreferences: Preference? = findPreference("search_range");
        rangePreferences?.setOnPreferenceChangeListener{ preference, newValue ->
            var curUser: User? = viewModel.user.value;

            if (curUser != null) {
                curUser.distance = newValue as Int;
                    viewModel.setUser(curUser, {
                });
            }

            true;
        };
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            var selectedPhotoUri = data.data;
            var profileImg: CircleImageView? = view?.findViewById<CircleImageView>(R.id.profileImage);

            if (profileImg != null) {
                profileImg.setImageURI(selectedPhotoUri);
                uploadImage(selectedPhotoUri.toString(), FirebaseAuth.getInstance().currentUser?.uid);
            }}
    }

    public fun uploadImage(uriString: String, name: String?) {

        // IF no URI was received, do nothing.
        if (uriString == null)
            return

        // Extract the UR as a string
        val filename = UUID.randomUUID().toString()

        // Create a reference for the picture in the cloud storage service.
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        val myUri = Uri.parse(uriString)

        // Upload the file.
        ref.putFile(myUri)
            .addOnSuccessListener {
                Log.d("Main", "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    var user: User? = viewModel.user.value;

                    if (user != null) {
                        user.profilePic = it.toString();
                        viewModel.setUser(user, {

                        });
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(getActivity(), "Picture Upload failed: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }
}
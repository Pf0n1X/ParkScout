package com.example.parkscout.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.ColorInt
import com.example.parkscout.R
import kotlinx.android.synthetic.main.fragment_add_park.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddParkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddParkFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_park, container, false)
        val rootView = inflater.inflate(R.layout.fragment_add_park, container, false)
        var selectPhotoBtn =
            rootView.findViewById(R.id.uploadPhotobBtn) as Button

        selectPhotoBtn.setOnClickListener {
            selectPhoto()
        }

//        val checkedChipId = chipGroup.checkedChipId // Returns View.NO_ID if singleSelection = false
//        val checkedChipIds = chipGroup.checkedChipIds // Returns a list of the selected chips' IDs, if any
//
//        soccerChip.setOnCheckedChangeListener { buttonView, isChecked ->
//            soccerChip.setChipBackgroundColorResource(R.color.blue)
//
//        }
//        chipGroup.setOnCheckedChangeListener { group, checkedId ->
//            if(soccerChip.isChecked){
//                soccerChip.setChipBackgroundColorResource(R.color.blue)
//            }
////             Responds to child chip checked/unchecked
//        }

    }
    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddParkFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddParkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
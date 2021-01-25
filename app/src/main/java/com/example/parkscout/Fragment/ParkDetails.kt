package com.example.parkscout.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import com.example.parkscout.R
import com.example.parkscout.park_full_details


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PARK_NAME = "park name"
private const val  STAR_RATE = 1

/**
 * A simple [Fragment] subclass.
 * Use the [ParkDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParkDetails : Fragment()   {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(PARK_NAME)
            param2 = it.getInt(STAR_RATE.toString())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_park_details, container, false)
        val park_name_b: Button = rootView.findViewById(R.id.park_name) as Button
        val star_rate: RatingBar = rootView.findViewById(R.id.ratingBar) as RatingBar

        val strtext = arguments?.getString("park_name")
        park_name_b.setText(strtext)
//        star_rate.numStars = arguments?.getInt("star_num")!!
        park_name_b.setOnClickListener {
     //       final FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
      //          fragTransaction.setCustomAnimations(R.anim.fragment_slide_from_right, R.anim.animation_leave);

            val intent = Intent(activity, park_full_details::class.java)
            intent.putExtra("park_name",requireArguments().getString("park_name") )
            startActivity(intent)

        }


        // Inflate the layout for this fragment
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ParkDetails.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            ParkDetails().apply {
                arguments = Bundle().apply {
                    putString(PARK_NAME, param1)
                    putInt(STAR_RATE.toString(), param2)
                }
            }
    }
}
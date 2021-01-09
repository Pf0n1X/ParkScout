package com.example.parkscout.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkscout.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ParkDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParkDetails : Fragment() {
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
        val view: View = inflater.inflate(R.layout.fragment_park_details, container,
                false)
        val activity = activity as Context
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
      //  recyclerView.adapter = DogListAdapter(activity)
        return view
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_park_details, container, false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context != null) {
            // Get dog names and descriptions.
            val resources = context.resources
           // names = resources.getStringArray(R.array.names)
          //  descriptions = resources.getStringArray(R.array.descriptions)
          //  urls = resources.getStringArray(R.array.urls)

            // Get dog images.
            //val typedArray = resources.obtainTypedArray(R.array.images)
            //val imageCount = names.size
            //imageResIds = IntArray(imageCount)
            //for (i in 0 until imageCount) {
             //   imageResIds[i] = typedArray.getResourceId(i, 0)
            //}
           // typedArray.recycle()
        }
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
        fun newInstance(): ParkDetails {
            return ParkDetails()
        }

        fun getInstance(): ParkDetails? {
            val bundle = Bundle()
           // bundle.putInt("USERNAME", username)
            val fragment = ParkDetails()
          //  fragment.setArguments(bundle)
            return fragment
        }


//        fun newInstance(param1: String, param2: String) =
//            ParkDetails().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}
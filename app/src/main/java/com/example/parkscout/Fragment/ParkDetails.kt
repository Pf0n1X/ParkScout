package com.example.parkscout.Fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkscout.Adapter.CommentAdapter
import com.example.parkscout.Adapter.ImagesAdapter
import com.example.parkscout.R
import com.example.parkscout.Repository.Comment
import com.example.parkscout.Repository.Rating
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.ViewModel.ParkDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_park_details.*
import java.util.*

private const val PARK_NAME = "park name"
private const val  STAR_RATE = 1

class ParkDetails : Fragment()   {

    // Data Members
    private lateinit var mBtnExpand: Button;
    private lateinit var mContainer: LinearLayout;
    private lateinit var mCommentsRecyclerView: RecyclerView;
    private lateinit var viewModel: ParkDetailsViewModel;
    private lateinit var mCommentAdapter: CommentAdapter
    private lateinit var mBtnSendComment: ImageButton;
    private lateinit var mTVCommentText: TextView;
    private lateinit var mParkId: String
    private lateinit var mImggRecyclerView: RecyclerView
    private lateinit var mAdapter: ImagesAdapter

    // Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_park_details, container, false)

        mContainer = rootView.findViewById(R.id.park_details_container);
        mCommentsRecyclerView = rootView.findViewById(R.id.park_details_rv_comments);
        mTVCommentText = rootView.findViewById(R.id.park_details_comment_input);
        mBtnSendComment = rootView.findViewById(R.id.park_details_btnSendComment);
        mImggRecyclerView = rootView.findViewById(R.id.parkImages)

        setupCommentsRecyclerView();
        setIsVisible(false);
        var bottomSheetBehavior: BottomSheetBehavior<LinearLayout> = BottomSheetBehavior.from(mContainer);
        viewModel = ViewModelProvider(this).get(ParkDetailsViewModel::class.java);

        // Setup the comment send button
        mBtnSendComment.setOnClickListener{event  ->
            var uid: String ? = FirebaseAuth.getInstance().currentUser?.uid;

            if (uid != null) {
                var comment: Comment = Comment(
                    mParkId,
                    uid,
                    mTVCommentText.text.toString(),
                    System.currentTimeMillis()
                );
                viewModel.addComment(mParkId, comment, {
                    Log.d("TAG", "Success when trying to save");
                });
            }

            mTVCommentText.setText("");
        };

        // set up park images
        mImggRecyclerView.setHasFixedSize(true)
        mAdapter = ImagesAdapter(this.requireContext(), LinkedList<Uri>());
        mImggRecyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged();

        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(
            activity?.applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        mImggRecyclerView.layoutManager = linearLayoutManager

        // Inflate the layout for this fragment
        return rootView
    }

    fun setDetails(title: String, starNum: Int, parkId: String) {
        this.mParkId = parkId;
        park_details_park_name.text = title;
        park_details_rating.numStars = starNum;
        setIsVisible(true);

        if(park_details_rating != null) {

            park_details_rating.setOnRatingBarChangeListener(object : RatingBar.OnRatingBarChangeListener {
                override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                    if (p2) {
                        var uid: String? = FirebaseAuth.getInstance().currentUser?.uid;
                        if (uid != null) {
                            var ratingToAdd: Rating? = p0?.rating?.let {
                                Rating(
                                    uid,
                                    mParkId,
                                    it,
                                    System.currentTimeMillis()
                                )

                            };
                            if (ratingToAdd != null) {
                                viewModel.addRating(mParkId, ratingToAdd, {
                                    Log.d("TAG", "Success when trying to save rating");
                                })
                            };

                        }
                    }
                }
            })
        }

        var listener = { spot: TrainingSpotWithAll? ->
            var commentArr = spot?.trainingSpotsWithComments?.comments;
            if (commentArr != null) {
                mCommentAdapter.mComments = commentArr;
                mCommentAdapter.notifyDataSetChanged();
            } else {
                mCommentAdapter.mComments = LinkedList<Comment>();
                (mCommentAdapter.mComments as LinkedList<Comment>).add(Comment("1", "Tomer", "Message", 12345678));
                mCommentAdapter.notifyDataSetChanged();
            }

            var images = spot?.trainingSpotWithImages?.getImages();
            if(mAdapter.imagesURL!= null){
                if(mAdapter.imagesURL!!.size != 0){
                    mAdapter.imagesURL!!.clear();
                }
            }
            if(images != null){

                for (img in images){
                    var imgUri:Uri = Uri.parse(img.ImgUrl);
                    mAdapter.imagesURL?.add(imgUri)
                    mAdapter.notifyDataSetChanged();
                    mImggRecyclerView.adapter = mAdapter
                }

            }

            var rating = spot?.trainingSpotWithRating?.sport_rating;

            if (rating != null) {
                calcAvgRating(rating);
            };

        }

        if (viewModel.trainingSpot.value != null) {
            listener(viewModel.trainingSpot.value);
        }

        viewModel.getTrainingSpotByID(parkId).observe(viewLifecycleOwner, androidx.lifecycle.Observer {  } );
    }
        fun calcAvgRating(rating : List<Rating>){
          val filteredRate =   rating.sortedWith(compareByDescending<Rating> { it.user_Id }
                .thenByDescending { it.rate_dateTime })

            var avgRating : Float;
            var sumRating : Float = 0.0F;
            var latUser : String = "";
            var size  = 0;
            for (rate in filteredRate){
                if(latUser == ""){
                    latUser = rate.user_Id;
                    sumRating += rate.rate;
                    size = 1;
                }else if(latUser != rate.user_Id){
                    sumRating += rate.rate;
                    size ++;
                }
                latUser = rate.user_Id;
            }

            avgRating = sumRating/size;
            park_details_rating.rating = avgRating;
        }

    fun setupCommentsRecyclerView() {
        mCommentsRecyclerView.setHasFixedSize(true)
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        mCommentsRecyclerView.layoutManager = linearLayoutManager
        this.mCommentAdapter = CommentAdapter(requireContext());
        mCommentsRecyclerView.adapter = mCommentAdapter;
    }

    public fun setIsVisible(isVisible: Boolean) {
        mContainer.isVisible = isVisible;
    }
}
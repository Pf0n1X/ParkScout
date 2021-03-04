package com.example.parkscout.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.parkscout.R
import java.util.*

class ImagesAdapter(val context: Context, var imagesURL: LinkedList<Uri>?): RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    private  var mContext: Context
    private  var mImagesURL: LinkedList<Uri>?

    init {
        mContext = context
        mImagesURL = imagesURL;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var park_image: ImageView

        init {
             park_image = itemView.findViewById(R.id.parkImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view: View;
        view = LayoutInflater.from(mContext).inflate(R.layout.fragment_park_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.park_image.setImageURI(mImagesURL?.get(position));
        Glide.with(holder.itemView).load(mImagesURL?.get(position)).into(holder.park_image);

    }

    override fun getItemCount(): Int {
        val size : Int? = imagesURL?.size;
        if(size != null){
            return size;
        }else {
            return 0;
        }
    }

}
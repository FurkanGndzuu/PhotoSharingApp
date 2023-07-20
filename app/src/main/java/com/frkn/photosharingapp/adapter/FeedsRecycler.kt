package com.frkn.photosharingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frkn.photosharingapp.R
import com.frkn.photosharingapp.models.Post
import com.squareup.picasso.Picasso

class FeedsRecycler(val postList : ArrayList<Post>) : RecyclerView.Adapter<FeedsRecycler.PostHolder>() {

    class PostHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        var inflater = LayoutInflater.from(parent.context)
        val view : View = inflater.inflate(R.layout.recycler_row , parent , false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return  postList.size;
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.recycler_row_description_text).text = postList[position].userEmail
        holder.itemView.findViewById<TextView>(R.id.recycler_row_description_text).text = postList[position].description
        Picasso.get().load(postList[position].imageUrl).into(holder.itemView.findViewById<ImageView>(R.id.recycler_row_imageview))
    }
}
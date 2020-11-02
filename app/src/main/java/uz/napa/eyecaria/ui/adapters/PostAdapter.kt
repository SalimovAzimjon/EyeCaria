package uz.napa.eyecaria.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_post.view.*
import uz.napa.eyecaria.R
import uz.napa.eyecaria.model.Post

class PostAdapter : ListAdapter<Post, PostAdapter.PostViewHolder>(DiffPostUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.itemView.apply {
            post_description.text = getItem(position).text
            post_image.setImageResource(getItem(position).imgUrl)
        }
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}

class DiffPostUtil : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Post, newItem: Post) =
        oldItem.text == newItem.text

}
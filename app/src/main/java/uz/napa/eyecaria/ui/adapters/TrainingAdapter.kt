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
import kotlinx.android.synthetic.main.item_training.view.*
import uz.napa.eyecaria.R
import uz.napa.eyecaria.model.Training


class TrainingAdapter :
    ListAdapter<Training, TrainingAdapter.TrainingViewHolder>(DiffTrainingUtil()) {
    private var itemClickListener: ((Training) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        return TrainingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_training, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val training = getItem(position)
        holder.itemView.apply {
            tv_video_desc.text = training.description
            video_thumbnail.setImageResource(training.thumbnail)
            setOnClickListener { itemClickListener?.let { it(training) } }
        }
    }

    fun setOnItemClickListener(listener: (Training) -> Unit) {
        itemClickListener = listener
    }

    inner class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}

class DiffTrainingUtil : DiffUtil.ItemCallback<Training>() {
    override fun areItemsTheSame(oldItem: Training, newItem: Training) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Training, newItem: Training) =
        oldItem.description == newItem.description

}
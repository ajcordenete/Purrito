package com.aljon.purrito.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aljon.purrito.R
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.databinding.ListFavoriteItemBinding
import com.aljon.purrito.ui.TabFragmentDirections
import kotlinx.android.synthetic.main.list_favorite_item.view.*

class FavoriteAdapter (private val onItemSelectListener: OnItemSelectListener): ListAdapter<Feed, FavoriteAdapter.ViewHolder>(FeedDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListFavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feed = getItem(position)
        holder.bind(feed)

        holder.itemView.setOnClickListener { onItemSelectListener.onClick(feed, holder.itemView.findViewById(R.id.feed_image))  }


        holder.itemView.findViewById<ImageButton>(R.id.favorite_button).setOnClickListener { onItemSelectListener.onFavorite(feed) }
        holder.itemView.findViewById<ImageButton>(R.id.share_button).setOnClickListener { onItemSelectListener.onShare(feed.url) }
    }

    class ViewHolder(private val binding: ListFavoriteItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(feed: Feed) {
            binding.feed = feed
            binding.executePendingBindings()
        }
    }

    companion object FeedDiffUtil: DiffUtil.ItemCallback<Feed>() {
        override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem.id.equals(newItem.id, true)
        }
    }

    class OnItemSelectListener(val clickListener: (feed: Feed, imageView: ImageView) -> Unit,
                               val favoriteListener: (feed: Feed) -> Unit,
                               val shareListener: (url: String) -> Unit) {
        fun onClick(feed: Feed, imageView: ImageView) = clickListener(feed, imageView)
        fun onFavorite(feed: Feed) = favoriteListener(feed)
        fun onShare(url: String) = shareListener(url)
    }
}
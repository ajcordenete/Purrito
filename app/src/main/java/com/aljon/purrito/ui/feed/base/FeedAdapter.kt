package com.aljon.purrito.ui.feed.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aljon.purrito.BR
import com.aljon.purrito.R
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.databinding.GridItemFeedBigBinding
import com.aljon.purrito.databinding.GridItemFeedSmallBinding
import com.aljon.purrito.util.Constants.VIEW_TYPE_ITEM_BIG
import com.aljon.purrito.util.Constants.VIEW_TYPE_ITEM_SMALL
import com.aljon.purrito.util.Constants.VIEW_TYPE_LOADING
import com.aljon.purrito.util.MediaHelper

class FeedAdapter(private val onItemSelectListener: OnItemSelectListener): ListAdapter<Feed, RecyclerView.ViewHolder>(FeedDiffUtil) {

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        //null is for loading view
        return if (currentList[position] == null) {
            VIEW_TYPE_LOADING

        } else if(MediaHelper.isGif(currentList[position].url)) {
            VIEW_TYPE_ITEM_BIG

        } else {
            VIEW_TYPE_ITEM_SMALL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_ITEM_SMALL) {
            ViewHolderGridSmall(GridItemFeedSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        } else if(viewType == VIEW_TYPE_ITEM_BIG) {
            ViewHolderGridBig(GridItemFeedBigBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        } else {
            LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == VIEW_TYPE_LOADING){
            val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = true

        } else {
            val feed = getItem(position)

            if(holder is ViewHolderGridSmall) {
                holder.bind(feed)
                holder.itemView.setOnClickListener {
                    onItemSelectListener.onClick(feed, holder.itemView.findViewById(R.id.feed_image))
                }
            }
            else if(holder is ViewHolderGridBig) {
                holder.bind(feed)
                holder.itemView.setOnClickListener {
                    onItemSelectListener.onClick(feed, holder.itemView.findViewById(R.id.feed_image))
                }
            }
        }
    }

    class ViewHolderGridSmall(binding: GridItemFeedSmallBinding): BaseVieHolder(binding)

    class ViewHolderGridBig(binding: GridItemFeedBigBinding): BaseVieHolder(binding)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    open class BaseVieHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(feed: Feed) {
            binding.setVariable(BR.feed, feed)
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

    class OnItemSelectListener(val clickListener: (feed: Feed, imageView: ImageView) -> Unit ) {
        fun onClick(feed: Feed, imageView: ImageView) = clickListener(feed, imageView)
    }
}
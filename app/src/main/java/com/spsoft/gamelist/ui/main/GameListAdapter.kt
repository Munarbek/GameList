package com.spsoft.gamelist.ui.main

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spsoft.gamelist.databinding.ItemGameListBinding
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.spsoft.gamelist.R
import com.spsoft.gamelist.data.models.gamelist.Result
import com.spsoft.gamelist.databinding.ItemLoadingBinding

class GameListAdapter : ListAdapter<Result, RecyclerView.ViewHolder>(DIFF) {
    private lateinit var binding: ItemGameListBinding

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGame {
        return if (viewType == VIEW_TYPE_ITEM) {
            binding =
                ItemGameListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderGame(binding)
        } else {
            binding =
                ItemGameListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderGame(binding)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderGame) {
            populateItemRows(holder as ViewHolderGame, position)
        } else if (holder is LoadingViewHolderGame) {
            showLoadingView(holder as LoadingViewHolderGame, position)
        }
    }


    private fun showLoadingView(viewHolder: LoadingViewHolderGame, position: Int) {
        //ProgressBar would be displayed
    }

    private fun populateItemRows(viewHolder: ViewHolderGame, position: Int) {
        viewHolder.bind(position)
    }

    override fun getItemCount(): Int {
        return if (currentList == null) 0 else currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    inner class ViewHolderGame(private val binding: ItemGameListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val currentGame = getItem(position)
            binding.textViewName.text = currentGame.name
            binding.textViewDate.text = currentGame.released

            binding.imageView.load(currentGame.background_image) {
                crossfade(true)
                placeholder(android.R.drawable.alert_dark_frame)
            }

        }
    }

    inner class LoadingViewHolderGame(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(
                oldItem: Result,
                newItem: Result
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: Result,
                newItem: Result
            ): Boolean {
                return oldItem.name == newItem.name
            }

        }
    }


}

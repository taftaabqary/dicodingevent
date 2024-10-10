package com.althaaf.dicodingevents.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.althaaf.dicodingevents.data.local.EventEntity
import com.althaaf.dicodingevents.data.response.ListEventsItem
import com.althaaf.dicodingevents.databinding.ItemListEventBinding
import com.althaaf.dicodingevents.ui.detail.DetailActivity
import com.bumptech.glide.Glide

class ListEventAdapter<T>(private val listEvent: List<T>): RecyclerView.Adapter<ListEventAdapter<T>.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ItemListEventBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: T) {
            when(data) {
                is ListEventsItem -> {
                    binding.tvEventTime.text = data.beginTime
                    binding.tvEventTitle.text = data.name
                    binding.tvEventOwner.text = data.ownerName
                    binding.tvLocation.text = data.cityName
                    binding.tvCategory.text = data.category
                    Glide.with(this@ListViewHolder.itemView.context)
                        .load(data.imageLogo)
                        .into(binding.ciEvent)

                    this.itemView.setOnClickListener {
                        val intent = Intent(this.itemView.context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.ID_EVENT, data.id)
                        this.itemView.context.startActivity(intent)
                    }
                }

                is EventEntity -> {
                    binding.tvEventTime.text = data.date
                    binding.tvEventTitle.text = data.title
                    binding.tvEventOwner.text = data.owner
                    binding.tvLocation.text = data.location
                    binding.tvCategory.text = data.category
                    Glide.with(this@ListViewHolder.itemView.context)
                        .load(data.image)
                        .into(binding.ciEvent)

                    this.itemView.setOnClickListener {
                        val intent = Intent(this.itemView.context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.ID_EVENT, data.id)
                        this.itemView.context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemListEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataEvent = listEvent[position]
        holder.bind(dataEvent)
    }

    override fun getItemCount(): Int = listEvent.size
}
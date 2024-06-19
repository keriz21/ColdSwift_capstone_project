package com.bangkit.coldswiftapps.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.coldswiftapps.data.remote.response.ListEventResponse
import com.bangkit.coldswiftapps.databinding.EventListBinding
import com.bangkit.coldswiftapps.utils.DateFormatter
import com.bumptech.glide.Glide
import java.util.TimeZone

class EventAdapter(private val events: List<ListEventResponse>) :
RecyclerView.Adapter<EventAdapter.ViewHolder>(){

    private var onItemClickCallback: OnItemClickCallback? = null

    class ViewHolder(var binding: EventListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        val binding = EventListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {
        val event = events[position]
        with(holder.binding){
            Glide.with(root.context)
                .load(event.eventImageURL)
                .into(ivTiket)

            nameEvent.text = event.eventName
            tvVenue.text = event.eventLocation
            tvDate.text = DateFormatter.formatDateDay(event.eventDate!!)
            tvMonth.text = DateFormatter.formatMonthAbbreviation(event.eventDate)
            tvPrice.text = DateFormatter.formatPrice(event.price!!)


            root.setOnClickListener {
                onItemClickCallback?.onItemClicked(event)
            }
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListEventResponse)
    }
}
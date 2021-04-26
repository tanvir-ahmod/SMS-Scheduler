package com.example.smsScheduler.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smsScheduler.databinding.ItemTimelineBinding
import com.example.smsScheduler.utils.Constants
import com.example.smsScheduler.utils.Utils
import java.lang.StringBuilder

class EventAdapter(private val onEditClicked: (id: Int) -> Unit) :
    RecyclerView.Adapter<EventAdapter.TimeLineHolder>() {

    private var eventModel: List<EventModel> = arrayListOf()

    fun addTimeLineData(eventWithSmsAndPhoneNumbers: List<EventModel>) {
        this.eventModel = eventWithSmsAndPhoneNumbers
        notifyDataSetChanged()
    }

    class TimeLineHolder(private val binding: ItemTimelineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            eventModel: EventModel,
            onEditClicked: (id: Int) -> Unit
        ) {
            val numbers = eventModel.receivers
            val phoneNumbers = StringBuilder()
            for ((index, number) in numbers.withIndex()) {
                phoneNumbers.append(number)
                if (index != numbers.size - 1)
                    phoneNumbers.append(", ")
            }
            val message = eventModel.message
            val date =
                Utils.timelineDateFormatter.format(eventModel.timestamp)

            binding.tvTo.text = phoneNumbers
            binding.tvMessage.text = message
            binding.tvDate.text = date
            binding.tvTime.text = Constants.EVENT_STATUS[eventModel.status]

            binding.ivLaunch.setOnClickListener {
                onEditClicked(eventModel.eventID)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineHolder {

        val binding =
            ItemTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeLineHolder(
            binding
        )
    }

    override fun getItemCount(): Int = eventModel.size

    override fun onBindViewHolder(holder: TimeLineHolder, position: Int) {

        val event = eventModel[position]
        holder.bind(event, onEditClicked)
    }
}
package com.example.scheduledmessenger.ui.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledmessenger.data.source.local.entity.EventWithSmsAndPhoneNumbers
import com.example.scheduledmessenger.databinding.ItemTimelineBinding
import com.example.scheduledmessenger.utils.Utils
import java.lang.StringBuilder

class TimelineAdapter :
    RecyclerView.Adapter<TimelineAdapter.TimeLineHolder>() {

    private var eventWithSmsAndPhoneNumbers: List<EventWithSmsAndPhoneNumbers> = arrayListOf()

    fun addTimeLineData(eventWithSmsAndPhoneNumbers: List<EventWithSmsAndPhoneNumbers>) {
        this.eventWithSmsAndPhoneNumbers = eventWithSmsAndPhoneNumbers
        notifyDataSetChanged()
    }

    class TimeLineHolder(private val binding: ItemTimelineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(phoneNumbers: String, message: String, date: String, time: String) {
            binding.tvTo.text = phoneNumbers
            binding.tvMessage.text = message
            binding.tvDate.text = date
            binding.tvTime.text = time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineHolder {

        val binding =
            ItemTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeLineHolder(binding)
    }

    override fun getItemCount(): Int = eventWithSmsAndPhoneNumbers.size

    override fun onBindViewHolder(holder: TimeLineHolder, position: Int) {

        val event = eventWithSmsAndPhoneNumbers[position]
        val smsAndPhoneNumbers = event.smsAndPhoneNumbers
        val phoneNumbers = StringBuilder()
        for ((index, number) in smsAndPhoneNumbers.phoneNumbers.withIndex()) {
            phoneNumbers.append(number.phoneNumber)
            if (index != smsAndPhoneNumbers.phoneNumbers.size - 1)
                phoneNumbers.append(", ")
        }
        val message = smsAndPhoneNumbers.sms.message
        val date = Utils.timelineDateFormatter.format(event.event.timestamp)
        val time = Utils.timeFormatter.format(event.event.timestamp)
        holder.bindData(phoneNumbers.toString(), message, date, time)
    }
}
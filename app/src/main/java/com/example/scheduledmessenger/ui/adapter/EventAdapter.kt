package com.example.scheduledmessenger.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledmessenger.data.source.local.entity.Event
import com.example.scheduledmessenger.databinding.ItemTimelineBinding
import com.example.scheduledmessenger.utils.Constants
import com.example.scheduledmessenger.utils.Utils
import java.lang.StringBuilder

class EventAdapter(private val onEditClicked: (id: Int) -> Unit, private val onDeleteClicked: (eventId : Int) -> Unit) :
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
            onEditClicked: (id: Int) -> Unit,
            onDeleteClicked: (eventId : Int) -> Unit
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

            binding.ivDelete.setOnClickListener {
                onDeleteClicked(eventModel.eventID)
            }

            binding.ivEdit.visibility =
                if (eventModel.status == Constants.SENT)
                    View.GONE else View.VISIBLE


            binding.ivEdit.setOnClickListener {
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
        holder.bind(event, onEditClicked, onDeleteClicked)
    }
}
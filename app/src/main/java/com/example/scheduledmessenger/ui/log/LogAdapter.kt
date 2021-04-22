package com.example.scheduledmessenger.ui.log

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledmessenger.data.source.local.entity.EventLog
import com.example.scheduledmessenger.databinding.ItemLogBinding
import com.example.scheduledmessenger.utils.Constants
import com.example.scheduledmessenger.utils.Utils.logDateFormatter
import com.example.scheduledmessenger.utils.Utils.timeFormatter


class LogAdapter(
    private val onEditClicked: (id: Int) -> Unit
) :
    RecyclerView.Adapter<LogAdapter.LogHolder>() {

    private var logs: List<EventLog> = arrayListOf()

    fun addLogData(logs: List<EventLog>) {
        this.logs = logs
        notifyDataSetChanged()
    }

    class LogHolder(private val binding: ItemLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(
            log: EventLog,
            onEditClicked: (id: Int) -> Unit
        ) {
            val logEventID = "#${log.eventID}"
            val underLinedText = SpannableString(logEventID)
            underLinedText.setSpan(UnderlineSpan(), 1, logEventID.length, 0)

            binding.tvId.text = underLinedText
            binding.tvStatus.text = Constants.LOG_MESSAGE[log.logStatus]!!
            binding.tvTime.text = timeFormatter.format(log.timestamp)
            binding.tvDate.text = logDateFormatter.format(log.timestamp)

            binding.tvId.setOnClickListener {
                onEditClicked(log.eventID)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
        val binding =
            ItemLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogHolder(binding)
    }

    override fun getItemCount(): Int = logs.size

    override fun onBindViewHolder(holder: LogHolder, position: Int) {
        val log = logs[position]
        holder.bindData(log, onEditClicked)
    }
}
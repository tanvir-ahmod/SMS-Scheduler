package com.example.scheduledmessenger.ui.log

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledmessenger.data.source.local.entity.EventLog
import com.example.scheduledmessenger.databinding.ItemLogBinding
import com.example.scheduledmessenger.utils.Constants
import com.example.scheduledmessenger.utils.Utils.logDateFormatter

class LogAdapter :
    RecyclerView.Adapter<LogAdapter.LogHolder>() {

    private var logs: List<EventLog> = arrayListOf()

    fun addLogData(logs: List<EventLog>) {
        this.logs = logs
        notifyDataSetChanged()
    }

    class LogHolder(private val binding: ItemLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(time: String, eventMessage: String) {
            binding.tvTime.text = time
            binding.tvStatus.text = eventMessage
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
        val time = logDateFormatter.format(log.timestamp)
        val status = Constants.LOG_MESSAGE[log.logStatus]!!
        holder.bindData(time, status)
    }
}
package com.dmytrokoniev.holyalarm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R


class AlarmListAdapter : RecyclerView.Adapter<AlarmItemViewHolder>() {
    private var alarmsList: MutableList<AlarmItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmItemViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_alarm, parent, false)

        return AlarmItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmItemViewHolder, position: Int) {
        holder.bind(alarmsList[position])
    }

    override fun getItemCount(): Int = alarmsList.size

    fun setAlarmList(inputList: List<AlarmItem>) {
        alarmsList.clear()
        alarmsList.addAll(inputList)
        notifyDataSetChanged()
    }

    fun addAlarm(inputItem: AlarmItem) {
        alarmsList.add(inputItem)
        notifyItemInserted(alarmsList.lastIndex)
    }
}

data class AlarmItem(
    val time: String,
    val dayPart: String
)

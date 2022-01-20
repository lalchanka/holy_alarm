package com.dmytrokoniev.holyalarm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R
import java.io.Serializable


class AlarmListAdapter: RecyclerView.Adapter<AlarmItemViewHolder>() {
    private var alarmsList: MutableList<AlarmItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmItemViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_alarm, parent, false)

        return AlarmItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmItemViewHolder, position: Int) {
        holder.bind(alarmsList[position])
        holder.enabled.setOnCheckedChangeListener { buttonView, isChecked ->
            // 05.01.2022 dmytrokoniev@gmail.com TODO: <text of todo>
        }
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
    val id: Int = 0,
    val hour: Int = 7,
    val minute: Int = 15,
    val is24HourView: Boolean = true,
    val isEnabled: Boolean = true
): Serializable

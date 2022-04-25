package com.dmytrokoniev.holyalarm.ui

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.versionedparcelable.ParcelField
import com.dmytrokoniev.holyalarm.R
import kotlinx.parcelize.Parcelize
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

@Parcelize
data class AlarmItem(
    val id: String,
    @IntRange(from = 0, to = 23) val hour: Int,
    @IntRange(from = 0, to = 59) val minute: Int,
    val is24HourView: Boolean = true,
    val isEnabled: Boolean = true
): Parcelable

package com.dmytrokoniev.holyalarm.alarmlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.data.AlarmItem


class AlarmListAdapter : RecyclerView.Adapter<AlarmItemViewHolder>() {

    private var alarmsList: MutableList<AlarmItem> = mutableListOf()
    private var launchInFragmentScope: ((suspend () -> Unit) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmItemViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_alarm, parent, false)

        return AlarmItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmItemViewHolder, position: Int) {
        holder.bind(alarmsList[position], requireLaunchInFragmentScope())
    }

    override fun getItemCount(): Int = alarmsList.size

    fun setAlarmList(inputList: List<AlarmItem>) {
        alarmsList.clear()
        alarmsList.addAll(inputList)
        notifyDataSetChanged()
    }

    fun addAlarm(inputItem: AlarmItem, position: Int) {
        alarmsList.add(position, inputItem)
        notifyItemInserted(alarmsList.lastIndex)
    }

    fun getAlarm(index: Int): AlarmItem = alarmsList[index]

    fun removeAlarm(position: Int, onRemoved: ((AlarmItem) -> Unit)? = null) {
        val alarmToRemove = alarmsList[position]
        alarmsList.remove(alarmToRemove)
        notifyItemRemoved(position)
        onRemoved?.invoke(alarmToRemove)
    }

    fun setLaunchInFragmentScope(launch: (suspend () -> Unit) -> Unit) {
        this.launchInFragmentScope = launch
    }

    fun clear() {
        launchInFragmentScope = null
    }

    private fun requireLaunchInFragmentScope(): (suspend () -> Unit) -> Unit =
        launchInFragmentScope ?: throw IllegalStateException(
            "Accessing launchInFragmentScope outside of Fragment lifecycle"
        )
}

package com.dmytrokoniev.holyalarm.ui

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R
import kotlinx.parcelize.Parcelize
import java.util.*


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

    fun addAlarm(position: Int, inputItem: AlarmItem) {
        alarmsList.add(position, inputItem)
        notifyItemInserted(alarmsList.lastIndex)
    }

    fun getAlarm(index: Int): AlarmItem = alarmsList[index]

    fun removeAlarm(
        position: Int
        /***, viewHolder: RecyclerView.ViewHolder***/
    ) {
        var removedItem = alarmsList[position]

        alarmsList.removeAt(position)
        notifyItemRemoved(position)

//        Snackbar.make(viewHolder.itemView, "${removedItem.name?.get("En")} removed", Snackbar.LENGTH_LONG).setAction("UNDO") {
//            products.add(removedPosition, removedItem)
//            notifyItemInserted(removedPosition)
//
//        }.show()
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

@Parcelize
data class AlarmItem(
    val id: String,
    @IntRange(from = 0, to = 23) val hour: Int,
    @IntRange(from = 0, to = 59) val minute: Int,
    val is24HourView: Boolean = true,
    val isEnabled: Boolean = true
) : Parcelable {

    companion object {
        fun AlarmItem.toMillis(): Long {
            val date = Date()
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            return calendar.timeInMillis
        }
    }
}

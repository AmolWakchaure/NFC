package sns.sn.systems.patient.dosagehistory.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import sns.sn.systems.R
import sns.sn.systems.patient.dosagehistory.pojo.DosageDetailsTimeStamp
import sns.sn.systems.patient.dosagehistory.view.DosageHistoryFragment

class DosageDetailsTimeStampHistoryAdapter (val userList: ArrayList<DosageDetailsTimeStamp>) : RecyclerView.Adapter<DosageDetailsTimeStampHistoryAdapter.ViewHolder>()
{
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dosage_details_history_row, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int
    {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        fun bindItems(user: DosageDetailsTimeStamp)
        {
            val dosageNameTxt = itemView.findViewById(R.id.dosageNameTxt) as TextView
            val dateTxt = itemView.findViewById(R.id.dateTxt) as TextView
            val timeTxt = itemView.findViewById(R.id.timeTxt) as TextView

            dosageNameTxt.text = user.dosageName
            dateTxt.text = user.dosageDate
            timeTxt.text = user.dosageTime


        }


    }
}
package sns.sn.systems.patient.dosagehistory.adapter

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import sns.sn.systems.R
import sns.sn.systems.patient.consumption.view.fragment.PillConsumptionFragment
import sns.sn.systems.patient.dosagehistory.pojo.DosageDetails
import sns.sn.systems.patient.dosagehistory.view.DosageHistoryFragment

class DosageHistoryAdapter (val userList: ArrayList<DosageDetails>, val fragment : DosageHistoryFragment) : RecyclerView.Adapter<DosageHistoryAdapter.ViewHolder>()
{
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dosage_history_row, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindItems(userList[position],fragment)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int
    {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        fun bindItems(user: DosageDetails,fragment : DosageHistoryFragment)
        {
            val dosageNameTxt = itemView.findViewById(R.id.dosageNameTxt) as TextView
            val dosageItemLi = itemView.findViewById(R.id.dosageItemLi) as LinearLayout

            dosageNameTxt.text = user.dosageName

            dosageItemLi.setOnClickListener {

                fragment.callDosageDetailsFragment()

            }

        }


    }
}
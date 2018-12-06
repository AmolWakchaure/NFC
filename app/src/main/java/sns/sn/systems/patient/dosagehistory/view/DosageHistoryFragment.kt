package sns.sn.systems.patient.dosagehistory.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_dosage_history.*
import sns.sn.systems.R
import sns.sn.systems.classes.MyApplication
import sns.sn.systems.patient.consumption.view.fragment.PillConsumptionFragment
import sns.sn.systems.patient.dosagehistory.adapter.DosageHistoryAdapter
import sns.sn.systems.patient.dosagehistory.pojo.DosageDetails

class DosageHistoryFragment : Fragment()
{
    companion object
    {
        val DOSAGE_HISTORY = ArrayList<DosageDetails>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return  inflater?.inflate(R.layout.fragment_dosage_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {

        DOSAGE_HISTORY.add(DosageDetails("Dosage 1"))
        DOSAGE_HISTORY.add(DosageDetails("Dosage 2"))
        DOSAGE_HISTORY.add(DosageDetails("Dosage 3"))
        DOSAGE_HISTORY.add(DosageDetails("Dosage 4"))
        DOSAGE_HISTORY.add(DosageDetails("Dosage 5"))
        DOSAGE_HISTORY.add(DosageDetails("Dosage 6"))
        DOSAGE_HISTORY.add(DosageDetails("Dosage 7"))
        DOSAGE_HISTORY.add(DosageDetails("Dosage 8"))
        DOSAGE_HISTORY.add(DosageDetails("Dosage 9"))
        DOSAGE_HISTORY.add(DosageDetails("Dosage 10"))
        DOSAGE_HISTORY.add(DosageDetails("Dosage 11"))

        dosageHistory.layoutManager = LinearLayoutManager(MyApplication.context, LinearLayout.VERTICAL, false)
        val adapter = DosageHistoryAdapter(DOSAGE_HISTORY,this);
        dosageHistory.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun callDosageDetailsFragment()
    {
        var  dosageHistoryDetailsFragment = DosageHistoryDetailsFragment();
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.container, dosageHistoryDetailsFragment).commit()
    }

}
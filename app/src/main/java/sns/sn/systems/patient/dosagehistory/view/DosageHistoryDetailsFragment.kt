package sns.sn.systems.patient.dosagehistory.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_dosage_details_history.*
import kotlinx.android.synthetic.main.fragment_dosage_history.*
import sns.sn.systems.R
import sns.sn.systems.classes.MyApplication
import sns.sn.systems.patient.dosagehistory.adapter.DosageDetailsTimeStampHistoryAdapter
import sns.sn.systems.patient.dosagehistory.pojo.DosageDetailsTimeStamp

class DosageHistoryDetailsFragment : Fragment()
{

    companion object
    {
        val DOSAGE_HISTORY = ArrayList<DosageDetailsTimeStamp>()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return  inflater?.inflate(R.layout.fragment_dosage_details_history, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {

        DOSAGE_HISTORY.add(DosageDetailsTimeStamp("Pill 1 removed","23 Nov 2018","05:38 PM"))
        DOSAGE_HISTORY.add(DosageDetailsTimeStamp("Pill 2 removed","23 Nov 2018","05:38 PM"))
        DOSAGE_HISTORY.add(DosageDetailsTimeStamp("Pill 3 removed","23 Nov 2018","05:38 PM"))
        DOSAGE_HISTORY.add(DosageDetailsTimeStamp("Pill 4 removed","23 Nov 2018","05:38 PM"))
        DOSAGE_HISTORY.add(DosageDetailsTimeStamp("Pill 5 removed","23 Nov 2018","05:38 PM"))
        DOSAGE_HISTORY.add(DosageDetailsTimeStamp("Pill 6 removed","23 Nov 2018","05:38 PM"))
        DOSAGE_HISTORY.add(DosageDetailsTimeStamp("Pill 7 removed","23 Nov 2018","05:38 PM"))
        DOSAGE_HISTORY.add(DosageDetailsTimeStamp("Pill 8 removed","23 Nov 2018","05:38 PM"))
        DOSAGE_HISTORY.add(DosageDetailsTimeStamp("Pill 9 removed","23 Nov 2018","05:38 PM"))
        DOSAGE_HISTORY.add(DosageDetailsTimeStamp("Pill 10 removed","23 Nov 2018","05:38 PM"))

        dosageDetailsHistory.layoutManager = LinearLayoutManager(MyApplication.context, LinearLayout.VERTICAL, false)
        val adapter = DosageDetailsTimeStampHistoryAdapter(DOSAGE_HISTORY);
        dosageDetailsHistory.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}
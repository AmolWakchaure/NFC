package sns.sn.systems.patient.consumption.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sns.sn.systems.R

class PillConsumptionFragment : Fragment()
{
    /*companion object
    {
        val VEHICLE_DATA = ArrayList<DosageDetails>()
        lateinit var  rootView : View
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return  inflater?.inflate(R.layout.fragment_pill_consumption, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {

    }

}
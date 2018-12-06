package sns.sn.systems.pharmacist.prescription.model

import android.databinding.BaseObservable

class ReadWritePrescriptionModel (private var prescriptionString: String) : BaseObservable()
{
    fun setPrescriptionString(prescriptionString : String)
    {
        this.prescriptionString = prescriptionString
    }
    fun getPrescriptionString() : String?
    {
        return prescriptionString
    }
}
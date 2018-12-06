package sns.sn.systems.pharmacist.prescription.viewmodel

interface ReadWritePrescCallbacks
{
    fun writePrescriptionSuccess(message : String)
    fun writePrescriptionFailure(message : String)
    fun readPrescriptionSuccess(message : String)
    fun readPrescriptionFailure(message : String)
    fun notifyUser(message : String)

}
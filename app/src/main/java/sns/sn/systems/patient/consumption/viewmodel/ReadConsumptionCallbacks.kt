package sns.sn.systems.patient.consumption.viewmodel

interface ReadConsumptionCallbacks
{
    fun readConsumptionSuccess(message : String)
    fun readConsumptionFailure(message : String)
}
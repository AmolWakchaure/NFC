package sns.sn.systems.pharmacist.consumption.viewmodel

interface ReadConsumptionCallbacks
{
    fun readConsumptionSuccess(message : String)
    fun readConsumptionFailure(message : String)
}
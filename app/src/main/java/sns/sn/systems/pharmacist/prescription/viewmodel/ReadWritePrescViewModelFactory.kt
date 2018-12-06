package sns.sn.systems.pharmacist.prescription.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import sns.sn.systems.authenticate.interfaces.LoginResultCallbacks
import sns.sn.systems.authenticate.viewmodel.LoginViewModel

class ReadWritePrescViewModelFactory (private val listner : ReadWritePrescCallbacks) : ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        return ReadWritePrescViewModel (listner) as T
    }
}
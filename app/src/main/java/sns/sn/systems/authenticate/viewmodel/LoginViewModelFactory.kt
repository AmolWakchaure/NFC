package sns.sn.systems.authenticate.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import sns.sn.systems.authenticate.interfaces.LoginResultCallbacks

class LoginViewModelFactory (private val listner : LoginResultCallbacks) : ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        return LoginViewModel (listner) as T
    }
}
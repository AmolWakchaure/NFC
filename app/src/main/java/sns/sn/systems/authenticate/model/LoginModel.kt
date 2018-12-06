package sns.sn.systems.authenticate.model

import android.databinding.BaseObservable

class LoginModel (private var username : String) : BaseObservable()
{


    fun setUsername(username : String)
    {
        this.username = username

    }
    fun getUsername(): String?
    {
        return username

    }
}
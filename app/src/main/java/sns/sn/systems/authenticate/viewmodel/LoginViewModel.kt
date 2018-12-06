package sns.sn.systems.authenticate.viewmodel

import android.arch.lifecycle.ViewModel
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import sns.sn.systems.R
import sns.sn.systems.authenticate.interfaces.LoginResultCallbacks
import sns.sn.systems.authenticate.model.LoginModel
import sns.sn.systems.classes.F
import sns.sn.systems.classes.V

class LoginViewModel (private val listner : LoginResultCallbacks) : ViewModel()
{

    private val loginModel : LoginModel
    init
    {
        this.loginModel = LoginModel("")
    }
    //create function to set username after use finish enter text
    val usernameTextWatcher : TextWatcher
        get() = object : TextWatcher
        {
            override fun afterTextChanged(username: Editable?)
            {
                loginModel.setUsername(username.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {

            }
        }
    //create function to process login button  click
    fun onLoginClicked(v: View)
    {
        if(!V.isCheckEmpty(loginModel.getUsername(),"Warning ! enter username"))
            return

        if(loginModel.getUsername().equals("sns"))
        {
            listner.onSuccess("pharmacist", loginModel.getUsername()!!,"NA")
        }
        else if(loginModel.getUsername().equals("sns123"))
        {
            listner.onSuccess("patient",loginModel.getUsername()!!,"NA")
        }
        else
        {
            listner.onFailure("Login fail")
        }
        /*if(F.isNetworkAvailable())
        {
            proceedLogin()
        }
        else
        {
            F.t(""+ R.string.connection)
        }*/

    }
}
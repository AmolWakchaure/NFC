package sns.sn.systems.authenticate.view

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_login.*
import sns.sn.systems.R
import sns.sn.systems.authenticate.interfaces.LoginResultCallbacks
import sns.sn.systems.authenticate.viewmodel.LoginViewModel
import sns.sn.systems.authenticate.viewmodel.LoginViewModelFactory
import sns.sn.systems.classes.F
import sns.sn.systems.classes.MyApplication
import sns.sn.systems.databinding.ActivityLoginBinding
import sns.sn.systems.forgotpass.view.ForgotPasswordActivity
import sns.sn.systems.interfaces.Constants
import sns.sn.systems.patient.consumption.view.activity.PatientHomeActivity
import sns.sn.systems.pharmacist.homepage.view.HomeActivity


class LoginActivity : AppCompatActivity(),LoginResultCallbacks {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        val activityLogin = DataBindingUtil.setContentView<ActivityLoginBinding>(this,R.layout.activity_login)
        //insatantiate new ViewModels
        activityLogin.loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        //setClickListner()
    }

    /*private fun setClickListner() {

        buttonLogin.setOnClickListener {

            startActivity(Intent(this,HomeActivity::class.java))
        }
        layoutForgotPass.setOnClickListener {

            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
    }*/

    override fun onSuccess(userType: String,userName : String,userPassword : String)
    {
       if(userType.equals("pharmacist"))
       {
           MyApplication.editor.putString(Constants.USER_TYPE,userType)
           MyApplication.editor.putString(Constants.USER_NAME,userName)
           MyApplication.editor.putString(Constants.USER_PASSWORD,userPassword)
           MyApplication.editor.commit()

           val intent = Intent(this, HomeActivity::class.java)
           startActivity(intent)
           finish()
       }
       else if(userType.equals("patient"))
       {
           MyApplication.editor.putString(Constants.USER_TYPE,userType)
           MyApplication.editor.putString(Constants.USER_NAME,userName)
           MyApplication.editor.putString(Constants.USER_PASSWORD,userPassword)
           MyApplication.editor.commit()

           val intent = Intent(this, PatientHomeActivity::class.java)
           startActivity(intent)
           finish()
       }
       else
       {
           F.t("Invalid login")
       }
    }

    override fun onFailure(message: String)
    {
        F.t(message)
    }
}

package sns.sn.systems.splash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import sns.sn.systems.R
import sns.sn.systems.authenticate.view.LoginActivity
import sns.sn.systems.classes.MyApplication
import sns.sn.systems.interfaces.Constants
import sns.sn.systems.patient.consumption.view.activity.PatientHomeActivity
import sns.sn.systems.pharmacist.homepage.view.HomeActivity

class SplashActivity : AppCompatActivity()
{
    private lateinit var mdelayHandler : Handler
    private var splashDelay : Long = Constants.SPLASH_DELAY//3 sec

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //initialise the handler
        mdelayHandler = Handler()
        //navigate with delay
        mdelayHandler.postDelayed(mRunnable,splashDelay)
    }
    internal  val mRunnable : Runnable = Runnable {

        if(!isFinishing)
        {

            val USER_NAME = MyApplication.prefs.getString(Constants.USER_NAME,"0")
            val USER_TYPE = MyApplication.prefs.getString(Constants.USER_TYPE,"0")

            if(USER_NAME.equals("0"))
            {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                if(USER_TYPE.equals("pharmacist"))
                {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else if(USER_TYPE.equals("patient"))
                {
                    val intent = Intent(this, PatientHomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

        }
    }
    override fun onDestroy()
    {
        super.onDestroy()
        mdelayHandler.removeCallbacks(mRunnable)

    }
}

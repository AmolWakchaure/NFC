package sns.sn.systems.classes

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.support.multidex.MultiDex
import sns.sn.systems.interfaces.Constants

class MyApplication : Application()
{
    companion object
    {

        lateinit var context: Context
        lateinit var prefs : SharedPreferences
        lateinit var editor : SharedPreferences.Editor
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    override fun onCreate()
    {
        super.onCreate()
        context = applicationContext

        //shared preferences
        prefs = getSharedPreferences(Constants.PREF_KEY,0)
        editor = prefs.edit()
        editor.commit()

    }
}
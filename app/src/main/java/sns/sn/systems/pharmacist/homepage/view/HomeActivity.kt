package sns.sn.systems.pharmacist.homepage.view

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import sns.sn.systems.R
import sns.sn.systems.authenticate.view.LoginActivity
import sns.sn.systems.classes.F
import sns.sn.systems.classes.MyApplication
import sns.sn.systems.pharmacist.consumption.view.ReadConsumptionActivity
import sns.sn.systems.dialog.CustomAlertDialog
import sns.sn.systems.dialog.DialogClickInterface
import sns.sn.systems.pharmacist.prescription.view.ReadWritePrescriptionActivity
import sns.sn.systems.profile.ProfileActivity

class HomeActivity : AppCompatActivity(), DialogClickInterface {


    private val identifier = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setClickListner()
    }

    private fun setClickListner() {

        readPrescLi.setOnClickListener {

            startActivity(Intent(this, ReadWritePrescriptionActivity::class.java))
        }
        readConLi.setOnClickListener {

            startActivity(Intent(this, ReadConsumptionActivity::class.java))
        }
        profileLi.setOnClickListener {

            startActivity(Intent(this, ProfileActivity::class.java))
        }
        logoutLi.setOnClickListener {

            CustomAlertDialog.instance.showConfirmDialog("Logout", "Do you want to logout app?", "Logout", "Cancel", this, identifier);
        }
    }
    override fun onClickPositiveButton(pDialog: DialogInterface, pDialogIntefier: Int)
    {

        if (pDialogIntefier == 0)
        {
            MyApplication.editor.clear()
            MyApplication.editor.commit()
            F.t("Successfully logout")
            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            finish()
        }
        pDialog.dismiss()
    }

    override fun onClickNegativeButton(pDialog: DialogInterface, pDialogIntefier: Int)
    {
        if (pDialogIntefier == 0)
            F.t("Cancel")
        pDialog.dismiss()
    }


}

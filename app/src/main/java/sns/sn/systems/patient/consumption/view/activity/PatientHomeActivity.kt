package sns.sn.systems.patient.consumption.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import sns.sn.systems.R
import kotlinx.android.synthetic.main.activity_patient_pill_consumtion.*
import kotlinx.android.synthetic.main.menu_left_drawer.*
import sns.sn.systems.authenticate.view.LoginActivity
import sns.sn.systems.classes.F
import sns.sn.systems.classes.MyApplication
import sns.sn.systems.dialog.CustomAlertDialog
import sns.sn.systems.dialog.DialogClickInterface
import sns.sn.systems.encryption.Encrypt
import sns.sn.systems.patient.dosagehistory.view.DosageHistoryFragment
import sns.sn.systems.patient.consumption.view.fragment.PillConsumptionFragment
import sns.sn.systems.patient.navdrawer.library.SimpleItem
import sns.sn.systems.patient.navdrawer.library.SlidingRootNav
import sns.sn.systems.patient.navdrawer.library.SlidingRootNavBuilder
import sns.sn.systems.patient.navdrawer.menu.DrawerAdapter
import sns.sn.systems.patient.navdrawer.menu.DrawerItem
import java.util.*

class PatientHomeActivity : AppCompatActivity() ,DrawerAdapter.OnItemSelectedListener, DialogClickInterface
{
    private val POS_HOME = 0
    private val POS_DOSAGE_HISTORY = 1
    private val POS_LOGOUT = 2

    private val identifier = 0
    private var screenTitles: Array<String>? = null
    private lateinit var screenIcons: Array<Drawable?>

    private var slidingRootNav: SlidingRootNav? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_pill_consumtion)


        //Encrypt.processEncryption()


        setSupportActionBar(toolbar)

        slidingRootNav = SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject()

        screenIcons = loadScreenIcons()
        screenTitles = loadScreenTitles()

        val adapter = DrawerAdapter(Arrays.asList(
                createItemFor(POS_HOME).setChecked(true),
                createItemFor(POS_DOSAGE_HISTORY),
                createItemFor(POS_LOGOUT)) as List<DrawerItem<DrawerAdapter.ViewHolder>>?)
        adapter.setListener(this)

        list.setNestedScrollingEnabled(false)
        list.setLayoutManager(LinearLayoutManager(this))
        list.setAdapter(adapter)
        adapter.setSelected(POS_HOME)
    }

    private fun createItemFor(position: Int): DrawerItem<*>
    {


        return SimpleItem(screenIcons[position], screenTitles!![position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent))
    }
    override fun onItemSelected(position: Int)
    {

        if(position == POS_HOME)
        {
            slidingRootNav!!.closeMenu()
            var  pillConsumptionFragment = PillConsumptionFragment();
            supportFragmentManager.beginTransaction().replace(R.id.container, pillConsumptionFragment).commit()

        }
        else if(position == POS_DOSAGE_HISTORY)
        {
            slidingRootNav!!.closeMenu()
            var  dosageHistoryFragment = DosageHistoryFragment();
            supportFragmentManager.beginTransaction().replace(R.id.container, dosageHistoryFragment).commit()
        }
        else if (position == POS_LOGOUT)
        {
            CustomAlertDialog.instance.showConfirmDialog("Logout", "Do you want to logout app?", "Logout", "Cancel", this, identifier)
        }

    }

    private fun loadScreenIcons(): Array<Drawable?>
    {

        val ta = resources.obtainTypedArray(R.array.ld_activityScreenIcons)
        val icons = arrayOfNulls<Drawable>(ta.length())
        for (i in 0 until ta.length()) {
            val id = ta.getResourceId(i, 0)
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id)
            }
        }
        ta.recycle()
        return icons
    }

    private fun loadScreenTitles(): Array<String> {
        return resources.getStringArray(R.array.ld_activityScreenTitles)
    }

    @ColorInt
    private fun color(@ColorRes res: Int): Int {
        return ContextCompat.getColor(this, res)
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

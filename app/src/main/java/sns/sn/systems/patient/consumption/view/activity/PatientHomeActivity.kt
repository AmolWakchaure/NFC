package sns.sn.systems.patient.consumption.view.activity

import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import sns.sn.systems.R
import kotlinx.android.synthetic.main.activity_patient_pill_consumtion.*

import kotlinx.android.synthetic.main.menu_left_drawer.*
import sns.sn.systems.authenticate.view.LoginActivity
import sns.sn.systems.classes.F
import sns.sn.systems.classes.M
import sns.sn.systems.classes.MyApplication
import sns.sn.systems.dialog.CustomAlertDialog
import sns.sn.systems.dialog.DialogClickInterface
import sns.sn.systems.encryption.Encrypt
import sns.sn.systems.patient.dosagehistory.view.DosageHistoryFragment
import sns.sn.systems.patient.consumption.view.fragment.PillConsumptionFragment
import sns.sn.systems.patient.consumption.viewmodel.ReadConsumptionCallbacks
import sns.sn.systems.patient.navdrawer.library.SimpleItem
import sns.sn.systems.patient.navdrawer.library.SlidingRootNav
import sns.sn.systems.patient.navdrawer.library.SlidingRootNavBuilder
import sns.sn.systems.patient.navdrawer.menu.DrawerAdapter
import sns.sn.systems.patient.navdrawer.menu.DrawerItem
import java.lang.Exception
import java.util.*

class PatientHomeActivity : AppCompatActivity() ,DrawerAdapter.OnItemSelectedListener, DialogClickInterface, ReadConsumptionCallbacks
{
    private val POS_HOME = 0
    private val POS_DOSAGE_HISTORY = 1
    private val POS_LOGOUT = 2

    private val identifier = 0
    private var screenTitles: Array<String>? = null
    private lateinit var screenIcons: Array<Drawable?>

    private var slidingRootNav: SlidingRootNav? = null

    companion object
    {
        // var ndef: Ndef? = null
        var nfcAdapter: NfcAdapter? = null
        // var pendingIntent : PendingIntent? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_pill_consumtion)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

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
    override fun onPause()
    {
        // nfcAdapter?.disableForegroundDispatch(this)
        nfcAdapter?.disableForegroundDispatch(this)
        super.onPause()

    }
    override fun onResume()
    {
        super.onResume()
        //nfcAdapter?.enableForegroundDispatch(this, pendingIntent,null,null)
        enableForgroundDispatchSystem()
    }

    private fun enableForgroundDispatchSystem() {
        val intent = Intent(this, PatientHomeActivity::class.java).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val intentFilters = arrayOf<IntentFilter>()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilters, null)
    }

    var encryptedString : String? = null
    var pillBoardId : String? = null
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        try
        {
            if(intent.hasExtra(NfcAdapter.EXTRA_TAG))
            {

                val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
                //val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_NDEF_MESSAGES)

                if (parcelables != null && parcelables.size > 0)
                {
                    var ndefMessage = parcelables.get(0) as NdefMessage
                    val ndefRecords = ndefMessage.getRecords()

                    if(ndefRecords != null && ndefRecords.size > 0)
                    {

                        var receivedString = M.bytesToHex(ndefRecords.get(1).payload)
                        Log.e("NDEF_RESPONSE", "ndefRecord : "+receivedString)
                        //check therapy started or not
                        if(receivedString.length == 10)
                        {

                            var atttheRateCheck = receivedString.substring(6,10)
                            //Log.e("NDEF_RESPONSE", "atttheRateCheck : " + atttheRateCheck)
                            if(atttheRateCheck.equals("4040"))//@@
                            {
                                F.t("Therapy not started yet.")
                            }

                        }
                        else
                        {
                            encryptedString = receivedString.substring(6)

                            if(encryptedString != null || encryptedString != "")
                            {
                                Log.e("NDEF_RESPONSE", "encryptedString : " + encryptedString)
                                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                                pillBoardId = M.bytesToHex(tag.id)
                                // pillBoardId = "NA"
                                Log.e("NDEF_RESPONSE", "pillBoardId : " + pillBoardId)
                                //check all pills consume or not
                                if(encryptedString!!.substring(0,4).equals("2323"))//##
                                {
                                    F.t("All pill are consumed")
                                    var  consumedString = encryptedString!!.substring(4)
                                    checkGetRemovals(consumedString,pillBoardId!!)
                                }

                                if(encryptedString!!.substring(0,4).equals("2424"))//$$
                                {
                                    F.t("Pill pack not connected, connect the pill pack")
                                    var  consumedString = encryptedString!!.substring(4)
                                    checkGetRemovals(consumedString,pillBoardId!!)
                                }
                                else
                                {
                                    checkGetRemovals(encryptedString!!,pillBoardId!!)
                                }
                            }

                        }
                    }
                    else
                    {
                        F.t("No NDEF records found");
                    }
                }
                else
                {
                    F.t("No NDEF messages found");
                }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }

    }

    fun checkGetRemovals(encryptedString: String,pillBoardId: String)
    {
        val ENCRYPTED_STRINGS = Encrypt.devideStringIntoEqualPart(encryptedString)
        /*for(i in 0 until ENCRYPTED_STRINGS.size)
        {
            Log.e("NDEF_RESPONSE","ENCRYPTED_STRINGS index "+i+" Strings : "+ENCRYPTED_STRINGS.get(i))
        }*/
        val REMOVALS_STRINGS = ArrayList<String>()

        for(i in 0 until ENCRYPTED_STRINGS.size)
        {
            var removalsString = Encrypt.processEncryption(ENCRYPTED_STRINGS.get(i))
            REMOVALS_STRINGS.add(removalsString)
        }

        /*for(i in 0 until REMOVALS_STRINGS.size)
        {
            Log.e("NDEF_RESPONSE","REMOVALS_STRINGS index "+i+" Strings : "+REMOVALS_STRINGS.get(i))
        }*/

        for(i in 0 until REMOVALS_STRINGS.size)
        {
            var pillData = REMOVALS_STRINGS.get(i).split("#")

            val pillDateTime = pillData[0]
            val pillGroup = pillData[1].replace("0","")
            val pillPosition = pillData[2].replace("0","")

            Log.e("NDEF_RESPONSE","pillData : "+pillData)

            Log.e("NDEF_RESPONSE","pillGroup : "+pillGroup+" pillPosition : "+pillPosition)


            if (pillGroup.equals("1")) {

                if (pillPosition.equals("1")) {
                    groupOneOne.setVisibility(View.GONE)
                    groupOneOneButton.setVisibility(View.VISIBLE)
                    groupOneOneTime.setText(pillDateTime)
                }
                if (pillPosition.equals("2")) {
                    groupOneTwo.setVisibility(View.GONE)
                    groupOneTwoButton.setVisibility(View.VISIBLE)
                    groupOneTwoeTime.setText(pillDateTime)
                }
                if (pillPosition.equals("3")) {
                    groupOneThree.setVisibility(View.GONE)
                    groupOneThreeButton.setVisibility(View.VISIBLE)
                    groupOneThreeTime.setText(pillDateTime)
                }
                if (pillPosition.equals("4")) {
                    groupOneFour.setVisibility(View.GONE)
                    groupOneFourButton.setVisibility(View.VISIBLE)
                    groupOneFourTime.setText(pillDateTime)
                }
            }
            if (pillGroup == "2") {
                if (pillPosition == "1") {
                    groupTwoOne.setVisibility(View.GONE)
                    groupTwoOneButton.setVisibility(View.VISIBLE)
                    groupTwoOneTime.setText(pillDateTime)
                }
                if (pillPosition == "2") {
                    groupTwoTwo.setVisibility(View.GONE)
                    groupTwoTwoButton.setVisibility(View.VISIBLE)
                    groupTwoTwoTime.setText(pillDateTime)
                }
                if (pillPosition == "3") {
                    groupTwoThree.setVisibility(View.GONE)
                    groupTwoThreeButton.setVisibility(View.VISIBLE)
                    groupTwoThreeTime.setText(pillDateTime)
                }
                if (pillPosition == "4") {
                    groupTwoFour.setVisibility(View.GONE)
                    groupTwoFourButton.setVisibility(View.VISIBLE)
                    groupTwoFourTime.setText(pillDateTime)
                }
            }
            if (pillGroup == "3") {
                if (pillPosition == "1") {
                    groupThreeOne.setVisibility(View.GONE)
                    groupThreeOneButton.setVisibility(View.VISIBLE)
                    groupThreeOneTime.setText(pillDateTime)
                }
                if (pillPosition == "2") {
                    groupThreeTwo.setVisibility(View.GONE)
                    groupThreeTwoButton.setVisibility(View.VISIBLE)
                    groupThreeTwoTime.setText(pillDateTime)
                }
                if (pillPosition == "3") {
                    groupThreeThree.setVisibility(View.GONE)
                    groupThreeThreeButton.setVisibility(View.VISIBLE)
                    groupThreeThreeTime.setText(pillDateTime)
                }
                if (pillPosition == "4") {
                    groupThreeFour.setVisibility(View.GONE)
                    groupThreeFourButton.setVisibility(View.VISIBLE)
                    groupThreeFourTime.setText(pillDateTime)
                }
            }
            if (pillGroup == "4") {
                if (pillPosition == "1") {
                    groupFourOne.setVisibility(View.GONE)
                    groupFourOneButton.setVisibility(View.VISIBLE)
                    groupFourOneTime.setText(pillDateTime)
                }
                if (pillPosition == "2") {
                    groupFourTwo.setVisibility(View.GONE)
                    groupFourTwoButton.setVisibility(View.VISIBLE)
                    groupFourTwoTime.setText(pillDateTime)
                }
                if (pillPosition == "3") {
                    groupFourThree.setVisibility(View.GONE)
                    groupFourThreeButton.setVisibility(View.VISIBLE)
                    groupFourThreeTime.setText(pillDateTime)
                }
                if (pillPosition == "4") {
                    groupFourFour.setVisibility(View.GONE)
                    groupFourFourButton.setVisibility(View.VISIBLE)
                    groupFourFourTime.setText(pillDateTime)
                }
            }
            if (pillGroup == "5") {
                if (pillPosition == "1") {
                    groupFiveOne.setVisibility(View.GONE)
                    groupFiveOneButton.setVisibility(View.VISIBLE)
                    groupFiveOneTime.setText(pillDateTime)
                }
                if (pillPosition == "2") {
                    groupFiveTwo.setVisibility(View.GONE)
                    groupFiveTwoButton.setVisibility(View.VISIBLE)
                    groupFiveTwoTime.setText(pillDateTime)
                }
                if (pillPosition == "3") {
                    groupFiveThree.setVisibility(View.GONE)
                    groupFiveThreeButton.setVisibility(View.VISIBLE)
                    groupFiveThreeTime.setText(pillDateTime)
                }
                if (pillPosition == "4") {
                    groupFiveFour.setVisibility(View.GONE)
                    groupFiveFourButton.setVisibility(View.VISIBLE)
                    groupFiveFourTime.setText(pillDateTime)
                }
            }
            if (pillGroup == "6") {
                if (pillPosition == "1") {
                    groupSixOne.setVisibility(View.GONE)
                    groupSixOneButton.setVisibility(View.VISIBLE)
                    groupSixOneTime.setText(pillDateTime)
                }
                if (pillPosition == "2") {
                    groupSixTwo.setVisibility(View.GONE)
                    groupSixTwoButton.setVisibility(View.VISIBLE)
                    groupSixTwoTime.setText(pillDateTime)
                }
                if (pillPosition == "3") {
                    groupSixThree.setVisibility(View.GONE)
                    groupSixThreeButton.setVisibility(View.VISIBLE)
                    groupSixThreeTime.setText(pillDateTime)
                }
                if (pillPosition == "4") {
                    groupSixFour.setVisibility(View.GONE)
                    groupSixFourButton.setVisibility(View.VISIBLE)
                    groupSixFourTime.setText(pillDateTime)
                }
            }
            if (pillGroup == "7") {
                if (pillPosition == "1") {
                    groupSevenOne.setVisibility(View.GONE)
                    groupSevenOneButton.setVisibility(View.VISIBLE)
                    groupSevenOneTime.setText(pillDateTime)
                }
                if (pillPosition == "2") {
                    groupSevenTwo.setVisibility(View.GONE)
                    groupSevenTwoButton.setVisibility(View.VISIBLE)
                    groupSevenTwoTime.setText(pillDateTime)
                }
                if (pillPosition == "3") {
                    groupSevenThree.setVisibility(View.GONE)
                    groupSevenThreeButton.setVisibility(View.VISIBLE)
                    groupSevenThreeTime.setText(pillDateTime)
                }
                if (pillPosition == "4") {
                    groupSevenFour.setVisibility(View.GONE)
                    groupSevenFourButton.setVisibility(View.VISIBLE)
                    groupSevenFourTime.setText(pillDateTime)
                }
            }
        }
    }
    override fun readConsumptionSuccess(message: String)
    {

    }

    override fun readConsumptionFailure(message: String)
    {

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

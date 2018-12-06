package sns.sn.systems.pharmacist.consumption.view

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareUltralight
import android.nfc.tech.Ndef
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_read_consumption.*
import sns.sn.systems.R
import sns.sn.systems.authenticate.view.LoginActivity
import sns.sn.systems.classes.F
import sns.sn.systems.classes.M
import sns.sn.systems.classes.MyApplication
import sns.sn.systems.databinding.ActivityReadConsumptionBinding
import sns.sn.systems.databinding.ActivityReadWritePrescriptionBinding
import sns.sn.systems.encryption.Encrypt
import sns.sn.systems.interfaces.Constants
import sns.sn.systems.patient.consumption.view.activity.PatientHomeActivity
import sns.sn.systems.pharmacist.consumption.model.Patient
import sns.sn.systems.pharmacist.consumption.searchablespinner.BaseSearchDialogCompat
import sns.sn.systems.pharmacist.consumption.searchablespinner.SearchResultListener
import sns.sn.systems.pharmacist.consumption.searchablespinner.SimpleSearchDialogCompat
import sns.sn.systems.pharmacist.consumption.viewmodel.ReadConsumptionCallbacks
import sns.sn.systems.pharmacist.homepage.view.HomeActivity
import sns.sn.systems.pharmacist.prescription.view.ReadWritePrescriptionActivity
import java.io.IOException
import java.lang.Exception
import java.util.ArrayList
import kotlin.experimental.and

class ReadConsumptionActivity : AppCompatActivity(),ReadConsumptionCallbacks {


    internal var VEHICLE_NUMBER = ArrayList<Patient>()
    companion object
    {
       // var ndef: Ndef? = null
        var nfcAdapter: NfcAdapter? = null
       // var pendingIntent : PendingIntent? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_consumption)
        //val activityLogin = DataBindingUtil.setContentView<ActivityReadConsumptionBinding>(this,R.layout.activity_read_consumption)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
       // pendingIntent = PendingIntent.getActivity(this,0, Intent(this, ReadConsumptionActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0)

        setClickListner()


        VEHICLE_NUMBER.add(Patient("Amol Wakchaure"))
        VEHICLE_NUMBER.add(Patient("Gokul Wakchaure"))
        VEHICLE_NUMBER.add(Patient("Sandip Bhagwat"))
        VEHICLE_NUMBER.add(Patient("Vinayak"))
        VEHICLE_NUMBER.add(Patient("Kunal"))
        VEHICLE_NUMBER.add(Patient("Amol Wakchaure"))
        VEHICLE_NUMBER.add(Patient("Gokul Wakchaure"))
        VEHICLE_NUMBER.add(Patient("Sandip Bhagwat"))
        VEHICLE_NUMBER.add(Patient("Vinayak"))
        VEHICLE_NUMBER.add(Patient("Kunal"))



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
        val intent = Intent(this, ReadConsumptionActivity::class.java).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
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
                        //Log.e("NDEF_RESPONSE", "ndefRecord : "+receivedString)
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


            if (pillGroup == "1") {

                if (pillPosition == "1") {
                    groupOneOne.setVisibility(View.GONE)
                    groupOneOneButton.setVisibility(View.VISIBLE)
                    groupOneOneTime.setText(pillDateTime)
                }
                if (pillPosition == "2") {
                    groupOneTwo.setVisibility(View.GONE)
                    groupOneTwoButton.setVisibility(View.VISIBLE)
                    groupOneTwoeTime.setText(pillDateTime)
                }
                if (pillPosition == "3") {
                    groupOneThree.setVisibility(View.GONE)
                    groupOneThreeButton.setVisibility(View.VISIBLE)
                    groupOneThreeTime.setText(pillDateTime)
                }
                if (pillPosition == "4") {
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

    private fun setClickListner() {

        selectPAtientLi.setOnClickListener {

            if (!VEHICLE_NUMBER.isEmpty()) {
                val dialog = SimpleSearchDialogCompat(
                        this,
                        "Patient Name",
                        "Search",
                        null,
                        VEHICLE_NUMBER,
                        object : SearchResultListener<Patient>
                        {
                            override fun onSelected(dialog : BaseSearchDialogCompat<*>, item: Patient, position: Int)
                            {

                                dialog.dismiss()
                            }
                        }
                )
                dialog.show()
                //Typeface tf = Typeface.createFromAsset(ActivityImmobiliser.this.getAssets(), "TitilliumWeb-Regular.ttf");
                // dialog.getSearchBox().setTypeface(Typeface.SERIF);
            }

        }
        gobackLi.setOnClickListener {

            finish()
        }
    }

}

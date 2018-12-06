package sns.sn.systems.pharmacist.prescription.view

import android.app.PendingIntent
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareUltralight
import android.nfc.tech.Ndef
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import kotlinx.android.synthetic.main.activity_read_write_prescription.*
import sns.sn.systems.R
import sns.sn.systems.authenticate.viewmodel.LoginViewModel
import sns.sn.systems.authenticate.viewmodel.LoginViewModelFactory
import sns.sn.systems.classes.F
import sns.sn.systems.databinding.ActivityLoginBinding
import sns.sn.systems.databinding.ActivityReadWritePrescriptionBinding
import sns.sn.systems.deviceComm.MainActivity
import sns.sn.systems.pharmacist.consumption.model.Patient
import sns.sn.systems.pharmacist.consumption.searchablespinner.BaseSearchDialogCompat
import sns.sn.systems.pharmacist.consumption.searchablespinner.SearchResultListener
import sns.sn.systems.pharmacist.consumption.searchablespinner.SimpleSearchDialogCompat
import sns.sn.systems.dialog.DialogClickInterface
import sns.sn.systems.pharmacist.prescription.viewmodel.ReadWritePrescCallbacks
import sns.sn.systems.pharmacist.prescription.viewmodel.ReadWritePrescViewModel
import sns.sn.systems.pharmacist.prescription.viewmodel.ReadWritePrescViewModelFactory
import java.io.IOException
import java.util.ArrayList



class ReadWritePrescriptionActivity : AppCompatActivity(),DialogClickInterface, ReadWritePrescCallbacks
{



    val identifier = 0
    internal var VEHICLE_NUMBER = ArrayList<Patient>()

    companion object {

        //patient details
        lateinit var PT_DOB : String
        lateinit var PT_GENDER : String
        lateinit var PT_ID : String
//        lateinit var PT_NAME : String
//        lateinit var PT_PHONE : String
//        lateinit var PT_EMAIL : String


        //prescription details
        lateinit var PRESC_NUMBER : String
        lateinit var PRESC_NAME : String
        lateinit var PRESC_STRENGTH : String
        lateinit var PRESC_DRUG_FORM : String
        lateinit var PRESC_FREQUENCY : String
        lateinit var PRESC_EXP_DATE : String
        lateinit var PRESC_PT_COST : String
        lateinit var PRESC_MFG : String
        lateinit var PRESC_LOT_NUMBER : String
        lateinit var PRESC_QUANTITY : String
        lateinit var PRESC_DAY_SPLY : String
        lateinit var PRESC_REFILL_REMAIN : String
        lateinit var PRESC_DAW_CODE : String
        lateinit var PRESC_ISSUE_DATE : String
        lateinit var PRESC_DATE_FILLED : String
        //lateinit var PRESC_EXPI_DATE : String

        //pharmacy details
        lateinit var PHARM_NAME : String
        lateinit var PHARM_ADDRESS : String
        lateinit var PHARM_NUMBER : String

        //provide details
        lateinit var PROV_NUMBER : String
        lateinit var PROV_FAX_NUMBER : String
        lateinit var PROV_DOC_NAME : String

        lateinit  var PRESC_STRING : String

        var ndef: Ndef? = null
        var nfcAdapter: NfcAdapter? = null
        var pendingIntent : PendingIntent? = null
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_read_write_prescription)
        val activityLogin = DataBindingUtil.setContentView<ActivityReadWritePrescriptionBinding>(this,R.layout.activity_read_write_prescription)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = PendingIntent.getActivity(this,0, Intent(this, ReadWritePrescriptionActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0)
        allocateStaticData();
        activityLogin.readWritePrescViewModel = ViewModelProviders.of(this, ReadWritePrescViewModelFactory(this)).get(ReadWritePrescViewModel::class.java)
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
        nfcAdapter?.disableForegroundDispatch(this)
        super.onPause()

    }
    override fun onResume()
    {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this,pendingIntent,null,null)
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        if (tag != null) {
            try {
                val mifareUltralight = MifareUltralight.get(tag)
                mifareUltralight.connect()
                try {
                    if (!F.CheckType(mifareUltralight)) {
                        F.t("The tag is not an NTAG SmartSensor. Communication is not possible.")
                    } else if (!F.CheckDynamicLockBits(mifareUltralight) || !F.CheckStaticLockBits(mifareUltralight)) {
                        F.t("Some communication memory regions are locked down. Communication is not possible.")
                    } else {
                        //call get removals
                        //checkGetRemovals();
                        F.t("Connected with tag");
                        ndef = Ndef.get(tag)

                    }
                } finally {
                    mifareUltralight.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                F.t("Unexpected communication error while connecting to tag.")
            }

        }
    }
    override fun writePrescriptionSuccess(message: String)
    {

        F.t(message)
    }

    override fun writePrescriptionFailure(message: String)
    {

    }

    override fun readPrescriptionSuccess(prescriptionDetails: String)
    {
        var PRESC_DETAILS_ARRAY = prescriptionDetails.split("#")

        var dob = PRESC_DETAILS_ARRAY.get(0)
        ptDobTxt.setText(Html.fromHtml("<b>Year Of Birth : </b>"+dob.get(0)+""+dob.get(1)+" "+F.returnMonthName(dob.get(2)+""+dob.get(3))+" "+dob.get(4)+""+dob.get(5)+""+dob.get(6)+""+dob.get(7)))
        var ptGender = PRESC_DETAILS_ARRAY.get(1)
        if(ptGender.equals("M"))
        {
            ptGenderTxt.setText(Html.fromHtml("<b>Gender : </b> Male"))
        }
        else if(ptGender.equals("F"))
        {
            ptGenderTxt.setText(Html.fromHtml("<b>Gender : </b> Female"))
        }
        else
        {
            ptGenderTxt.setText(Html.fromHtml("<b>Gender : </b> Gender not found"))
        }

       // ptNameTxt.setText(Html.fromHtml("<b>Name : </b>"+PRESC_DETAILS_ARRAY.get(2)))
        //ptPhoneTxt.setText(Html.fromHtml("<b>Phone : </b>"+PRESC_DETAILS_ARRAY.get(3)))
        //ptEmailTxt.setText(Html.fromHtml("<b>Email : </b>"+PRESC_DETAILS_ARRAY.get(4)))
        ptDignosisCodeTxt.setText(Html.fromHtml("<b>Patient Id : </b>"+PRESC_DETAILS_ARRAY.get(2)))

        prescNameTxt.setText(Html.fromHtml("<b>Name : </b>"+PRESC_DETAILS_ARRAY.get(3)))
        prescNumberTxt.setText(Html.fromHtml("<b>Number : </b>"+PRESC_DETAILS_ARRAY.get(4)))
        prescStrengthTxt.setText(Html.fromHtml("<b>Strength : </b>"+PRESC_DETAILS_ARRAY.get(5)))
        prescDrugFormulationTxt.setText(Html.fromHtml("<b>Drug Formulation : </b>"+PRESC_DETAILS_ARRAY.get(6)))
        prescFrequencyTxt.setText(Html.fromHtml("<b>Frequency : </b>"+PRESC_DETAILS_ARRAY.get(7)))

        var expDate = PRESC_DETAILS_ARRAY.get(8)
        prescExpDateTxt.setText(Html.fromHtml("<b>Expiry date : </b>"+expDate.get(0)+""+expDate.get(1)+" "+F.returnMonthName(expDate.get(2)+""+expDate.get(3))+" "+expDate.get(4)+""+expDate.get(5)+""+expDate.get(6)+""+expDate.get(7)))


        prescPtCostTxt.setText(Html.fromHtml("<b>Patient Cost (amount paid) : </b>"+PRESC_DETAILS_ARRAY.get(9)))
        prescManufactTxt.setText(Html.fromHtml("<b>Manufacturer : </b>"+PRESC_DETAILS_ARRAY.get(10)))
        prescLotNuTxt.setText(Html.fromHtml("<b>Lot Number : </b>"+PRESC_DETAILS_ARRAY.get(11)))
        prescQtyTxt.setText(Html.fromHtml("<b>Quantity : </b>"+PRESC_DETAILS_ARRAY.get(12)))
        prescDaySplyTxt.setText(Html.fromHtml("<b>Day Supply : </b>"+PRESC_DETAILS_ARRAY.get(13)))
        prescRefillRemainTxt.setText(Html.fromHtml("<b>Refills remaining : </b>"+PRESC_DETAILS_ARRAY.get(14)))
        prescDawCodeTxt.setText(Html.fromHtml("<b>DAW Code : </b>"+PRESC_DETAILS_ARRAY.get(15)))

        var issuedDate = PRESC_DETAILS_ARRAY.get(16)
        prescIssuedDateTxt.setText(Html.fromHtml("<b>Issued Date : </b>"+issuedDate.get(0)+""+issuedDate.get(1)+" "+F.returnMonthName(issuedDate.get(2)+""+issuedDate.get(3))+" "+issuedDate.get(4)+""+issuedDate.get(5)+""+issuedDate.get(6)+""+issuedDate.get(7)))


        var ExpiryDate = PRESC_DETAILS_ARRAY.get(17)
        prescDateFilledTxt.setText(Html.fromHtml("<b>Date Filled : </b>"+ExpiryDate.get(0)+""+ExpiryDate.get(1)+" "+F.returnMonthName(ExpiryDate.get(2)+""+ExpiryDate.get(3))+" "+ExpiryDate.get(4)+""+ExpiryDate.get(5)+""+ExpiryDate.get(6)+""+ExpiryDate.get(7)))


        //var ExpDate = PRESC_DETAILS_ARRAY.get(18)
       // prescExpDateeTxt.setText(Html.fromHtml("<b>Exp Date : </b>"+ExpDate.get(0)+""+ExpDate.get(1)+" "+F.returnMonthName(ExpDate.get(2)+""+ExpDate.get(3))+" "+ExpDate.get(4)+""+ExpDate.get(5)+""+ExpDate.get(6)+""+ExpDate.get(7)))


        pharmNameTxt.setText(Html.fromHtml("<b>Name : </b>"+PRESC_DETAILS_ARRAY.get(18)))
        pharmAddressTxt.setText(Html.fromHtml("<b>Address : </b>"+PRESC_DETAILS_ARRAY.get(19)))
        pharmPhNumberTxt.setText(Html.fromHtml("<b>Phone Number : </b>"+PRESC_DETAILS_ARRAY.get(20)))

        providerPhNumberTxt.setText(Html.fromHtml("<b>Phone Number : </b>"+PRESC_DETAILS_ARRAY.get(21)))
        providerFaxNumberTxt.setText(Html.fromHtml("<b>Fax Number : </b>"+PRESC_DETAILS_ARRAY.get(22)))
        providerDocNameTxt.setText(Html.fromHtml("<b>Doctor name : </b>"+PRESC_DETAILS_ARRAY.get(23)))

        F.t("Prescription read successfully")

    }

    override fun readPrescriptionFailure(message: String)
    {

    }
    override fun notifyUser(message: String)
    {
        F.t(message)
    }

    private fun allocateStaticData()
    {
        //patient details
        //PT_NAME = "Amol"
        //PT_PHONE = "8806283610"
        //PT_EMAIL= "amolwakchaure14@gmail.com"

        PT_DOB= "05.10.1990"
        PT_GENDER= "Male"
        PT_ID= "1234"

        //prescription details
        PRESC_NAME= "ABC Prescrption"
        PRESC_NUMBER= "123456"
        PRESC_STRENGTH= "12"
        PRESC_DRUG_FORM= "1234556"
        PRESC_FREQUENCY= "52"
        PRESC_EXP_DATE= "27.11.2018"
        PRESC_PT_COST= "321"
        PRESC_MFG= "ABC ltd"
        PRESC_LOT_NUMBER= "963"
        PRESC_QUANTITY= "123"
        PRESC_DAY_SPLY= "2332"
        PRESC_REFILL_REMAIN= "123"
        PRESC_DAW_CODE= "456465"
        PRESC_ISSUE_DATE= "27.10.2018"
        PRESC_DATE_FILLED= "27.09.2018"
        //PRESC_EXPI_DATE= "27.09.2018"

        //pharmacy details
        PHARM_NAME= "ABC ltd"
        PHARM_ADDRESS= "sn systems,301 parbati industries, swargate, pune"
        PHARM_NUMBER= "132"

        //provide details
        PROV_NUMBER= "+915326425321"
        PROV_FAX_NUMBER= "9655323"
        PROV_DOC_NAME= "sdbb sdf"

        val PT_DOBb = padRight(PT_DOB.replace(".", ""), 8)
        val PT_GENDERr = padRight(PT_GENDER, 1)
        val PT_IDr = padRight(PT_ID, 8)

        val PRESC_NAME1 = padRight(PRESC_NAME, 15)
        val PRESC_NUMBER1 = padRight(PRESC_NUMBER, 8)
        val PRESC_STRENGTH1 = padRight(PRESC_STRENGTH, 4)
        val PRESC_DRUG_FORM1 = padRight(PRESC_DRUG_FORM, 3)
        val PRESC_FREQUENCY1 = padRight(PRESC_FREQUENCY, 140)
        val PRESC_EXP_DATE1 = padRight(PRESC_EXP_DATE.replace(".", ""), 8)
        val PRESC_PT_COST1 = padRight(PRESC_PT_COST, 6)
        val PRESC_MFG1 = padRight(PRESC_MFG, 4)
        val PRESC_LOT_NUMBER1 = padRight(PRESC_LOT_NUMBER, 9)
        val PRESC_QUANTITY1 = padRight(PRESC_QUANTITY, 3)
        val PRESC_DAY_SPLY1 = padRight(PRESC_DAY_SPLY, 2)
        val PRESC_REFILL_REMAIN1 = padRight(PRESC_REFILL_REMAIN, 2)
        val PRESC_DAW_CODE1 = padRight(PRESC_DAW_CODE, 1)
        val PRESC_ISSUE_DATE1 = padRight(PRESC_ISSUE_DATE.replace(".", ""), 8)
        val PRESC_DATE_FILLED1 = padRight(PRESC_DATE_FILLED.replace(".", ""), 8)

        val PHARM_NAME1 = padRight(PHARM_NAME, 25)
        val PHARM_ADDRESS1 = padRight(PHARM_ADDRESS, 57)
        val PHARM_NUMBER1 = padRight(PHARM_NUMBER, 10)

        val PROV_NUMBER1 = padRight(PROV_NUMBER, 10)
        val PROV_FAX_NUMBER1 = padRight(PROV_FAX_NUMBER, 10)
        val PROV_DOC_NAME1 = padRight(PROV_DOC_NAME, 25)

        val buffer = StringBuffer()

        PRESC_STRING = buffer.append(
                PT_DOBb + "" + PT_GENDERr + "" + PT_IDr + "" + PRESC_NAME1 + "" + PRESC_NUMBER1 + "" + PRESC_STRENGTH1 + ""
                + PRESC_DRUG_FORM1 + "" + PRESC_FREQUENCY1 + "" + PRESC_EXP_DATE1 + "" + PRESC_PT_COST1 + "" + PRESC_MFG1 + ""
                + PRESC_LOT_NUMBER1 + "" + PRESC_QUANTITY1 + "" + PRESC_DAY_SPLY1 + "" + PRESC_REFILL_REMAIN1 + ""
                + PRESC_DAW_CODE1 + "" + PRESC_ISSUE_DATE1 + "" + PRESC_DATE_FILLED1 + "" + PHARM_NAME1 + "" + PHARM_ADDRESS1 + "" + PHARM_NUMBER1 + "" + PROV_NUMBER1 + "" + PROV_FAX_NUMBER1 + "" + PROV_DOC_NAME1).toString()

        Log.e("PRESCRIPTION_DETAILS","PRESC_STRING  :"+PRESC_STRING)

        /*for (i in 0 until PRESC_STRING.length)
        {
            Log.e("PRESCRIPTION_ARRAY",""+"PRESCRIPTION_ARRAY [" + i + "] : " + PRESC_STRING.get(i))

        }*/

    }

    private fun setClickListner() {

        selectPaitCard.setOnClickListener {

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

    override fun onClickPositiveButton(pDialog: DialogInterface, pDialogIntefier: Int)
    {
        if (pDialogIntefier == 0)
            F.t("Write")
        pDialog.dismiss()


    }
    override fun onClickNegativeButton(pDialog: DialogInterface, pDialogIntefier: Int)
    {

        if (pDialogIntefier == 0)
            F.t("Cancel")
        pDialog.dismiss()
    }

    fun padRight(s: String, n: Int): String {
        return String.format("%0$-" + n + "s", s).substring(0, n)
    }
}

package sns.sn.systems.deviceComm

import android.app.PendingIntent
import android.content.Intent
import android.nfc.*
import android.nfc.tech.MifareUltralight
import android.nfc.tech.Ndef
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import sns.sn.systems.R
import sns.sn.systems.classes.F
import java.io.IOException
import java.util.ArrayList

class MainActivity : AppCompatActivity()
{

    var nfcAdapter : NfcAdapter? = null
    var pendingIntent : PendingIntent? = null
    var ndef : Ndef? = null
    private val DATA = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = PendingIntent.getActivity(this,0, Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0)
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

                        checkStaticCode()
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

    private fun checkStaticCode()
    {

        if (DATA.size < 5) {
            DATA.add("1")
            if (DATA.size == 1)
            {
                oneOne.setVisibility(View.GONE)
                oneOneButton.setVisibility(View.VISIBLE)
            } else if (DATA.size == 2) {
                twoOne.setVisibility(View.GONE)
                twoOneButton.setVisibility(View.VISIBLE)
            } else if (DATA.size == 3) {
                threeOne.setVisibility(View.GONE)
                threeOneButton.setVisibility(View.VISIBLE)
            } else if (DATA.size == 4) {
                fourOne.setVisibility(View.GONE)
                fourOneButton.setVisibility(View.VISIBLE)
            } else if (DATA.size == 5) {
                oneTwo.setVisibility(View.GONE)
                oneTwoButton.setVisibility(View.VISIBLE)
            }
        }


    }
    private fun writeReadNdef(payload: ByteArray): List<ByteArray>? {
        var responses: List<ByteArray>? = null
        try {
            ndef?.connect()
            try {
                writeNdef(payload)
                try { /* Give the NHS31xx some time to respond. */
                    Thread.sleep(111)
                } catch (e: InterruptedException) {
                    Log.e("sleep", e.toString())
                }

                responses = readNdef()
            } finally {
                ndef?.close()
            }
        } catch (e: NullPointerException) {
            Log.e("writeReadNdef", e.toString())
        } catch (e: IOException) {
            Log.e("writeReadNdef", e.toString())
        } catch (e: IllegalStateException) {
            Log.e("writeReadNdef", e.toString())
        }

        return responses
    }

    @Throws(IOException::class)
    private fun readNdef(): List<ByteArray> {
        val responses = ArrayList<ByteArray>()
        try {
            val ndefMessage = ndef?.getNdefMessage()
            for (r in ndefMessage!!.records) {
                val response = r.payload
                if (r.tnf.toInt() != 2) {
                    Log.d("readNdef", "Skipping a non-mime record")
                } else if (response == null || response.size < 2) {
                    Log.i("readNdef", "Empty record or record too short. Ignored.")
                } else if (response[1].toInt() != 1) {
                    Log.d("readNdef", "Tag byte not 1. Did we read back our own command?")
                } else {
                    responses.add(response)
                }
            }
        } catch (e: FormatException) {
            Log.i("readNdef", e.toString())
        }

        return responses
    }

    @Throws(IOException::class)
    private fun writeNdef(payload: ByteArray) {
        val record = android.nfc.NdefRecord.createMime("n/p", payload)
        val message = NdefMessage(arrayOf(record))
        try {

            Log.d("writeNdef", "MESSAGE : " + message.toString())
            ndef?.writeNdefMessage(message)
        } catch (e: FormatException) {
            Log.e("writeNdef", e.toString())
        }

    }

}

package sns.sn.systems.classes

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.nfc.tech.MifareUltralight
import android.util.Log
import android.widget.Toast
import sns.sn.systems.R
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.experimental.and

class F
{
    companion object
    {

        fun returnMonthName(monthNumber : String ) : String
        {
            var  monthName : String
            if(monthNumber.equals("01"))
            {
                monthName = "Jan"
            }
            else if(monthNumber.equals("02"))
            {
                monthName = "Feb"
            }
            else if(monthNumber.equals("03"))
            {
                monthName = "Mar"
            }
            else if(monthNumber.equals("04"))
            {
                monthName = "Apr"
            }
            else if(monthNumber.equals("05"))
            {
                monthName = "May"
            }
            else if(monthNumber.equals("06"))
            {
                monthName = "Jun"
            }
            else if(monthNumber.equals("07"))
            {
                monthName = "Jul"
            }
            else if(monthNumber.equals("08"))
            {
                monthName = "Aug"
            }
            else if(monthNumber.equals("09"))
            {
                monthName = "Sep"
            }
            else if(monthNumber.equals("10"))
            {
                monthName = "Oct"
            }
            else if(monthNumber.equals("11"))
            {
                monthName = "Nov"
            }
            else if(monthNumber.equals("12"))
            {
                monthName = "Dec"
            }
            else
            {
                monthName = "Date not found"
            }
            return monthName
        }

        fun isNetworkAvailable(): Boolean
        {
            val connectivityManager = MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }
        fun t(message : String)
        {
            Toast.makeText(MyApplication.context,message,Toast.LENGTH_LONG).show()
        }

        fun e(message: String)
        {
            Log.e("NFC",message)
        }


        fun CheckType(mifareUltralight: MifareUltralight): Boolean {
            /* From the HW documentation:
         * The GET_VERSION command [60h] is used to retrieve information on the MIFARE family, product
         * version, storage size and other product data required to identify the product. This command is
         * available on other MIFARE products to have a common way of identifying products across platforms
         * and evolution steps. The GET_VERSION command has no arguments and replies the version
         * information for the specific type.
         * GET_VERSION response for NHS devices:
         * 0	Fixed header	00h
         * 1	Vendor ID	04h	NXP Semiconductors
         * 2	Product type	04h	NTAG
         * 3	Product subtype	06h	NHS
         * 4	Major product version	00h
         * 5	Minor product version	00h
         * 6	Size	13h
         * 7	Protocol type	03h	ISO/IEC 14443-3 compliant
         */
            val NFC_GET_VERSION = 0x60.toByte()
            var ok = false
            try {
                val bytes = mifareUltralight.transceive(byteArrayOf(NFC_GET_VERSION))
                ok = (bytes != null
                        && bytes.size == 8
                        && bytes[0].toInt() == 0x00
                        && bytes[1].toInt() == 0x04
                        && bytes[2].toInt() == 0x04
                        && bytes[3].toInt() == 0x06
                        && bytes[7].toInt() == 0x03)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return ok
        }

        fun CheckDynamicLockBits(mifareUltralight: MifareUltralight): Boolean {
            /* From the HW documentation:
         * To lock the pages starting a page address 10h (16d) onwards, the lock bytes 2 to 4 located in
         * page E2h (226d) are used. [...] The granularity is 16 pages [...]. When a lock bit is set to one,
         * the corresponding page range is made read only. The block lock (BL) bits protect the lock bits.
         * Each block lock bit protects two lock bits. When the block lock bit is set, the corresponding
         * lock bits are read only. Block lock and lock bits are one time programmable (OTP). [...]
         * The dynamic lockage page is protected against corruption by a valid flag. The valid flag has to
         * be set to 0xbd.
         */
            val DYNAMIC_LOCKBITS_PAGE = 0xE2
            var ok = false
            try {
                val rx = mifareUltralight.readPages(DYNAMIC_LOCKBITS_PAGE)
                if (rx != null && rx.size == 4 * 4) { /* MIFARE Ultralight compatible NFC tags are read 4 pages at a time. */
                    if (rx[0].toInt() == 0x00 && rx[1] and 0x3F == 0x00.toByte()) {
                        /* All 'dynamic' pages can be written to, check if the block lock bits are set. */
                        if ((rx[2] and 0x7F) != 0x7F.toByte()) {
                            val tx = ByteArray(4)
                            System.arraycopy(rx, 0, tx, 0, 4)
                            tx[2] = 0x7F
                            mifareUltralight.writePage(DYNAMIC_LOCKBITS_PAGE, tx)
                        }
                        ok = true
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return ok
        }

        fun CheckStaticLockBits(mifareUltralight: MifareUltralight): Boolean {
            /* From the HW documentation:
         * The bits of byte 2 and byte 3 of page 02h represent the field programmable read-only locking
         * mechanism. Each page from 03h (Capability Container) to 0Fh can be individually locked by setting
         * the corresponding locking bit Lx to logic 1 to prevent further write access. After locking the
         * corresponding page becomes read-only memory. The three least significant bits of lock byte 0 are
         * the block-locking bits. Bit 2 deals with pages 0Ah to 0Fh, bit 1 deals with pages 04h to 09h and
         * bit 0 deals with page 03h (Capability Container). Once the block locking bits are set, the
         * locking configuration for the corresponding memory area is frozen.
         */
            val STATIC_LOCKBITS_PAGE = 0x02
            var ok = false
            try {
                val rx = mifareUltralight.readPages(STATIC_LOCKBITS_PAGE)
                if (rx != null && rx.size == 4 * 4) { /* MIFARE Ultralight compatible NFC tags are read 4 pages at a time. */
                    if (rx[2] and 0xF8.toByte() == 0x00.toByte() && rx[3].toInt() == 0x00) {
                        /* All 'static' pages can be written to, check if the block lock bits are set. */
                        if (rx[2] and 0x07 != 0x07.toByte()) {
                            val tx = ByteArray(4)
                            System.arraycopy(rx, 0, tx, 0, 4)
                            tx[2] = 0x07
                            mifareUltralight.writePage(STATIC_LOCKBITS_PAGE, tx)
                        }
                        ok = true
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return ok
        }




    }

}
package sns.sn.systems.dialog

import android.content.DialogInterface
import android.R.id.button1
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.TextView
import android.view.Window.FEATURE_NO_TITLE
import android.widget.Button
import sns.sn.systems.R


class CustomAlertDialog : DialogClickInterface, DialogInterface.OnClickListener
{

    var mDialogClickInterface: DialogClickInterface? = null
    private var mDialogIdentifier: Int = 0
    private var mContext: Context? = null

    /**
     * Show confirmation dialog with two buttons
     *
     * @param pMessage
     * @param pPositiveButton
     * @param pNegativeButton
     * @param pContext
     * @param pDialogIdentifier
     */

    fun showConfirmDialog(pTitle: String, pMessage: String,
                          pPositiveButton: String, pNegativeButton: String,
                          pContext: Context, pDialogIdentifier: Int) {

        mDialogClickInterface = pContext as DialogClickInterface
        mDialogIdentifier = pDialogIdentifier
        mContext = pContext

        val dialog = Dialog(pContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_confirm_dialog)

        if (pTitle != "") {
            val title = dialog.findViewById(R.id.textTitle) as TextView
            title.text = pTitle
            title.visibility = View.VISIBLE
        }

        val text = dialog.findViewById(R.id.textDialog) as TextView
        text.text = pMessage
        val button = dialog.findViewById(R.id.button) as Button
        button.setText(pPositiveButton)
        val button1 = dialog.findViewById(R.id.button1) as Button
        button1.setText(pNegativeButton)
        dialog.setCancelable(false)
        dialog.show()      // if decline button is clicked, close the custom dialog
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // Close dialog
                mDialogClickInterface!!.onClickPositiveButton(dialog, pDialogIdentifier)
            }
        })
        button1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // Close dialog
                mDialogClickInterface!!.onClickNegativeButton(dialog, pDialogIdentifier)
            }
        })

    }

    override fun onClick(pDialog: DialogInterface, pWhich: Int) {

        when (pWhich) {
            DialogInterface.BUTTON_POSITIVE -> mDialogClickInterface!!.onClickPositiveButton(pDialog, mDialogIdentifier)
            DialogInterface.BUTTON_NEGATIVE -> mDialogClickInterface!!.onClickNegativeButton(pDialog, mDialogIdentifier)
        }

    }

    override fun onClickPositiveButton(pDialog: DialogInterface, pDialogIntefier: Int) {}

    override fun onClickNegativeButton(pDialog: DialogInterface, pDialogIntefier: Int) {}

    companion object {

        var mDialog: CustomAlertDialog? = null

        val instance: CustomAlertDialog
            get() {

                if (mDialog == null)
                    mDialog = CustomAlertDialog()

                return mDialog!!

            }
    }

}
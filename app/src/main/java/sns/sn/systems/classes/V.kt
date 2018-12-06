package sns.sn.systems.classes

import android.text.TextUtils

class V
{

    companion object
    {

        //check empty editext field
        fun isCheckEmpty(data : String?,message : String) : Boolean
        {
            var status = false
            try
            {
                if(TextUtils.isEmpty(data))
                {
                     F.t(message)
                    status = false
                }
                else
                {
                    status = true
                }
            }
            catch (e: Exception)
            {
                F.e("isCheckEmpty" + e.toString())
            }
            return status

        }

    }
}
package sns.sn.systems.authenticate.interfaces

interface LoginResultCallbacks
{
    fun onSuccess(userType : String,userName : String,userPassword : String)
    fun onFailure(message : String)
}
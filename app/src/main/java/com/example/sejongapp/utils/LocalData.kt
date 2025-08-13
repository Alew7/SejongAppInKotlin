import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.sejongapp.SpleshLoginPages.SplashLoginActivity
import com.example.sejongapp.models.DataClasses.UserData


object LocalData {
    fun getSavedToken(context: Context): String {
        val prefs = context.getSharedPreferences("Settings", MODE_PRIVATE)
        return prefs.getString("token", "null") ?: "null"
    }

    fun setToken(context: Context,token: String) {

        Log.i("Token_TAG", "Trying to save the token! token = $token")
        val prefs = context.getSharedPreferences("Settings", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("token", token)
        editor.apply()

//        startActivity(context, intent, null)
    }

    fun deletToken (context: Context) {
        Log.i("Token_TAG", "Trying to delete the token!")
        val prefs = context.getSharedPreferences("Settings", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("token").apply()
        Log.i("Token_TAG", "Token after deletion: ${prefs.getString("token", "null")}")

        Log.i("Token_TAG", "The token has deleted")


        val intent = Intent(context,SplashLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    fun getUserData(context: Context): UserData {
        val prefs = context.getSharedPreferences("Settings", MODE_PRIVATE)
        return UserData(
            prefs.getString("username", "null") ?: "null",
            prefs.getString("avatar", "null") ?: "null",
            prefs.getString("fullname", "null") ?: "null",
            prefs.getString("email", "null") ?: "null",
            prefs.getString("status", "null") ?: "null",
            prefs.getString("groups", "null")?.split(",") ?: listOf()
        )
    }

    fun setUserData(context: Context,userdata: UserData) {

        Log.i("user_TAG", "Trying to save the userData user = $userdata")
        val prefs = context.getSharedPreferences("Settings", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("username", userdata.username)
        editor.putString("avatar", userdata.avatar)
        editor.putString("fullname", userdata.fullname)
        editor.putString("email", userdata.email)
        editor.putString("status", userdata.status)
        editor.putString("groups", userdata.groups.toString())
        editor.apply()
    }

    fun deleteUserData (context: Context) {
        Log.i("Token_TAG", "Trying to delete the token!")
        val prefs = context.getSharedPreferences("Settings", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("username")
        editor.remove("avatar")
        editor.remove("fullname")
        editor.remove("email")
        editor.remove("status")
        editor.remove("groups")
        editor.apply()
    }

}
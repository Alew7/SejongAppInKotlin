import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.sejongapp.DataClasses.UserData
import com.example.sejongapp.SpleshLoginPages.SplashLoginActivity

import java.util.*


object LocalToken {

    private const val PREFERENCES_NAME = "user_prefs"

//    fun setLocal(context: Context, token: String): Context {
//        val locale = Locale(token)
//        Locale.setDefault(locale)
//
//        val config = Configuration(context.resources.configuration)
//        config.setLocale(locale)
//
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            context.createConfigurationContext(config)
//        } else {
//            @Suppress("DEPRECATION")
//            context.resources.updateConfiguration(config, context.resources.displayMetrics)
//            context
//        }
//    }


    fun getSavedToken(context: Context): String {
        val prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        return prefs.getString("token", "null") ?: "null"
    }

    fun setToken(context: Context, token: String, intent: Intent) {

        Log.i("Token_TAG", "Trying to save the token! token = $token")
        val prefs = context.getSharedPreferences("Settings", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("token", token)
        editor.apply()

        startActivity(context, intent, null)
    }

    fun deletToken(context: Context) {
        Log.i("Token_TAG", "Trying to delete the token!")
        val prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("token")
        editor.apply()

        Log.i("Token_TAG", "The token has deleted")


        val intent = Intent(context, SplashLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    fun getUserData(context: Context) : UserData {
        val prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        return UserData(
            prefs.getString("fullname", "null") ?: "null",
            prefs.getString("email", "null") ?: "null",
            prefs.getString("groups", "null") ?: "null",
            prefs.getString("phone", "null") ?: "null",
        )
    }





}
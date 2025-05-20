import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import java.util.*

object LocalToken {

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
        return prefs.getString("token", "token") ?: "null"
    }

    fun setToken(context: Context,token: String, intent: Intent) {

        Log.i("Token_TAG", "Trying to save the token! token = $token")
        val prefs = context.getSharedPreferences("Settings", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("token", token)
        editor.apply()

        startActivity(context, intent, null)
    }




}
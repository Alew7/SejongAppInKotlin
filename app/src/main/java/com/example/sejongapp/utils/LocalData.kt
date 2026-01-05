import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.sejongapp.SpleshLoginPages.SplashLoginActivity
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.utils.UserStatusEnum
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream



object LocalData {
    private const val PREFS = "app_prefs"
    private const val KEY_LANG = "app_lang"

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


    fun setLanguage(context: Context, lang: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANG, lang)
            .apply()
    }

    fun getSavedLanguage(context: Context): String? {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_LANG, null)
    }

    fun getUserData(context: Context): UserData {
        val prefs = context.getSharedPreferences("Settings", MODE_PRIVATE)
        val statusOrdinal: Int = prefs.getInt("status", UserStatusEnum.UNKNOWN.ordinal)
        val status: UserStatusEnum = UserStatusEnum.fromOrdinal(statusOrdinal)
        return UserData(
            prefs.getString("username", "null") ?: "null",
            prefs.getString("avatar", "null") ?: "null",
            prefs.getString("fullname", "null") ?: "null",
            prefs.getString("email", "null") ?: "null",
            status,
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
        editor.putInt("status", userdata.status.ordinal)
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



    fun compressImageToTempFile(context: Context, uri: Uri, quality: Int): File? {
        try {
            // 1. Get the InputStream from the original URI
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val originalBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)

            // 2. Prepare a ByteArrayOutputStream for compression
            val outputStream = ByteArrayOutputStream()

            // 3. Compress the Bitmap (e.g., to JPEG, 75% quality)
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            val compressedBytes = outputStream.toByteArray()

            // 4. Create a new temporary file to store the compressed data
            val compressedFile = File(context.cacheDir, "compressed_avatar_${System.currentTimeMillis()}.jpg")
            FileOutputStream(compressedFile).use { fos ->
                fos.write(compressedBytes)
            }

            // Recycle the original bitmap if you don't need it anymore
            originalBitmap.recycle()

            return compressedFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }





}
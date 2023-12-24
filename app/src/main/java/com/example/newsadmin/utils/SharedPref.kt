import android.content.Context
import android.content.SharedPreferences
import com.example.newsadmin.models.User
import com.google.gson.Gson

class SharedPreferencesManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    companion object {
        private const val PREF_NAME = "news_app_pref"
        private const val KEY_USER = "user"
        private const val KEY_LOGGED_IN = "is_logged_in"

        private var INSTANCE: SharedPreferencesManager? = null

        fun getInstance(context: Context): SharedPreferencesManager {
            if (INSTANCE == null) {
                synchronized(SharedPreferencesManager::class.java) {
                    INSTANCE = SharedPreferencesManager(context.applicationContext)
                }
            }
            return INSTANCE!!
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false)
    }

    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().apply {
            putString(KEY_USER, userJson)
            putBoolean(KEY_LOGGED_IN, true)
            apply()
        }
    }

    fun updateUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().apply {
            putString(KEY_USER, userJson)
            apply()
        }
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().apply {
            putString("token", token)
            apply()
        }
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(KEY_USER, null)
        return gson.fromJson(userJson, User::class.java)
    }
    fun logout() {
        sharedPreferences.edit().apply {
            putBoolean(KEY_LOGGED_IN, false)
            remove(KEY_USER)
            apply()
        }
    }

    fun getUserId(): String? {
        val userJson = sharedPreferences.getString(KEY_USER, null)
        return if (userJson != null) {
            val user = gson.fromJson(userJson, User::class.java)
            user._id
        } else {
            null
        }
    }
}

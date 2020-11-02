package uz.napa.eyecaria.ui.activity

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.fragment_settings.*
import uz.napa.eyecaria.R
import uz.napa.eyecaria.ui.fragments.DARK_MODE
import uz.napa.eyecaria.ui.fragments.SHARED_PREF
import uz.napa.eyecaria.util.MyContextWrapper
import uz.napa.eyecaria.util.MyPreference

class MainActivity : AppCompatActivity() {
    private lateinit var myPref:MyPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = getSharedPreferences(SHARED_PREF, 0)
        if (sharedPref.getBoolean(DARK_MODE, false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }


    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.let {
            val uiMode = it.uiMode
            it.setTo(baseContext.resources.configuration)
            it.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun attachBaseContext(newBase: Context?) {
        myPref = MyPreference(newBase!!)
        val lang = myPref.getLang()
        super.attachBaseContext(MyContextWrapper.wrap(newBase,lang))
    }
}
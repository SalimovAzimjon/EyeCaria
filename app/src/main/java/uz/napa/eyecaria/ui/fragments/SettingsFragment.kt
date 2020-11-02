package uz.napa.eyecaria.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_settings.*
import uz.napa.eyecaria.R
import uz.napa.eyecaria.ui.activity.MainActivity
import uz.napa.eyecaria.util.MyPreference
import uz.napa.eyecaria.util.PREFERENCE_NAME

const val SHARED_PREF = "SharedPreference"
const val DARK_MODE = "DARK_MODE"

class SettingsFragment : BaseFragment(R.layout.fragment_settings, R.color.colorAccent) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_back.setOnClickListener { findNavController().popBackStack() }
        handleDarkMode()
        handleLang()
    }

    private fun handleLang() {
        val myPref = MyPreference(requireContext())
        val lang = myPref.getLang()
        if (lang == "uz")
            switch_language.isChecked = true
        if (lang == "en")
            switch_language.isChecked = false

        switch_language.setOnCheckedChangeListener { compoundButton, isChekced ->
            if (isChekced){
                myPref.setLang("uz"){
                    startActivity(Intent(context, MainActivity::class.java))
                    requireActivity().finish()
                }

            }
            else{
                myPref.setLang("en"){
                    startActivity(Intent(context, MainActivity::class.java))
                    requireActivity().finish();
                }
            }
        }
    }

    private fun handleDarkMode() {
        val sharedPref = requireActivity().getSharedPreferences(SHARED_PREF, 0)
        val editor = sharedPref.edit()

        if (sharedPref.getBoolean(DARK_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            switch_night_mode.isChecked = true
        }


        switch_night_mode.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            editor.putBoolean(DARK_MODE, isChecked)
            editor.apply()
        }
    }


}
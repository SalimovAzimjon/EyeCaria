package uz.napa.eyecaria.util

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*


class MyContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {

        fun wrap(
            context: Context,
            language: String?
        ): MyContextWrapper? {
            var mContext = context
            val res: Resources = context.resources
            val configuration: Configuration = res.getConfiguration()
            val newLocale = Locale(language!!)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(newLocale)
                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
                mContext = context.createConfigurationContext(configuration)
            } else
                configuration.setLocale(newLocale)
                mContext = context.createConfigurationContext(configuration)
            return MyContextWrapper(mContext)
        }
        @Suppress("DEPRECATION")
        private fun getSystemLocaleLegacy(config: Configuration): Locale {
            return config.locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun getSystemLocale(config: Configuration): Locale {
            return config.locales.get(0)
        }

        @Suppress("DEPRECATION")
        private fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
            config.locale = locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun setSystemLocale(config: Configuration, locale: Locale) {
            config.setLocale(locale)
        }
    }
}
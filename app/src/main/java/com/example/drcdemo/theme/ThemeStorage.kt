package com.example.drcdemo.theme

import android.content.Context
import com.example.drcdemo.R

class ThemeStorage {

    companion object {
        fun setThemeColor(context: Context, themeColor: Int) {
            val sharedPreferences = context.getSharedPreferences("theme_data", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("theme", themeColor)
            editor.apply()
        }

        fun getThemeColor(context: Context): Int? {
            val sharedPreferences = context.getSharedPreferences("theme_data", Context.MODE_PRIVATE)
            return sharedPreferences.getInt("theme", R.id.theme1)
        }
    }
}
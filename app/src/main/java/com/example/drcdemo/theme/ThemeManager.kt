package com.example.drcdemo.theme

import android.content.Context
import com.example.drcdemo.R

class ThemeManager {

    companion object {
        fun setCustomizedThemes(context: Context, theme: Int?) {
            when (theme) {
                R.id.theme1 -> context.setTheme(R.style.Theme1)
                R.id.theme2-> context.setTheme(R.style.Theme2)
                R.id.theme3 -> context.setTheme(R.style.Theme3)
                R.id.theme4 -> context.setTheme(R.style.Theme4)
            }
        }
    }
}